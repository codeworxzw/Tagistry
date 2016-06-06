package com.pk.tagger.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.pk.tagger.BuildConfig;
import com.pk.tagger.R;
import com.pk.tagger.managers.SessionManager;
import com.stormpath.sdk.Stormpath;
import com.stormpath.sdk.StormpathCallback;
import com.stormpath.sdk.StormpathConfiguration;
import com.stormpath.sdk.StormpathLogger;
import com.stormpath.sdk.models.SocialProviderConfiguration;
import com.stormpath.sdk.models.SocialProvidersResponse;
import com.stormpath.sdk.models.StormpathError;
import com.stormpath.sdk.models.UserProfile;
import com.stormpath.sdk.providers.FacebookLoginProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Kieran on 26/01/2016.
 */
public class LoginActivity extends AppCompatActivity implements FacebookCallback<LoginResult>, GoogleApiClient.OnConnectionFailedListener {

    //called on Login page
    SessionManager session;

    private GoogleApiClient mGoogleApiClient;

    private static final int RC_SIGN_IN = 9001;

    private static final String TAG = "LoginActivity";
    private static final int REQUEST_REGISTER = 0;
    private static final String LOGIN_URL = "https://gigitch.duckdns.org/oauth/token";
    public static final String KEY_EMAIL = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_GRANT_TYPE = "grant_type";

    CallbackManager callbackManager;
    // temporary string to show the parsed response
    private String jsonResponse;

    //initialise views using ButterKnife
    @Bind(R.id.input_email) EditText _emailText;
    @Bind(R.id.input_password) EditText _passwordText;
    @Bind(R.id.btn_login) Button _loginButton;
    @Bind(R.id.link_register) TextView _registerLink;
    @Bind(R.id.link_skiplogin) TextView _skiploginLink;

    //used to intercept the social media oauth callbacks
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // getIntent() should always return the most recent
        setIntent(intent);

        //check contents of intent
        if (getIntent().getData() != null && getIntent().getData().getScheme() != null) {
                Log.d("catch", "this");
            if (getIntent().getData().getScheme().contentEquals(getString(R.string.facebook_app_id))) {

            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        session = new SessionManager(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("user_status", "email"));
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email"));
        LoginManager.getInstance().registerCallback(callbackManager, this);

        if (BuildConfig.DEBUG) {
            // we only want to show the logs in debug builds, for easier debugging
            Stormpath.setLogLevel(StormpathLogger.VERBOSE);
        }

        //initialise login button below
        _loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        //intialise Register link below
        _registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Start register activity
                Toast.makeText(getApplicationContext(), "Register is selected!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivityForResult(intent, REQUEST_REGISTER);
            }
        });

        _skiploginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Start register activity
                Toast.makeText(getApplicationContext(), "Skipping login!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        String serverClientId = getString(R.string.goog_app_id);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(new Scope(Scopes.EMAIL))
                .requestServerAuthCode(serverClientId, false)
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setScopes(gso.getScopeArray());

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("google click", "test ");
                signIn();
            }
        });



    }
    // login method

    public void login() {

        Log.d(TAG, "Login");
        //ensures that fields are correct by calling validate method
        //Toast.makeText(getApplicationContext(), "Will make login POST request soon!", Toast.LENGTH_SHORT).show();
        Toast.makeText(getApplicationContext(), "User Login Status: " + session.isLoggedIn(), Toast.LENGTH_LONG).show();


        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        if (!validate()) {
            onLoginFailed(progressDialog);
            return;
        }

        _loginButton.setEnabled(false);

        // get fields and TODO: insert into HTTP POST request below
        final String email = _emailText.getText().toString();
        final String password = _passwordText.getText().toString();
        // TODO: Implement your own authentication logic here. (make HTTP POST request to <server>/oauth/token)
        // JSON {"username":"bob@email.com", "password":"hunter2" or "Bob12345"}

        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Valid Response: " + response.toString());

                        try {
                            // Parsing json object response
                            JSONObject j = new JSONObject(response);
                            jsonResponse = "";
                            String token = j.getString("access_token");
                            jsonResponse += "Access token: " + token + "\n\n";
                            Log.d(TAG, jsonResponse);
                            session.createLoginSession(email, token);
                            onLoginSuccess();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "Error Response: " + error.toString());
                        onLoginFailed(progressDialog);

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Log.d(TAG, "Timeout/no connection error: " + error.toString());
                            Toast.makeText(getBaseContext(), "Please make sure you have internet connection", Toast.LENGTH_LONG);
                        } else if (error instanceof AuthFailureError) {
                            Log.d(TAG, "AuthFailure error: " + error.toString());
                        } else if (error instanceof ServerError) {
                            Log.d(TAG, "Server error: " + error.toString());
                        } else if (error instanceof NetworkError) {
                            Log.d(TAG, "Network error: " + error.toString());
                        } else if (error instanceof ParseError) {
                            Log.d(TAG, "Parse error: " + error.toString());
                        }
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put(KEY_PASSWORD, password);
                params.put(KEY_EMAIL, email);
                params.put(KEY_GRANT_TYPE, "password");
                Log.d("Params", "params: " + params.toString());
                return params;

            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Accept", "application/json");
                return headers;
            }

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=utf-8";
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
        Log.d(TAG, jsonObjectRequest.toString());

    }

    //retrieves the intent from RegisterActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //send this to the facebook button

        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_REGISTER) {
            if (resultCode == RESULT_OK) {
                String name = data.getStringExtra("Name");
                Log.d("this just kicked in", "");
                // TODO: Implement successful register logic here
                // By default we just finish the Activity and log them in automatically
                //this.finish();
                HashMap<String, String> user = session.getRegistrationDetails();
                 _emailText.setText(user.get(SessionManager.KEY_EMAIL));
                 _passwordText.setText(user.get(SessionManager.KEY_PASSWORD));
                session.clearRegistrationDetails();
                login();
            }
        }
       else if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }
    // If login is successful call the LandingActivity class
    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        //TODO: save token in shared prefs
        //SharedPreferences sharedPref = getBaseContext().getSharedPreferences("com.pk.tagger.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE);
       // SharedPreferences.Editor editor = sharedPref.edit();
        //editor.putString(getString(R.string.access_token), token);
        //editor.commit();

        //Toast.makeText(getBaseContext(), "Login successful", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(getBaseContext(), MainActivity.class);


       // intent.putExtra("Username", name);
        startActivity(intent);
        //finish();
    }

    public void onLoginSuccessStormpath() {
        _loginButton.setEnabled(true);
        //TODO: save token in shared prefs
        //SharedPreferences sharedPref = getBaseContext().getSharedPreferences("com.pk.tagger.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE);
        // SharedPreferences.Editor editor = sharedPref.edit();
        //editor.putString(getString(R.string.access_token), token);
        //editor.commit();

        Log.d("Success Stormpath", "this worked");

        Stormpath.getUserProfile(new StormpathCallback<UserProfile>() {
            @Override
            public void onSuccess(UserProfile userProfile) {
                // user data ready

                Log.d("success", userProfile.getEmail());
                Log.d("success", Stormpath.accessToken());
                session.createLoginSession(userProfile.getEmail(), Stormpath.accessToken());

                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                // intent.putExtra("Username", name);
                startActivity(intent);
            }

            @Override
            public void onFailure(StormpathError error) {
                // something went wrong
                Log.d("success", "this didn't work");
            }
        });

        //Toast.makeText(getBaseContext(), "Login successful", Toast.LENGTH_LONG).show();
        //Intent intent = new Intent(getBaseContext(), MainActivity.class);


        // intent.putExtra("Username", name);
        //startActivity(intent);
        //finish();
    }

    public void onLoginFailed(ProgressDialog progressDialog) {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        _loginButton.setEnabled(true);
        progressDialog.hide();
        return;

    }
    // checks if fields match required rules
    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 8 || password.length() > 100 || password.equals(password.toLowerCase())
                || !password.matches(".*\\d+.*"))  {
            _passwordText.setError("Password must be at least 8 characters and contain at least 1 uppercase, 1 lowercase, and 1 number");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }

    @Override
    public void onSuccess(final LoginResult loginResult) {
        Log.d("something", "2");
        loginResult.getRecentlyDeniedPermissions();

        Stormpath.socialLogin(SocialProvidersResponse.FACEBOOK, loginResult.getAccessToken().getToken(), null,
                new StormpathCallback<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // we are logged in via fb!
                        Toast.makeText(LoginActivity.this, "Success! " + loginResult.getAccessToken().getToken(), Toast.LENGTH_LONG).show();
                        onLoginSuccessStormpath();
                    }

                    @Override
                    public void onFailure(StormpathError error) {
                        Toast.makeText(LoginActivity.this, error.message(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    public void onCancel() {
// fb login was cancelled ny the user
        Toast.makeText(LoginActivity.this, "Canceled!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onError(FacebookException error) {
        Log.d("something", "3");
        // an error occurred while logging in via fb
        Toast.makeText(LoginActivity.this, "Error!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    // [START signIn]
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    // [START handleSignInResult]
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d("Test ", "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            String authCode = acct.getServerAuthCode();
            Log.d("code", authCode);
            //mStatusTextView.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));
            //updateUI(true);

//            Stormpath.socialGoogleCodeAuth(SocialProvidersResponse.GOOGLE, new SocialProviderConfiguration(getString(R.string.goog_app_id), getString(R.string.goog_app_id)), new StormpathCallback<String>() {
//                @Override
//                public void onSuccess(String s) {
//                    Log.d("THis worked", s);
//                }
//
//                @Override
//                public void onFailure(StormpathError error) {
//
//                }
//            });

            Stormpath.socialLogin(SocialProvidersResponse.GOOGLE, null, authCode,
                    new StormpathCallback<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            onLoginSuccessStormpath();
                        }

                        @Override
                        public void onFailure(StormpathError error) {
                            Toast.makeText(getBaseContext(), "Log in failed", Toast.LENGTH_SHORT).show();
                        }
                    });

        } else {
            // Signed out, show unauthenticated UI.
            //updateUI(false);
        }
    }
}