package com.harmit.mytubeapp;

import android.app.Activity;
import android.os.AsyncTask;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;

import java.io.IOException;

/**
 * Created by Harmit Patel on 10/3/15.
 */
public class AccessTokenUtil extends AsyncTask {

    GoogleConnectionUtilProtocol delegate;

    public interface  GoogleConnectionUtilProtocol {

        public void didGenerateAccessToken(String accessToken);
        public void didCatchException(Exception exc);
        public Activity getActivity();
    }


    private final static String YOUTUBE_API_SCOPE
            = "https://www.googleapis.com/auth/youtube";
    private final static String mScopes
            = "oauth2:" + YOUTUBE_API_SCOPE;

    String mScope;
    String mEmail;

    public AccessTokenUtil(Activity activity, String name, String scope) {

        this.mScope = scope;
        this.mEmail = name;
        delegate = (GoogleConnectionUtilProtocol) activity;
    }

    private String fetchToken() throws IOException {

        try {

            return GoogleAuthUtil.getToken(delegate.getActivity(), mEmail, mScopes);
        } catch (UserRecoverableAuthException userRecoverableException) {

            delegate.didCatchException(userRecoverableException);
        } catch (GoogleAuthException fatalException) {

            delegate.didCatchException(fatalException);
        }
        return null;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        try {
            String accessToken = fetchToken();
            if (accessToken != null) {

                System.out.println("Access Token "+accessToken);
                delegate.didGenerateAccessToken(accessToken);
            }
        } catch (IOException e) {

        }
        return null;
    }
}