package com.example.teamlabdocsapp.app.listnerers;

import com.example.teamlabdocsapp.app.api.TeamlabFolderRespose.TeamlabFolderResponse;

public interface OnDocumentsListener {
    public void onDocumentsListener(TeamlabFolderResponse response);
}
