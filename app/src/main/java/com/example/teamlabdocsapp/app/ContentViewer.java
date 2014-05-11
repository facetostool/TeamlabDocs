package com.example.teamlabdocsapp.app;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.teamlabdocsapp.app.Session.SessionManager;
import com.example.teamlabdocsapp.app.api.TeamlabAPI;
import com.example.teamlabdocsapp.app.api.TeamlabAPIDocuments;
import com.example.teamlabdocsapp.app.api.TeamlabFolderRespose.TeamlabFolderResponse;
import com.example.teamlabdocsapp.app.listnerers.OnDocumentsListener;

import java.util.HashMap;

public class ContentViewer extends Fragment implements OnDocumentsListener {

    Context context;
    TeamlabAPIDocuments documentsAPI;
    SessionManager session;
    ContentAdapter contentAdapter;
    View view;

    public static final String MY_DOCUMENTS = "My Documents";
    public static final String SHARED = "Shared with Me";
    public static final String COMMON = "Common Documents";
    public static final String TRASH = "Recycle Bin";

    public static final String ITEM_NAME = "itemName";

    public ContentViewer(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_layout, container,
                false);

//        ivIcon = (ImageView) view.findViewById(R.id.frag1_icon);
//        tvItemName = (TextView) view.findViewById(R.id.frag1_text);
//
        String selected = getArguments().getString(ITEM_NAME);
        Toast.makeText(context, selected, Toast.LENGTH_SHORT).show();
        session = new SessionManager(context);
        HashMap<String, String> user = session.getUserDetails();
        TeamlabAPI tmAPI = new TeamlabAPI(context, user.get(SessionManager.KEY_PORTAL));
        documentsAPI = tmAPI.documents(user.get(SessionManager.KEY_TOKEN));
        documentsAPI.setDocumentsListener(this);
        if (selected.equals(MY_DOCUMENTS)) {
            documentsAPI.myDocuments();
        } else if (selected.equals(SHARED)) {
            documentsAPI.shaderWithMe();
        } else if (selected.equals(COMMON)) {
            documentsAPI.common();
        } else if (selected.equals(TRASH)) {
            documentsAPI.recycleBin();
        }
        return view;
    }


    @Override
    public void onDocumentsListener(TeamlabFolderResponse response) {
        contentAdapter = new ContentAdapter(context, response);
        Log.v("RESPONSE", " " + response);
        // настраиваем список
        ListView lvMain = (ListView) view.findViewById(R.id.contentList);
        lvMain.setAdapter(contentAdapter);
    }
}