package com.example.teamlabdocsapp.app.api.TeamlabFolderRespose;

public class TeamlabResponseFileItem implements TeamlabResponseItem {

    public static final String SPREADSHEET = "xlsx";
    public static final String DOCUMENT = "docx";
    public static final String PRESENTATION = "pptx";

    public String id;
    public int folderId;
    public int version;
    public int versionGroup;
    public String contentLength;
    public int fileStatus;
    public String fileExst;
    public String title;
    public int access;
    public boolean sharedByMe;
    public String updatedBy;
    public String createdBy;
    public String updatedTime;
    public String createdTime;
    public String type;

    public TeamlabResponseFileItem(String id, int folderId, int version, int versionGroup,
                               String contentLength, int fileStatus, String fileExst, String title, int access,
                               boolean sharedByMe, String updatedBy, String createdBy, String updatedTime, String createdTime) {
        this.id = id;
        this.folderId = folderId;
        this.version = version;
        this.versionGroup = versionGroup;
        this.contentLength = contentLength;
        this.fileExst = fileExst;
        this.fileStatus = fileStatus;
        this.title = title;
        this.access = access;
        this.sharedByMe = sharedByMe;
        this.updatedBy = updatedBy;
        this.createdBy = createdBy;
        this.updatedTime = updatedTime;
        this.createdTime = createdTime;
        this.type = getType(title);
    }

    private String getType(String title) {
        String[] str = title.split("\\.");
        return str[str.length-1];
    }

    @Override
    public String getInfo(){
        return "file";
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
