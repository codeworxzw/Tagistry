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

import com.pk.tagger.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Kieran on 26/01/2016.
 */
public class LoginActivity extends AppCompatActivity {

    //called on Login page

    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    private static final String LOGIN_URL = "52.31.31.106:9000/oauth/token";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";

    //initialise views using ButterKnife

    @Bind(R.id.input_email)
    EditText _emailText;
    @Bind(R.id.input_password)
    EditText _passwordText;
    @Bind(R.id.btn_login)
    Button _loginButton;
    @Bind(R.id.link_signup)
    TextView _signupLink;
    @Bind(R.id.link_skiplogin)
    TextView _skiploginLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        //initialise login button below
        _loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        //intialise Register link below
        _signupLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Start register activity
                Toast.makeText(getApplicationContext(), "Signup is selected!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
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
        Toast.makeText(getApplicationContext(), "Will make login POST request soon!", Toast.LENGTH_SHORT).show();
        if (!validate()) {
            onLoginFailed();
            return;

        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        // get fields and TODO: insert into HTTP POST request below
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        // TODO: Implement your own authentication logic here. (make HTTP POST request to <server>/oauth/token)
        // JSON {"username":"bob@email.com", "password":"hunter2"}

    }
    //retrieves the intent from RegisterActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                String name = data.getStringExtra("Name");
                // TODO: Implement successful register logic here
                // By default we just finish the Activity and log them in automatically
                //this.finish();
                onLoginSuccess(name);
            }
        }
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }
    // If login is successful call the LandingActivity class
    public void onLoginSuccess(String name) {
        _loginButton.setEnabled(true);
        //Toast.makeText(getBaseContext(), "Login successful", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(getBaseContext(), MainActivity.class);

        intent.putExtra("Username", name);

        startActivity(intent);
        //finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
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

        if (password.isEmpty() || password.length() < 8 || password.length() > 10) {
            _passwordText.setError("between 8 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
}