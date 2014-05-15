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
    private String FILE_API_PATH = "/files/";

    private OnDocumentsListener mListener;

    public TeamlabAPIDocuments(Context context, String url, String token) {
        mQueue = Volley.newRequestQueue(context);
        this.url = url + FILE_API_PATH;
        this.token = token;
    }

    public void myDocuments() {
        Log.v("OPERATION", "CREATE REQUEST");
        getDocumentsRequest("@my");
    }

    public void shaderWithMe() {
        getDocumentsRequest("@share");
    }

    public void common() {
        getDocumentsRequest("@common");
    }

    public void recycleBin() {
        getDocumentsRequest("@trash");
    }

    public void openFolder(String folderId) {
        getDocumentsRequest(folderId);
    }

    private void getDocumentsRequest(String specialUrl){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                url + specialUrl + TeamlabAPI.REQUEST_TYPE, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.v("OPERATION", "GET JSON RESPONSE");
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
                Log.v("Error", error.getMessage());
                mListener.onDocumentsListener(null);
            }
        }) {
            @Override
            public Map<String,String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                Log.v("JSON new", " " + token);
                Log.v("OPERATION", "SHOW JSON");
                headers.put("AUTHORIZATION", token);
                return headers;
            }
        };
        Log.v("OPERATION", "ADD REQUEST IN QUEUE");
        mQueue.add(jsonObjectRequest);
    }

    public void deleteFolder(String folderId) {
        deleteRequest("folder/" + folderId);
        //TODO check access for delete
    }

    public void deleteFile(String fileId) {
        deleteRequest("file/" + fileId);
        //TODO check access for delete
    }

    private void deleteRequest(String specialUrl){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE,
                url + specialUrl + TeamlabAPI.REQUEST_TYPE, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.v("Response", response.toString());
 //               mListener.onDocumentsListener(responseObj);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("Error", error.getMessage());
//                mListener.onDocumentsListener(null);
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

    public void renameFolder(String folderId, String newTitle) {
        renameRequest(folderId, newTitle);
    }

    public void renameFile(String fileId, String newTitle) {
        renameRequest("file/" + fileId, newTitle);
    }

    private void renameRequest(String specialUrl, String newTitle){
        Map<String, String> params = new HashMap<String, String>();
        params.put("title", newTitle);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT,
                url + specialUrl + TeamlabAPI.REQUEST_TYPE, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.v("Response", response.toString());
                //               mListener.onDocumentsListener(responseObj);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("Error", error.getMessage());
//                mListener.onDocumentsListener(null);
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
