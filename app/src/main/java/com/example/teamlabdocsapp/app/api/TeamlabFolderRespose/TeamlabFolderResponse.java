package com.example.teamlabdocsapp.app.api.TeamlabFolderRespose;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by facetostool on 09.05.2014.
 */
public class TeamlabFolderResponse {
    public String id;
    public String parentId;
    public String title;
    public ArrayList<TeamlabResponseFolderItem> folders;
    public ArrayList<TeamlabResponseFileItem> files;

    public TeamlabFolderResponse(String id, String parentId, String title,
                                 ArrayList<TeamlabResponseFolderItem> folders,
                                 ArrayList<TeamlabResponseFileItem> files) {
        this.id = id;
        this.parentId = parentId;
        this.title = title;
        this.folders = folders;
        this.files = files;
    }

    public static TeamlabFolderResponse createTeamlabFolderResponse(JSONObject response) throws JSONException {
        JSONObject resposeJSON = response.getJSONObject("response");
        JSONObject current = resposeJSON.getJSONObject("current");
        String id = current.getString("id");
        String parentId = current.getString("parentId");
        String title = current.getString("title");

        ArrayList<TeamlabResponseFolderItem> folders;
        try {
            folders  = parseFolders(resposeJSON.getJSONArray("folders"));
        } catch (JSONException e) {
            folders = new ArrayList<TeamlabResponseFolderItem>();
        }
        ArrayList<TeamlabResponseFileItem> files = null;
        try {
            files = parseFiles(resposeJSON.getJSONArray("files"));
        } catch (JSONException e) {
            files = new ArrayList<TeamlabResponseFileItem>();
        }
        return new TeamlabFolderResponse(id, parentId, title, folders, files);
    }

    private static ArrayList<TeamlabResponseFolderItem> parseFolders(JSONArray foldersJSON) throws JSONException {
        ArrayList<TeamlabResponseFolderItem> folders = new ArrayList<TeamlabResponseFolderItem>();
        for(int i = 0; i < foldersJSON.length(); i++) {
            JSONObject folderJSON = foldersJSON.getJSONObject(i);

            String id = folderJSON.getString("id");
            int parentId = Integer.parseInt(folderJSON.getString("parentId"));
            int filesCount = Integer.parseInt(folderJSON.getString("filesCount"));
            int foldersCount = Integer.parseInt(folderJSON.getString("foldersCount"));
            boolean isShareable = Boolean.parseBoolean(folderJSON.getString("isShareable"));
            String title = folderJSON.getString("title");
            int access = Integer.parseInt(folderJSON.getString("access"));
            boolean sharedByMe = Boolean.parseBoolean(folderJSON.getString("sharedByMe"));
            String updatedTime = folderJSON.getString("updated");
            String createdTime = folderJSON.getString("created");
            String updatedBy;
            try {
                JSONObject updatedByJSON = folderJSON.getJSONObject("updatedBy");
                updatedBy = updatedByJSON.getString("displayName");
            } catch (JSONException e) {
                updatedBy = null;
            }
            JSONObject createdByJSON = folderJSON.getJSONObject("createdBy");
            String createdBy = createdByJSON.getString("displayName");

            TeamlabResponseFolderItem folder = new TeamlabResponseFolderItem(id, parentId, filesCount, foldersCount,
                    isShareable, title, access, sharedByMe, updatedBy, createdBy, updatedTime, createdTime);
            folders.add(folder);
        }
        return folders;
    }

    private static ArrayList<TeamlabResponseFileItem> parseFiles(JSONArray filesJSON) throws JSONException {
        ArrayList<TeamlabResponseFileItem> files = new ArrayList<TeamlabResponseFileItem>();
        for (int i = 0; i < filesJSON.length(); i++) {
            JSONObject fileJSON = filesJSON.getJSONObject(i);

            String id = fileJSON.getString("id");
            int folderId = Integer.parseInt(fileJSON.getString("folderId"));
            int version = Integer.parseInt(fileJSON.getString("version"));
            int versionGroup = Integer.parseInt(fileJSON.getString("versionGroup"));
            String contentLength = fileJSON.getString("contentLength");
            int fileStatus = Integer.parseInt(fileJSON.getString("fileStatus"));
            String fileExst = fileJSON.getString("fileExst");
            String title = fileJSON.getString("title");
            int access = Integer.parseInt(fileJSON.getString("access"));
            boolean sharedByMe = Boolean.parseBoolean(fileJSON.getString("sharedByMe"));
            String updatedTime = fileJSON.getString("updated");
            String createdTime = fileJSON.getString("created");
            String updatedBy;
            try {
                JSONObject updatedByJSON = fileJSON.getJSONObject("updatedBy");
                updatedBy = updatedByJSON.getString("displayName");
            } catch (JSONException e) {
                updatedBy = null;
            }
            JSONObject createdByJSON = fileJSON.getJSONObject("createdBy");
            String createdBy = createdByJSON.getString("displayName");

            TeamlabResponseFileItem file = new TeamlabResponseFileItem(id, folderId, version, versionGroup,
                    contentLength, fileStatus, fileExst, title, access, sharedByMe, updatedBy, createdBy,
                    updatedTime, createdTime);
            files.add(file);
        }
        return files;
    }
}
