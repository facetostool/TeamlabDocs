package com.example.teamlabdocsapp.app;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.teamlabdocsapp.app.Session.SessionManager;
import com.example.teamlabdocsapp.app.adapters.ContentAdapter;
import com.example.teamlabdocsapp.app.api.TeamlabAPI;
import com.example.teamlabdocsapp.app.api.TeamlabAPIDocuments;
import com.example.teamlabdocsapp.app.api.TeamlabFolderRespose.TeamlabFolderResponse;
import com.example.teamlabdocsapp.app.api.TeamlabFolderRespose.TeamlabResponseFileItem;
import com.example.teamlabdocsapp.app.api.TeamlabFolderRespose.TeamlabResponseFolderItem;
import com.example.teamlabdocsapp.app.api.TeamlabFolderRespose.TeamlabResponseItem;
import com.example.teamlabdocsapp.app.listnerers.OnDocumentsListener;

import java.util.HashMap;

public class ContentViewer extends Fragment implements OnDocumentsListener {

    Context context;
    TeamlabAPIDocuments documentsAPI;
    SessionManager session;
    ContentAdapter contentAdapter;
    View view;
    ListView lvMain;

    private int mShortAnimationDuration = 200;

    final int MENU_RENAME = 1;
    final int MENU_DELETE = 2;

    static final int MY_DOCUMENTS = 0;
    static final int SHARED = 1;
    static final int COMMON = 2;
    static final int TRASH = 3;

    public static final String ITEM_NAME = "itemName";

    public ContentViewer(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.v("OPERATION", "START");
        view = inflater.inflate(R.layout.fragment_layout, container,
                false);
        lvMain = (ListView) view.findViewById(R.id.contentList);

        int selected = getArguments().getInt(ITEM_NAME);
        session = new SessionManager(context);
        HashMap<String, String> user = session.getUserDetails();
        TeamlabAPI tmAPI = new TeamlabAPI(context, user.get(SessionManager.KEY_PORTAL));
        documentsAPI = tmAPI.documents(user.get(SessionManager.KEY_TOKEN));
        documentsAPI.setDocumentsListener(this);

        switch (selected) {
            case MY_DOCUMENTS:
                documentsAPI.myDocuments();
                ((Activity) context).setTitle(getString(R.string.menu_my_documents));
                break;
            case SHARED:
                documentsAPI.shaderWithMe();
                ((Activity) context).setTitle(getString(R.string.menu_shared));
                break;
            case COMMON:
                documentsAPI.common();
                ((Activity) context).setTitle(getString(R.string.menu_common));
                break;
            case TRASH:
                documentsAPI.recycleBin();
                ((Activity) context).setTitle(getString(R.string.menu_trash));
                break;
        }
        return view;
    }


    @Override
    public void onDocumentsListener(TeamlabFolderResponse response) {
        Log.v("OPERATION", "GET RESPONSE");
        final ProgressBar mLoadingView = (ProgressBar) getActivity().findViewById(R.id.loading_spinner);
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
            final LinearLayout mContentView = (LinearLayout) getActivity().findViewById(R.id.emptyContent);
            mContentView.animate()
                    .alpha(1f)
                    .setDuration(mShortAnimationDuration)
                    .setListener(null);
        }

        contentAdapter = new ContentAdapter(context, response);
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
                    Intent i=new Intent(context, ContentListActivity.class);
                    i.putExtra(ContentListActivity.FOLDER_ID, ((TeamlabResponseFolderItem) item).id);
                    ((Activity) context).startActivityForResult(i, 1);
                    ((Activity) context).overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_left);
//                    Fragment fragment = new ContentViewer(context);
//                    Bundle args = new Bundle();
//                    args.putInt(ContentViewer.ITEM_NAME, ContentViewer.OPEN_FOLDER);
//                    args.putString(ContentViewer.FOLDER_ID, ((TeamlabResponseFolderItem) item).id);
//                    fragment.setArguments(args);
//                    FragmentManager frgManager = getFragmentManager();
//                    frgManager.beginTransaction()
//                            .setCustomAnimations(R.animator.slide_in_left, R.animator.slide_in_right)
//                            .replace(R.id.content_frame, fragment, getString(R.string.fragment_tag))
//                            .addToBackStack("tag")
//                            .commit();
//                    ((Activity) context).setTitle(((TeamlabResponseFolderItem) item).title);
//                    ActionBar ab = getActivity().getActionBar();
//                    ab.setDisplayShowHomeEnabled(true);
                }
            }
        });
        registerForContextMenu(lvMain);

        Log.v("OPERATION", "END");
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        switch (v.getId()) {
            case R.id.contentList:
                menu.setHeaderTitle(R.string.context_title);
                menu.setHeaderIcon(android.R.drawable.ic_dialog_dialer);
                menu.add(0, MENU_DELETE, 0, R.string.context_delete_opt);
                menu.add(0, MENU_RENAME, 0, R.string.context_rename_opt);
                break;
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem menuItem) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuItem.getMenuInfo();
        Object item = lvMain.getAdapter().getItem(info.position);
        final TeamlabResponseItem selectedItem = (TeamlabResponseItem) item;
        switch (menuItem.getItemId()) {
            case MENU_DELETE:
                new AlertDialog.Builder(context)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle(R.string.confirm_delete_title)
                        .setMessage(getString(R.string.confirm_delete_message) + selectedItem.getTitle() + " ?")
                        .setPositiveButton(R.string.confirm_delete_yes, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (selectedItem instanceof TeamlabResponseFolderItem) {
                                    documentsAPI.deleteFolder(selectedItem.getId());
                                } else if (selectedItem instanceof TeamlabResponseFileItem) {
                                    documentsAPI.deleteFile(selectedItem.getId());
                                }
                                reloadFragment();
                            }

                        })
                        .setNegativeButton(R.string.confirm_delete_no, null)
                        .show();
                break;
            case MENU_RENAME:
                LayoutInflater factory = LayoutInflater.from(context);
                final View textEntryView = factory.inflate(R.layout.rename_dialog, null);
                new AlertDialog.Builder(context)
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setTitle(R.string.rename_title)
                        .setView(textEntryView)
                        .setPositiveButton(R.string.rename_ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                String newName = ((EditText) textEntryView.
                                        findViewById(R.id.editText)).getText().toString();
                                //TODO CHECK CLEAR FIELD
                                if (selectedItem instanceof TeamlabResponseFolderItem) {
                                    documentsAPI.renameFolder(selectedItem.getId(), newName);
                                } else if (selectedItem instanceof TeamlabResponseFileItem) {
                                    documentsAPI.renameFile(selectedItem.getId(), newName);
                                }
                                reloadFragment();
                            }
                        })
                        .setNegativeButton(R.string.rename_cancel, null)
                        .show();
                break;
        }
        return super.onContextItemSelected(menuItem);
    }

    private void reloadFragment() {
        Fragment currentFragment = getFragmentManager().findFragmentByTag(getString(R.string.fragment_tag));
        FragmentTransaction fragTransaction = getFragmentManager().beginTransaction();
        fragTransaction.detach(currentFragment);
        fragTransaction.attach(currentFragment);
        fragTransaction.commit();
    }
}