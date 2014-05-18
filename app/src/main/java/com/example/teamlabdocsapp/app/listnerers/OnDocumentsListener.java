package com.example.teamlabdocsapp.app.listnerers;

import com.example.teamlabdocsapp.app.api.TeamlabRespose.TeamlabFolderResponse;

public interface OnDocumentsListener {
    public void onDocumentsListener(TeamlabFolderResponse response);
}
