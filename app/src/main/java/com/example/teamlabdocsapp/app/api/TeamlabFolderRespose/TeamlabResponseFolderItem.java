package com.example.teamlabdocsapp.app.api.TeamlabFolderRespose;

/**
 * Created by facetostool on 09.05.2014.
 */
public class TeamlabResponseFolderItem {
    public String id;
    public int parentId;
    public int fileCounts;
    public int foldersCounts;
    public boolean isShareable;
    public String title;
    public int access;
    public boolean sharedByMe;
    public String updatedBy;
    public String createdBy;
    public String updatedTime;
    public String createdTime;

    public TeamlabResponseFolderItem(String id, int parentId, int fileCounts, int foldersCounts,
                                 boolean isShareable, String title, int access,
                                 boolean sharedByMe, String updatedBy, String createdBy, String updatedTime, String createdTime) {
        this.id = id;
        this.parentId = parentId;
        this.fileCounts = fileCounts;
        this.foldersCounts = foldersCounts;
        this.isShareable = isShareable;
        this.title = title;
        this.access = access;
        this.sharedByMe = sharedByMe;
        this.updatedBy = updatedBy;
        this.createdBy = createdBy;
        this.updatedTime = updatedTime;
        this.createdTime = createdTime;
    }

    public String getInfo(){
        return "folder";
    }
}
