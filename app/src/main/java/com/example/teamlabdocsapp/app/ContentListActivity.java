package com.example.teamlabdocsapp.app;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.teamlabdocsapp.app.Session.SessionManager;
import com.example.teamlabdocsapp.app.adapters.ContentAdapter;
import com.example.teamlabdocsapp.app.api.TeamlabAPI;
import com.example.teamlabdocsapp.app.api.TeamlabAPIDocuments;
import com.example.teamlabdocsapp.app.api.TeamlabRespose.TeamlabFolderResponse;
import com.example.teamlabdocsapp.app.api.TeamlabRespose.TeamlabResponseFolderItem;
import com.example.teamlabdocsapp.app.listnerers.OnDocumentsListener;

import java.util.HashMap;

public class ContentListActivity extends Activity implements OnDocumentsListener {
    TeamlabAPIDocuments documentsAPI;
    SessionManager session;
    ContentAdapter contentAdapter;
    ListView lvMain;

    private int mShortAnimationDuration = 200;

    public static final String FOLDER_ID = "folderId";
    public static final String TITLE = "title";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_activity);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        lvMain = (ListView) findViewById(R.id.contentList);

        Intent intent = getIntent();
        String folderId = intent.getStringExtra(FOLDER_ID);
        String title = intent.getStringExtra(TITLE);
        getActionBar().setTitle(title);
        session = new SessionManager(this);
        HashMap<String, String> user = session.getUserDetails();
        TeamlabAPI tmAPI = new TeamlabAPI(this, user.get(SessionManager.KEY_PORTAL));
        documentsAPI = tmAPI.documents(user.get(SessionManager.KEY_TOKEN));
        documentsAPI.setDocumentsListener(this);
        documentsAPI.openFolder(folderId);
    }

    @Override
    public void onDocumentsListener(TeamlabFolderResponse response) {
        Log.v("OPERATION", "GET RESPONSE");
        final ProgressBar mLoadingView = (ProgressBar) findViewById(R.id.loading_spinner);
        mLoadingView.animate()
                .alpha(0f)
                .setDuration(mShortAnimationDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mLoadingView.setVisibility(View.GONE);
                    }
                });
        if (response.getSize() == 0) {
            final LinearLayout mContentView = (LinearLayout) findViewById(R.id.emptyContent);
            mContentView.animate()
                    .alpha(1f)
                    .setDuration(mShortAnimationDuration)
                    .setListener(null);
        }

        contentAdapter = new ContentAdapter(this, response);
        lvMain.setAdapter(contentAdapter);
        lvMain.animate()
                .alpha(1f)
                .setDuration(mShortAnimationDuration)
                .setListener(null);

        lvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Object item = lvMain.getAdapter().getItem(position);
                if (item instanceof TeamlabResponseFolderItem) {
                    Intent i = new Intent(ContentListActivity.this, ContentListActivity.class);
                    i.putExtra(ContentListActivity.FOLDER_ID, ((TeamlabResponseFolderItem) item).id);
                    i.putExtra(ContentListActivity.TITLE, ((TeamlabResponseFolderItem) item).getTitle());
                    ContentListActivity.this.startActivity(i);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                }
            }
        });
        registerForContextMenu(lvMain);

        Log.v("OPERATION", "END");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case R.id.logout_settings:
                session.logoutUser();
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
        this.overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_right);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            reloadActivity();
        }
    }

    public void reloadActivity() {
        finish();
        startActivity(getIntent());
    }
}
