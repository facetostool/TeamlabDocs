package com.example.teamlabdocsapp.app.Session;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.example.teamlabdocsapp.app.LoginActivity;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.HashMap;

public class SessionManager {

    SharedPreferences pref;

    Editor editor;

    Context _context;

    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "AndroidHivePref";

    private static final String IS_LOGIN = "IsLoggedIn";
    public static final String KEY_TOKEN = "token";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PORTAL = "portal";

    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();

    }

    public void createLoginSession(String email, String portal, String token){
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_TOKEN, token);
        editor.putString(KEY_PORTAL, portal);
        editor.commit();
    }

    public boolean checkLogin(){
        boolean isLoggedIn = this.isLoggedIn();
        if(!isLoggedIn){
            Intent i = new Intent(_context, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(i);
            ((Activity) _context).finish();
        }
        return isLoggedIn;
    }

    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
        user.put(KEY_PORTAL, pref.getString(KEY_PORTAL, null));
        user.put(KEY_TOKEN, pref.getString(KEY_TOKEN, null));
        return user;
    }

    public void logoutUser(){
        editor.clear();
        editor.commit();
        Intent i = new Intent(_context, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(i);
        ((Activity) _context).finish();
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }

}