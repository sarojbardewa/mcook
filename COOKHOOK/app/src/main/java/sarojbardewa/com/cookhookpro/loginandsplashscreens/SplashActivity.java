package sarojbardewa.com.cookhookpro.loginandsplashscreens;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import sarojbardewa.com.cookhookpro.R;
import sarojbardewa.com.cookhookpro.mainrecipescreen.RecipeActivity;

public class SplashActivity extends AppCompatActivity implements Runnable{
    private static final double SplashScreenTime = 1.0; //Seconds
    private long mStartTimeMs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mStartTimeMs = System.currentTimeMillis();

        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run(){
        boolean loginSuccessful = false;
        SharedPreferenceHelper helper = SharedPreferenceHelper.getInstance(this);
        try
        {
            String userName = helper.GetSavedUserName();
            String password = helper.GetSavedPassword();
            if(userName != null && password != null)
            {
                UserProfile profile = UserProfile.getInstance();
                profile.Login(userName, password);
                loginSuccessful = true;
            }
        }
        catch (Exception ex) { }
        finally {
            while (((double)(System.currentTimeMillis() - mStartTimeMs)) / 1000 < SplashScreenTime)
            {
                try {
                    Thread.sleep(10);
                } catch(InterruptedException ex) { } //Ignore exceptions
            }
            if(loginSuccessful)
            {
                //TODO: Launch main activity
                Intent temp = new Intent(getApplicationContext(), RecipeActivity.class);
                startActivity(temp);
                finish();
            }
            else
            {
                Intent temp = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(temp);
                finish();
            }
        }
    }
}
