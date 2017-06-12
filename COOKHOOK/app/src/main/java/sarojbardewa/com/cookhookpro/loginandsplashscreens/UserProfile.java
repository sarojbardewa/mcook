package sarojbardewa.com.cookhookpro.loginandsplashscreens;

import android.app.Activity;
import android.content.Intent;
import android.os.SystemClock;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
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

    public void Login(final String username, final String password, final OnCompleteListener<AuthResult> listener) throws Exception
    {
        if(mIsLoggedIn) {
            throw new Exception("Already logged in");
        }

        //TODO: Code to login to firebase
        mAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    mIsLoggedIn = true;
                    mUsername = username;
                    mPassword = password;
                }
                listener.onComplete(task);
            }
        });


    }

    public void CreateAccount(final String username, final String password, final OnCompleteListener<AuthResult> listener) throws Exception
    {
        if(mIsLoggedIn) {
            throw new Exception("Already logged in");
        }

        mAuth.createUserWithEmailAndPassword(username, password).addOnCompleteListener(listener);
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
