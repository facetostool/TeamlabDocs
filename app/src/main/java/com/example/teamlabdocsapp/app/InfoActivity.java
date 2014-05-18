package com.example.teamlabdocsapp.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.teamlabdocsapp.app.Session.SessionManager;
import com.example.teamlabdocsapp.app.api.TeamlabAPI;
import com.example.teamlabdocsapp.app.api.TeamlabAPIDocuments;
import com.example.teamlabdocsapp.app.api.TeamlabRespose.TeamlabFolderResponse;
import com.example.teamlabdocsapp.app.listnerers.OnDocumentsListener;

import java.util.HashMap;

public class InfoActivity extends Activity implements OnDocumentsListener {

    public final static String ID = "itemId";
    TeamlabAPIDocuments documentsAPI;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_activity);

        Intent intent = getIntent();
        String itemId = intent.getStringExtra(ID);

        session = new SessionManager(this);
        HashMap<String, String> user = session.getUserDetails();
        TeamlabAPI tmAPI = new TeamlabAPI(this, user.get(SessionManager.KEY_PORTAL));
        documentsAPI = tmAPI.documents(user.get(SessionManager.KEY_TOKEN));
        documentsAPI.setDocumentsListener(this);
        documentsAPI.getFileInformation(itemId);
    }

    @Override
    public void onDocumentsListener(TeamlabFolderResponse response) {

    }

    public void closeInfo(View view) {
        finish();
    }
}
