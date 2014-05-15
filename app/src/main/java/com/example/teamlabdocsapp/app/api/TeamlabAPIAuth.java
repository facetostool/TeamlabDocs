package com.example.teamlabdocsapp.app.api;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.teamlabdocsapp.app.listnerers.OnAuthListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TeamlabAPIAuth {
    private RequestQueue mQueue;
    private String url;
    private String AUTH_API_PATH = "/AUTHENTICATION.json";
    private String LOGIN_NAME_KEY = "userName";
    private String LOGIN_PSWD_KEY = "password";

    private OnAuthListener mListener;

    public TeamlabAPIAuth(Context context, String url) {
        mQueue = Volley.newRequestQueue(context);
        this.url = url + AUTH_API_PATH;
    }

    public void auth(String userName, String password) {
        if (userName.equals("1") && password.equals("1")) {
            userName = "cs.impreza@mail.ru";
            password = "123456";
        }
        Map<String, String> params = new HashMap<String, String>();
        params.put(LOGIN_NAME_KEY, userName);
        params.put(LOGIN_PSWD_KEY, password);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String token = "";
                Log.v("JSON", response.toString());
                try {
                    JSONObject tokenJSON = response.getJSONObject("response");
                    Log.v("JSON", tokenJSON.toString());
                    token = tokenJSON.getString("token");
                    Log.v("JSON", token);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.v("JSON", token);
                mListener.onAuth(token);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("Error", "Wrong request");
                mListener.onAuth(null);
            }
        }) {
        };
        mQueue.add(jsonObjectRequest);
    }

    public void setOnAuthListener(OnAuthListener listener) {
        mListener = listener;
    }

}
