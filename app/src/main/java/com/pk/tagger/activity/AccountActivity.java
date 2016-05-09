package com.pk.tagger.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.pk.tagger.R;
import com.pk.tagger.managers.SessionManager;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by pk on 07/05/16.
 */
public class AccountActivity extends AppCompatActivity {

    // Session Manager Class
    SessionManager session;

    @Bind(R.id.btnLogout) Button _btnLogout;
    @Bind(R.id.lblEmail) TextView _lblEmail;
    @Bind(R.id.lblToken) TextView _lblToken;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        ButterKnife.bind(this);

        session = new SessionManager(getApplicationContext());
        session.checkLogin();


        HashMap<String, String> user = session.getUserDetails();

        // email
        String email = user.get(SessionManager.KEY_EMAIL);
        _lblEmail.setText(Html.fromHtml("Email: <b>" + email + "</b>"));
        // email
        String token = user.get(SessionManager.KEY_TOKEN);
        _lblToken.setText(Html.fromHtml("Token: <b>" + token + "</b>"));

        _btnLogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // Clear the session data
                // This will clear all session data and
                // redirect user to LoginActivity
                session.logoutUser();
            }
        });
    }





}
