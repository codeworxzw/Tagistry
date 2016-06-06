package com.pk.tagger.activity;

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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.pk.tagger.R;
import com.pk.tagger.managers.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Kieran on 26/01/2016.
 */
public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";
    private static final String REGISTER_URL = "https://gigitch.duckdns.org/register";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_FIRST_NAME = "firstname";
    public static final String KEY_SECOND_NAME = "secondname";
    public static final String KEY_PASSWORD = "password";

    // temporary string to show the parsed response
    private String jsonResponse;

    //@Bind(R.id.input_name) EditText _nameText;
    @Bind(R.id.input_email) EditText _emailText;
    @Bind(R.id.input_password) EditText _passwordText;
    @Bind(R.id.input_password_confirm) EditText _passwordconfirmText;
    @Bind(R.id.btn_register) Button _registerButton;
    @Bind(R.id.link_login) TextView _loginLink;
    @Bind(R.id.link_skiplogin) TextView _skiploginLink;

    SessionManager session;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        session = new SessionManager(getApplicationContext());
        //intialise buttons below
        _registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
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
    // register method
    public void register() {
        Log.d(TAG, "Register");
        // check fields in validate method
        if (!validate()) {
            onRegisterFailed();
            return;
        }

        _registerButton.setEnabled(false);

//        final ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this,
//                R.style.AppTheme);
//
//        progressDialog.setMessage("Creating Account...");
//        progressDialog.show();
//
//        Runnable progressRunnable = new Runnable() {
//
//            @Override
//            public void run() {
//                progressDialog.cancel();
//            }
//        };
//
//        Handler pdCanceller = new Handler();
//        pdCanceller.postDelayed(progressRunnable, 3000);

        // get fields and TODO: insert into HTTP POST request below
        final String email = _emailText.getText().toString();
        final String password = _passwordText.getText().toString();

        // Create jsonobject of user to send in the POST request
        JSONObject account = new JSONObject();
        try {
            account.put(KEY_EMAIL, email);
            account.put(KEY_PASSWORD, password);
        } catch(JSONException e) {
            e.printStackTrace();
        }

        Log.d(TAG, account.toString());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, REGISTER_URL, account,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "Valid Response: " + response.toString());

                        try {
                            // Parsing json object response
                            jsonResponse = "";
                            String email = response.getString("email");
                            jsonResponse += "User registered: " + email + "\n\n";
                            session.createRegisterSession(email, password);
                            Log.d(TAG, jsonResponse);
                            onRegisterSuccess();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            onRegisterFailed();
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "Error Response: " + error.toString());

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
                        onRegisterFailed();
                    }
                }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Accept", "application/json");
                return headers;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
        Log.d(TAG, "POSTing: " + jsonObjectRequest.toString());

//        new android.os.Handler().postDelayed(
//                new Runnable() {
//                    public void run() {
//                        // On complete call either onRegisterSuccess or onRegisterFailed
//                        // depending on success
//                        //onRegisterSuccess();
//                        //onRegisterFailed();
//                        progressDialog.dismiss();
//                    }
//                }, 3000);
    };
    // finish() closes register activity and calls LoginActivity and puts name data into intent
    public void onRegisterSuccess() {
        _registerButton.setEnabled(true);
        //String name = _nameText.getText().toString();
        Toast.makeText(getBaseContext(), "Registration succeeded", Toast.LENGTH_LONG).show();
        Intent resultIntent = new Intent();
        // TODO Add extras or a data URI to this intent as appropriate.

        setResult(RESULT_OK, resultIntent);
        finish();
    }

    public void onRegisterFailed() {
        Toast.makeText(getBaseContext(), "Registration failed", Toast.LENGTH_LONG).show();
        Log.d(TAG, "Registration failed");
        _registerButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        //String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        String confirmpassword = _passwordconfirmText.getText().toString();

//        if (name.isEmpty() || name.length() < 3) {
//            _nameText.setError("at least 3 characters");
//            valid = false;
//        } else {
//            _nameText.setError(null);
//        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("Please enter a valid email address");
            Log.d(TAG, "Invalid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        //TODO: add error catching for uppercase, lowercase, and numerical character requirements
        if (password.isEmpty() || password.length() < 8 || password.length() > 10 || password.equals(password.toLowerCase())
                || !password.matches(".*\\d+.*")) {
            _passwordText.setError("Password must be at least 8 characters and contain at least 1 uppercase, 1 lowercase, and 1 number");
            Log.d(TAG, "Invalid password");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        if (!confirmpassword.equals(password)) {
            _passwordconfirmText.setError("Passwords do not match");
            Log.d(TAG, "Passwords do not match");
            valid = false;
        } else {
            _passwordconfirmText.setError(null);
        }
        return valid;
    }
}
