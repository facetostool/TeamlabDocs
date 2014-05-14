package com.example.teamlabdocsapp.app;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.teamlabdocsapp.app.Session.SessionManager;
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

    final int MENU_RENAME = 1;
    final int MENU_DELETE = 2;

    public static final String OPEN_FOLDER = "folder";
    public static final String FOLDER_ID = "folderId";

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
        lvMain  = (ListView) view.findViewById(R.id.contentList);

        String selected = getArguments().getString(ITEM_NAME);
        Toast.makeText(context, selected, Toast.LENGTH_SHORT).show();
        session = new SessionManager(context);
        HashMap<String, String> user = session.getUserDetails();
        TeamlabAPI tmAPI = new TeamlabAPI(context, user.get(SessionManager.KEY_PORTAL));
        documentsAPI = tmAPI.documents(user.get(SessionManager.KEY_TOKEN));
        documentsAPI.setDocumentsListener(this);
        if (selected.equals(OPEN_FOLDER)) {
            String folderId = getArguments().getString(FOLDER_ID);
            documentsAPI.openFolder(folderId);
        } else {
            if (selected.equals(MY_DOCUMENTS)) {
                documentsAPI.myDocuments();
            } else if (selected.equals(SHARED)) {
                documentsAPI.shaderWithMe();
            } else if (selected.equals(COMMON)) {
                documentsAPI.common();
            } else if (selected.equals(TRASH)) {
                documentsAPI.recycleBin();
            }
            getActivity().setTitle(selected);
        }
        return view;
    }


    @Override
    public void onDocumentsListener(TeamlabFolderResponse response) {
        contentAdapter = new ContentAdapter(context, response);
        Log.v("RESPONSE", " " + response);
        // настраиваем список
        lvMain.setAdapter(contentAdapter);

        lvMain.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Object item = lvMain.getAdapter().getItem(position);
                if (item instanceof TeamlabResponseFolderItem) {
                    TeamlabResponseFolderItem folder = (TeamlabResponseFolderItem) item;
                    Toast.makeText(getActivity(), "Stop Clicking me: folder", Toast.LENGTH_SHORT).show();
                    Fragment fragment = new ContentViewer(getActivity());
                    Bundle args = new Bundle();
                    args.putString(ContentViewer.ITEM_NAME, ContentViewer.OPEN_FOLDER);
                    args.putString(ContentViewer.FOLDER_ID, folder.id);
                    fragment.setArguments(args);
                    FragmentManager frgManager = getFragmentManager();
                    frgManager.beginTransaction().replace(R.id.content_frame, fragment, "TAG").addToBackStack("tag")
                            .commit();
                    getActivity().setTitle(folder.title);
                }   else {
                    Toast.makeText(getActivity(), "Stop Clicking me: file", Toast.LENGTH_SHORT).show();
                }
            }
        });

        registerForContextMenu(lvMain);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        switch (v.getId()) {
            case R.id.contentList:
                menu.add(0, MENU_DELETE, 0, "Delete");
                menu.add(0, MENU_RENAME, 0, "Rename");
                break;
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem menuItem) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuItem.getMenuInfo();
        Object item = lvMain.getAdapter().getItem(info.position);
        final TeamlabResponseItem selectedItem = (TeamlabResponseItem) item;
        switch (menuItem.getItemId()) {
            case MENU_DELETE:
                new AlertDialog.Builder(getActivity())
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Delete confirm")
                        .setMessage("Are you really want to delete " + selectedItem.getTitle() + " ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (selectedItem instanceof TeamlabResponseFolderItem) {
                                    documentsAPI.deleteFolder(selectedItem.getId());
                                } else if(selectedItem instanceof TeamlabResponseFileItem) {
                                    documentsAPI.deleteFile(selectedItem.getId());
                                }
                                Fragment currentFragment = getFragmentManager().findFragmentByTag("TAG");
                                FragmentTransaction fragTransaction = getFragmentManager().beginTransaction();
                                fragTransaction.detach(currentFragment);
                                fragTransaction.attach(currentFragment);
                                fragTransaction.commit();
                            }

                        })
                        .setNegativeButton("No", null)
                        .show();

                Toast.makeText(getActivity(), "Delete " + selectedItem.getTitle(), Toast.LENGTH_SHORT).show();
                break;
            case MENU_RENAME:
                LayoutInflater factory = LayoutInflater.from(getActivity());
                final View textEntryView = factory.inflate(R.layout.alert_dialog_text_entry, null);
                new AlertDialog.Builder(getActivity())
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Rename")
                        .setView(textEntryView)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                String newName = ((EditText) textEntryView.
                                        findViewById(R.id.editText)).getText().toString();
                                //TODO CHECK CLEAR FIELD
                                if (selectedItem instanceof TeamlabResponseFolderItem) {
                                    documentsAPI.renameFolder(selectedItem.getId(), newName);
                                } else if (selectedItem instanceof TeamlabResponseFileItem) {
                                    documentsAPI.renameFile(selectedItem.getId(), newName);
                                }
                                Fragment currentFragment = getFragmentManager().findFragmentByTag("TAG");
                                FragmentTransaction fragTransaction = getFragmentManager().beginTransaction();
                                fragTransaction.detach(currentFragment);
                                fragTransaction.attach(currentFragment);
                                fragTransaction.commit();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
                Toast.makeText(getActivity(), "Rename " + selectedItem.getTitle(), Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onContextItemSelected(menuItem);
    }
}