package sarojbardewa.com.cookhookpro.loginandsplashscreens;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import sarojbardewa.com.cookhookpro.R;
import sarojbardewa.com.cookhookpro.mainrecipescreen.RecipeActivity;

public class SplashActivity extends AppCompatActivity implements Runnable, OnCompleteListener<AuthResult> {
    private static final double SplashScreenTime = 1.5; //Seconds
    private long mStartTimeMs;
    private boolean mLoginComplete, mLoginSuccessful;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mStartTimeMs = System.currentTimeMillis();

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
                profile.Login(email, password, this);
            }
            else { throw new Exception("Shouldn't get here, forcing re-login."); }
        }
        catch (Exception ex) {
            mLoginComplete = true;
        }
        finally {
            while ((!mLoginComplete) || (((double)(System.currentTimeMillis() - mStartTimeMs)) / 1000 < SplashScreenTime))
            {
                try {
                    Thread.sleep(100);
                } catch(InterruptedException ex) { } //Ignore exceptions
            }
            if(mLoginSuccessful)
            {
                //TODO: Launch main activity
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

    @Override
    public void onComplete(Task<AuthResult> task)
    {
        mLoginSuccessful = task.isSuccessful();
        mLoginComplete = true;
    }
}
