package com.example.teamlabdocsapp.app;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.teamlabdocsapp.app.Session.SessionManager;
import com.example.teamlabdocsapp.app.api.TeamlabAPI;
import com.example.teamlabdocsapp.app.api.TeamlabAPIDocuments;
import com.example.teamlabdocsapp.app.api.TeamlabRespose.TeamlabResponseFileItem;
import com.example.teamlabdocsapp.app.api.TeamlabRespose.TeamlabResponseFolderItem;
import com.example.teamlabdocsapp.app.api.TeamlabRespose.TeamlabResponseItem;
import com.example.teamlabdocsapp.app.listnerers.OnInfoListener;

import java.util.HashMap;

public class InfoActivity extends Activity implements OnInfoListener {

    public final static String ID = "itemId";
    TeamlabAPIDocuments documentsAPI;
    SessionManager session;

    public final static int FILE_INT = 0;
    public final static int FOLDER_INT = 1;
    public final static String ITEM_TYPE = "item_type";

    TextView itemTitle;
    TextView itemDesc;

    private int mShortAnimationDuration = 200;

    ProgressBar mLoadingView;
    LinearLayout mainInfo;
    LinearLayout deleteItem;
    LinearLayout renameItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_activity);

        mainInfo = (LinearLayout) findViewById(R.id.mainInfo);
        itemTitle = (TextView) findViewById(R.id.itemTitle);
        itemDesc = (TextView) findViewById(R.id.itemDesc);
        mLoadingView = (ProgressBar) findViewById((R.id.loading_spinner));
        deleteItem = (LinearLayout) findViewById(R.id.deleteItem);
        renameItem = (LinearLayout) findViewById(R.id.renameItem);

        Intent intent = getIntent();
        String itemId = intent.getStringExtra(ID);
        int itemType = intent.getIntExtra(ITEM_TYPE, -1);

        session = new SessionManager(this);
        HashMap<String, String> user = session.getUserDetails();
        TeamlabAPI tmAPI = new TeamlabAPI(this, user.get(SessionManager.KEY_PORTAL));
        documentsAPI = tmAPI.documents(user.get(SessionManager.KEY_TOKEN));
        documentsAPI.setInfoListener(this);
        documentsAPI.getItemInformation(itemType, itemId);
    }

    public void closeInfo(View view) {
        finish();
    }

    @Override
    public void onInfoListener(final TeamlabResponseItem responseItem) {
        mLoadingView.animate()
                .alpha(0f)
                .setDuration(mShortAnimationDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mLoadingView.setVisibility(View.GONE);
                    }
                });

        mainInfo.animate()
                .alpha(1f)
                .setDuration(mShortAnimationDuration)
                .setListener(null);

        deleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteItem(responseItem);
            }
        });

        renameItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                renameItem(responseItem);
            }
        });

        itemTitle.setText(responseItem.getTitle());
        itemDesc.setText("Just item");
    }

    public void renameItem(final TeamlabResponseItem item) {
        LayoutInflater factory = LayoutInflater.from(this);
        final View textEntryView = factory.inflate(R.layout.rename_dialog, null);
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setTitle(R.string.rename_title)
                .setView(textEntryView)
                .setPositiveButton(R.string.rename_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String newName = ((EditText) textEntryView.
                                findViewById(R.id.editText)).getText().toString();
                        //TODO CHECK CLEAR FIELD
                        if (item instanceof TeamlabResponseFolderItem) {
                            documentsAPI.renameFolder(item.getId(), newName);
                        } else if (item instanceof TeamlabResponseFileItem) {
                            documentsAPI.renameFile(item.getId(), newName);
                        }
                        String[] str = itemTitle.getText().toString().split("\\.");
                        String type = str[str.length-1];
                        itemTitle.setText(newName + "." + type);
                        setResult(RESULT_OK);
                    }
                })
                .setNegativeButton(R.string.rename_cancel, null)
                .show();
    }

    public void deleteItem(final TeamlabResponseItem item) {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(R.string.confirm_delete_title)
                .setMessage(getString(R.string.confirm_delete_message) + item.getTitle() + " ?")
                .setPositiveButton(R.string.confirm_delete_yes, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (item instanceof TeamlabResponseFolderItem) {
                            documentsAPI.deleteFolder(item.getId());
                        } else if (item instanceof TeamlabResponseFileItem) {
                            documentsAPI.deleteFile(item.getId());
                        }
                        setResult(RESULT_OK);
                        finish();
                    }

                })
                .setNegativeButton(R.string.confirm_delete_no, null)
                .show();
    }

}
