package com.example.teamlabdocsapp.app.api;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.teamlabdocsapp.app.api.TeamlabFolderRespose.TeamlabFolderResponse;
import com.example.teamlabdocsapp.app.listnerers.OnDocumentsListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TeamlabAPIDocuments {

    private RequestQueue mQueue;
    private String url;
    private String token;
    private String FILE_API_PATH = "/files";

    private OnDocumentsListener mListener;

    public static final String URL = "https://vasyapupkinloh.teamlab.com/API/2.0/AUTHENTICATION.json";

    public TeamlabAPIDocuments(Context context, String url, String token) {
        mQueue = Volley.newRequestQueue(context);
        this.url = url + FILE_API_PATH;
        this.token = token;
    }

    public void myDocuments() {
        getDocumentsRequest("/@my.json");
    }

    public void shaderWithMe() {
        getDocumentsRequest("/@share.json");
    }

    public void common() {
        getDocumentsRequest("/@common.json");
    }

    public void recycleBin() {
        getDocumentsRequest("/@trash.json");
    }

    private void getDocumentsRequest(String specialUrl){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url + specialUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                TeamlabFolderResponse responseObj = null;
                try {
                    responseObj = TeamlabFolderResponse.createTeamlabFolderResponse(response);
                } catch (JSONException e) {
                    Log.v("WRONG RESPONSE", response.toString());
                    e.printStackTrace();
                }
                mListener.onDocumentsListener(responseObj);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("Error", "Wrong request");
                Log.v("Error", error.getMessage());
                mListener.onDocumentsListener(null);
            }
        }) {
            @Override
            public Map<String,String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                Log.v("JSON new", " " + token);
                headers.put("AUTHORIZATION", token);
                return headers;
            }
        };
        mQueue.add(jsonObjectRequest);
    }

    public void setDocumentsListener(OnDocumentsListener listener) {
        mListener = listener;
    }

//    public Response openFolder(String folderId) {
//        HashMap<String,String> params =  new HashMap<String,String>();
//        return Request.sendRequest(Request.GET, this.apiURL + "/" + folderId, params, this.token);
//    }
//
//    public Response trash() {
//        HashMap<String,String> params =  new HashMap<String,String>();
//        return Request.sendRequest(Request.GET, this.apiURL + "/@trash", params, this.token);
//    }
//
//    public Response sharedWithMe() {
//        HashMap<String,String> params =  new HashMap<String,String>();
//        return Request.sendRequest(Request.GET, this.apiURL + "/@share", params, this.token);
//    }
//
//    public Response commonDocuments() {
//        HashMap<String,String> params =  new HashMap<String,String>();
//        return Request.sendRequest(Request.GET, this.apiURL + "/@common", params, this.token);
//    }
//
//    public Response createFolder(String parentId, String title) {
//        HashMap<String,String> params =  new HashMap<String,String>();
//        params.put("title", title);
//        return Request.sendRequest(Request.POST, this.apiURL + "/" + parentId, params, this.token);
//    }
//
//    public Response renameFolder(String parentId, String title) {
//        HashMap<String,String> params =  new HashMap<String,String>();
//        params.put("title", title);
//        return Request.sendRequest(Request.PUT, this.apiURL + "/" + parentId, params, this.token);
//    }
//
//    public Response renameFile(String fileId, String title) {
//        HashMap<String,String> params =  new HashMap<String,String>();
//        params.put("title", title);
//        return Request.sendRequest(Request.PUT, this.apiURL + "/file/" + fileId, params, this.token);
//    }

}
