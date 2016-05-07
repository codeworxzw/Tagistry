package com.pk.tagger.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.pk.tagger.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Kieran on 26/01/2016.
 */
public class LoginActivity extends AppCompatActivity {

    //called on Login page
    SessionManager session;

    private static final String TAG = "LoginActivity";
    private static final int REQUEST_REGISTER = 0;
    private static final String LOGIN_URL = "http://52.31.31.106:9000/oauth/token";
    public static final String KEY_EMAIL = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_GRANT_TYPE = "grant_type";


    // temporary string to show the parsed response
    private String jsonResponse;

    //initialise views using ButterKnife
    @Bind(R.id.input_email) EditText _emailText;
    @Bind(R.id.input_password) EditText _passwordText;
    @Bind(R.id.btn_login) Button _loginButton;
    @Bind(R.id.link_register) TextView _registerLink;
    @Bind(R.id.link_skiplogin) TextView _skiploginLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        session = new SessionManager(getApplicationContext());

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
        if (requestCode == REQUEST_REGISTER) {
            if (resultCode == RESULT_OK) {
                String name = data.getStringExtra("Name");
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
}