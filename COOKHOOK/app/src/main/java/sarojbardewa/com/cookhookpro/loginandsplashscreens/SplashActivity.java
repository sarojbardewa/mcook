package sarojbardewa.com.cookhookpro.loginandsplashscreens;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import sarojbardewa.com.cookhookpro.R;
import sarojbardewa.com.cookhookpro.mainrecipescreen.RecipeActivity;

/**
 * @brief Splash screen for the CookHook app. This is the first screen with which the user interacts.
 *
 * It's purpose is to display the splash image, and to also log the user in if the user saved account credentials.
 * Depending on whether or not the user saved credentials, this activity will either launch the LoginActivity
 * or the RecipeActivity (main activity).
 */
public class SplashActivity extends AppCompatActivity implements Runnable, OnCompleteListener<AuthResult> {
    private static final double SplashScreenTime = 1.5; //Seconds
    private long mStartTimeMs;
    private boolean mLoginComplete, mLoginSuccessful;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mStartTimeMs = System.currentTimeMillis(); //Record the current time for a minimum wait time loop

        //mLoginComplete and mLoginSuccessful are state variables to indicate the result of logging in.
        mLoginComplete = false;
        mLoginSuccessful = false;
        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run(){
        SharedPreferenceHelper helper = SharedPreferenceHelper.getInstance(this);
        UserProfile profile = UserProfile.getInstance();
        profile.Logout();
        try
        {
            String email = helper.GetSavedEmail();
            String password = helper.GetSavedPassword();
            if(email != null && password != null)
            {
                //If there were saved credentials, attempt to login
                profile.Login(email, password, this);
            }
            else { throw new Exception("Shouldn't get here, forcing re-login."); }
        }
        catch (Exception ex) {
            mLoginComplete = true; //If anything goes wrong, the login attempt is complete (it failed).
        }
        finally {
            //This loops wait until a minimum of time has elapsed between launching the app and any login attempt is complete.
            while ((!mLoginComplete) || (((double)(System.currentTimeMillis() - mStartTimeMs)) / 1000 < SplashScreenTime))
            {
                try {
                    Thread.sleep(100);
                } catch(InterruptedException ex) { } //Ignore exceptions
            }
            if(mLoginSuccessful)
            {
                Intent temp = new Intent(getApplicationContext(), RecipeActivity.class);
                startActivity(temp);
                finish();
            }
            else
            {
                Intent temp = new Intent(this, LoginActivity.class);
                startActivity(temp);
                finish();
            }
        }
    }

    /**
     * @brief This is the callback registered when attempting to login. Once onComplete is called,
     * the status of the task is checked to determine if login was successful.
     *
     * @param task The result of the authentication task.
     */
    @Override
    public void onComplete(Task<AuthResult> task)
    {
        mLoginSuccessful = task.isSuccessful();
        mLoginComplete = true;
    }
}
