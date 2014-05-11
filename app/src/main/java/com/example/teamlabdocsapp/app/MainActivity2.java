package com.example.teamlabdocsapp.app;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.teamlabdocsapp.app.Session.SessionManager;
import com.example.teamlabdocsapp.app.api.TeamlabAPI;
import com.example.teamlabdocsapp.app.api.TeamlabAPIDocuments;
import com.example.teamlabdocsapp.app.api.TeamlabFolderRespose.TeamlabFolderResponse;
import com.example.teamlabdocsapp.app.listnerers.OnDocumentsListener;

import java.util.HashMap;

public class MainActivity2 extends ActionBarActivity implements OnDocumentsListener {

    TeamlabAPIDocuments documentsAPI;

    SessionManager session;

    ContentAdapter contentAdapter;

    Button btnLogout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        session = new SessionManager(this);
        //close from activity if user don't loggedIn
        if (!session.checkLogin())
            return;

        TextView lblEmail = (TextView) findViewById(R.id.lblEmail);

        btnLogout = (Button) findViewById(R.id.btnLogout);

        Toast.makeText(getApplicationContext(), "User Login Status: " + session.isLoggedIn(), Toast.LENGTH_LONG).show();

        HashMap<String, String> user = session.getUserDetails();

        TeamlabAPI tmAPI = new TeamlabAPI(this, user.get(SessionManager.KEY_PORTAL));
        documentsAPI = tmAPI.documents(user.get(SessionManager.KEY_TOKEN));
        documentsAPI.setDocumentsListener(this);
        documentsAPI.myDocuments();

        String email = user.get(SessionManager.KEY_EMAIL);

        lblEmail.setText(Html.fromHtml("Email: <b>" + email + "</b>"));

        btnLogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                session.logoutUser();
            }
        });

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onDocumentsListener(TeamlabFolderResponse response) {
        contentAdapter = new ContentAdapter(this, response);
        Log.v("RESPONSE", " " + response);
        // настраиваем список
        ListView lvMain = (ListView) findViewById(R.id.contentList);
        lvMain.setAdapter(contentAdapter);
    }
}
