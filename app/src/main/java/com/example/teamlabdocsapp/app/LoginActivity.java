package com.example.teamlabdocsapp.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.teamlabdocsapp.app.Session.AlertDialogManager;
import com.example.teamlabdocsapp.app.Session.SessionManager;
import com.example.teamlabdocsapp.app.api.TeamlabAPI;
import com.example.teamlabdocsapp.app.api.TeamlabAPIAuth;
import com.example.teamlabdocsapp.app.listnerers.OnAuthListener;

public class LoginActivity extends Activity implements OnAuthListener {

    TextView tv;
    ProgressBar pb;

    EditText txtUsername, txtPassword, txtPortal;

    AlertDialogManager alert = new AlertDialogManager();

    SessionManager session;

    String email;
    String portal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        session = new SessionManager(getApplicationContext());

        txtUsername = (EditText) findViewById(R.id.txtUsername);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        txtPortal = (EditText) findViewById(R.id.txtPortal);
        tv = (TextView) findViewById(R.id.textProgress);
        pb = (ProgressBar) findViewById(R.id.progressBar);

    }

    public void logIn(View view) {
        email = txtUsername.getText().toString();
        portal = txtPortal.getText().toString();

        String password = txtPassword.getText().toString();

        if (email.trim().length() > 0 && password.trim().length() > 0) {
            TeamlabAPI tmAPI = new TeamlabAPI(getApplicationContext(), portal);
            TeamlabAPIAuth authAPI = tmAPI.auth();
            authAPI.setOnAuthListener(this);
            authAPI.auth(email, portal);
        } else {
            alert.showAlertDialog(LoginActivity.this, getString(R.string.login_fail),
                    getString(R.string.error_field_required), false);
        }

    }

    @Override
    public void onAuth(String token) {
        if (token != null) {
            session.createLoginSession(email, portal, token);
            Intent i = new Intent(this, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        } else {
            alert.showAlertDialog(LoginActivity.this, getString(R.string.login_fail),
                    getString(R.string.error_invalid), false);
            return;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}
