package com.example.teamlabdocsapp.app.api.TeamlabRespose;

import com.example.teamlabdocsapp.app.api.helpers.TimeConvertHelper;

public class TeamlabResponseFolderItem implements TeamlabResponseItem{
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
        this.updatedTime = TimeConvertHelper.convertTime(updatedTime);
        this.createdTime = TimeConvertHelper.convertTime(createdTime);
    }

    @Override
    public String getInfo(){
        String info = "";
//        if (updatedBy == null) {
        info = createdBy + " | Created: " + createdTime + " | " + "Folders: " + foldersCounts
        + " | " + "Files: " + fileCounts;
//        } else {
//            info = updatedBy + " | Обновлён: " + updatedTime + " | " + contentLength;
//        }
        return info;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getTitle() {
        return title;
    }
}
