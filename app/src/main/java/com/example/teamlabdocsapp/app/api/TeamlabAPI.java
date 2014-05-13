package com.example.teamlabdocsapp.app.api;

import android.content.Context;

import com.example.teamlabdocsapp.app.listnerers.OnAuthListener;

public class TeamlabAPI {

    private String API_PATH = "/api/2.0";
    private String HTTPS = "https://";

    private String url;
    private Context context;

    public final static String REQUEST_TYPE = ".json";

    public TeamlabAPI(Context context, String portal) {
        this.context = context;
        if (portal.equals("1")) {
            portal = "vasyapupkinloh.teamlab.com";
        }
        this.url = createUrl(portal);
    }

    public TeamlabAPIAuth auth() {
        return new TeamlabAPIAuth(context, url);
    }

    public TeamlabAPIDocuments documents(String token) {
        return new TeamlabAPIDocuments(context, url, token);
    }

    private String createUrl(String portal){
        return HTTPS + portal + API_PATH;
    }

}