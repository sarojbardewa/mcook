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
import com.google.firebase.auth.UserProfileChangeRequest;

/**
 * Created by Kyle on 6/10/2017.
 */

/**
 * Helper class meant to deal with account creation, login, and account details. It's a singleton,
 * and a thin wrapper around the FirebaseAuth class.
 */
public class UserProfile {
    private static final UserProfile ourInstance = new UserProfile();
    private boolean mIsLoggedIn;
    private String mUsername, mPassword, mEmail;
    private FirebaseUser mFirebaseUser;

    //Firebase related authentication
    private FirebaseAuth mAuth;

    private UserProfile() {
        mIsLoggedIn = false;
        mAuth = FirebaseAuth.getInstance();
    }

    public static UserProfile getInstance() {
        return ourInstance;
    }

    public void Login(final String email, final String password, final OnCompleteListener<AuthResult> listener) throws Exception
    {
        if(mIsLoggedIn) {
            throw new Exception("Already logged in");
        }


        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

            /**
             * This callback intercepts the onComplete() callback to record information, then calls the listener passed to the function.
             *
             * @param task The result of the logging in task.
             */
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //This onComplete
                if(task.isSuccessful())
                {
                    mIsLoggedIn = true;
                    mEmail = email;
                    mUsername = mAuth.getCurrentUser().getDisplayName();
                    mPassword = password;
                }
                listener.onComplete(task);
            }
        });


    }

    public void CreateAccount(final String username, final String email, final String password, final OnCompleteListener<AuthResult> listener) throws Exception
    {
        if(mIsLoggedIn) {
            throw new Exception("Already logged in");
        }

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

            /**
             * This callback gets a little tricky. Since account creation and updating the profile name are two
             * different steps, there is a nested callback which, on successful login, will update the display name
             * of the Firebase user profile, then call the onCompleteListener passed to this function. If anything fails,
             * the listener passed to this function is called earlier.
             * @param task
             */
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                final Task<AuthResult> createAccountTask = task;
                if(task.isSuccessful())
                {
                    mAuth.getCurrentUser().updateProfile(new UserProfileChangeRequest.Builder().setDisplayName(username).build())
                            .addOnCompleteListener(new OnCompleteListener<Void>()
                    {
                        /**
                         * The nested listener finally calls the listener passed to the CreateAccount function.
                         * @param task
                         */
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            listener.onComplete(createAccountTask);
                        }
                    });
                }
                else
                {
                    /**
                     * On failure, just call the listener passed to CreateAccount() to notify the listener that the operation failed.
                     */
                    listener.onComplete(createAccountTask);
                }
            }
        });
    }

    public String GetUserName()
    {
        return mAuth.getCurrentUser().getDisplayName();
    }

    public String GetUid()
    {
        return mAuth.getCurrentUser().getUid();
    }

    public void Logout()
    {
        mAuth.signOut();
        mIsLoggedIn = false;
    }

    /**
     * Public helper function so that any activity may call this function to return to the login screen.
     * @param activity
     */
    public void LogoutAndGoToLoginScreen(Activity activity)
    {
        Logout();
        SharedPreferenceHelper.getInstance(activity).DeleteCredentials();
        Intent temp = new Intent(activity, LoginActivity.class);
        activity.startActivity(temp);
        activity.finish();
    }
}
