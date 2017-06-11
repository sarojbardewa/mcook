package sarojbardewa.com.cookhookpro.loginandsplashscreens;

import android.app.Activity;
import android.content.Intent;
import android.os.SystemClock;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Kyle on 6/10/2017.
 */

public class UserProfile {
    private static final UserProfile ourInstance = new UserProfile();
    private boolean mIsLoggedIn;
    private boolean mLoginComplete, mLoginError;
    private String mLoginErrorMessage;
    private String mUsername, mPassword;
    private FirebaseUser mFirebaseUser;

    //Firebase related authentication
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private UserProfile() {
        mIsLoggedIn = false;
        mAuth = FirebaseAuth.getInstance();
    }

    public static UserProfile getInstance() {
        return ourInstance;
    }

    public void Login(String username, String password) throws Exception
    {
        if(mIsLoggedIn) {
            throw new Exception("Already logged in");
        }

        //TODO: Code to login to firebase
        Task<AuthResult> task = mAuth.signInWithEmailAndPassword(username, password);
        long startTimeMs = SystemClock.currentThreadTimeMillis();
        long timeoutMs = 5000;
        while(!task.isComplete() && SystemClock.currentThreadTimeMillis() - startTimeMs < timeoutMs) {
            try { Thread.sleep(10); } catch (InterruptedException ex) { }
        }
        if(!task.isSuccessful())
        {
            if(task.getException() != null) {
                throw new Exception(task.getException().getMessage());
            } else {
                throw new Exception("Unknown account login problem");
            }
        }
        AuthResult result = task.getResult();
        mFirebaseUser = result.getUser();
        mUsername = username;
        mPassword = password;
        mIsLoggedIn = true;
    }

    public void CreateAccount(String username, String password) throws Exception
    {
        if(mIsLoggedIn) {
            throw new Exception("Already logged in");
        }

        Task<AuthResult> task = mAuth.createUserWithEmailAndPassword(username, password);
        long startTimeMs = SystemClock.currentThreadTimeMillis();
        long timeoutMs = 5000;
        while(!task.isComplete() && SystemClock.currentThreadTimeMillis() - startTimeMs < timeoutMs) {
            try { Thread.sleep(10); } catch (InterruptedException ex) { }
        }
        if(!task.isSuccessful())
        {
            if(task.getException() != null) {
                throw new Exception(task.getException().getMessage());
            } else {
                throw new Exception("Unknown account creation problem");
            }
        }
    }

    private void Logout()
    {
        //TODO: Code to log out of firebase (if needed)
        mAuth.signOut();
        mIsLoggedIn = false;
    }

    public void LogoutAndGoToLoginScreen(Activity activity)
    {
        Logout();
        SharedPreferenceHelper.getInstance(activity).DeleteCredentials();
        Intent temp = new Intent(activity, LoginActivity.class);
        activity.startActivity(temp);
        activity.finish();
    }
}
