package sarojbardewa.com.cookhookpro.loginandsplashscreens;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Kyle on 6/10/2017.
 */

/**
 * Class to help store and retrieve data from SharedPreferences. Implemented as a singleton.
 */
public class SharedPreferenceHelper {
    private static final SharedPreferenceHelper ourInstance = new SharedPreferenceHelper();
    private Activity mActivity;
    private SharedPreferences mSharedPreferences;
    private static final String EmailKey = "edu.pdx.mcecs.ece558sp17.group3.loginscreen.email_sp_key";
    private static final String PasswordKey = "edu.pdx.mcecs.ece558sp17.group3.loginscreen.password_sp_key";
    private static final String AreCredentialsRemembered = "edu.pdx.mcecs.ece558sp17.group3.loginscreen.credentials_remembered";

    private SharedPreferenceHelper() {
        mSharedPreferences = null;
        mActivity = null;
    }

    public static SharedPreferenceHelper getInstance(Activity activity) {
        if(ourInstance.mSharedPreferences == null)
        {
            ourInstance.mSharedPreferences = activity.getPreferences(Context.MODE_PRIVATE);
        }
        return ourInstance;
    }

    public String GetSavedEmail() throws Exception {
        if(mSharedPreferences.getBoolean(AreCredentialsRemembered, false))
        {
            return mSharedPreferences.getString(EmailKey, null);
        }
        else
        {
            throw new Exception("Unable to retrieve user credentials");
        }
    }

    public String GetSavedPassword() throws Exception {
        if(mSharedPreferences.getBoolean(AreCredentialsRemembered, false))
        {
            return mSharedPreferences.getString(PasswordKey, null);
        }
        else
        {
            throw new Exception("Unable to retrieve user credentials");
        }
    }

    public void SaveCredentials(String userName, String password)
    {
        SharedPreferences.Editor edit = mSharedPreferences.edit();
        edit.putBoolean(AreCredentialsRemembered, true);
        edit.putString(EmailKey, userName);
        edit.putString(PasswordKey, password);
        edit.commit();
    }

    public void DeleteCredentials()
    {
        SharedPreferences.Editor edit = mSharedPreferences.edit();
        edit.putBoolean(AreCredentialsRemembered, false);
        edit.putString(EmailKey, null);
        edit.putString(PasswordKey, null);
        edit.commit();
    }
}
