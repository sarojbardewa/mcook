package sarojbardewa.com.cookhookpro.loginandsplashscreens;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import sarojbardewa.com.cookhookpro.R;
import sarojbardewa.com.cookhookpro.mainrecipescreen.RecipeActivity;

/**
 * Created by Hiral on 7/28/2016.
 */

/**
 * A login screen that offers login via email/password.
 * Provides Functionality to Signup for new account
 */
public class LoginActivity extends AppCompatActivity implements OnClickListener, OnCompleteListener<AuthResult> {
    public static final String EXTRA_USER_NAME = "sarojbardewa.com.cookhookpro.loginandsplashscreens.user_name";
    public static final String EXTRA_PASSWORD = "sarojbardewa.com.cookhookpro.loginandsplashscreens.password";

    // UI references
    private EditText mUsernameView;
    private EditText mPasswordView;
    private CheckBox mRememberCheckBox;
    private ProgressDialog dialog;
    private static String mUsername;
    private static String mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mUsernameView = (EditText) findViewById(R.id.username);
        mPasswordView = (EditText) findViewById(R.id.password);
        mRememberCheckBox = (CheckBox) findViewById(R.id.remember_me_checkBox);

        //OnClick Listener for SignInButton
        findViewById(R.id.email_sign_in_button).setOnClickListener(this);
        findViewById(R.id.create_account_button).setOnClickListener(this);

        Intent temp = getIntent();
        String userName = temp.getStringExtra(EXTRA_USER_NAME);
        String password = temp.getStringExtra(EXTRA_PASSWORD);
        if(userName != null && password != null) {
            mUsernameView.setText(userName);
            mPasswordView.setText(password);
            Toast.makeText(getApplicationContext(), "Account Created Succesfully", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.create_account_button:
                startActivity(new Intent(this, SignUPActivity.class));
                break;
            case R.id.email_sign_in_button:
                // Code below gets the User name and Password
                String userName = mUsernameView.getText().toString();
                String password = mPasswordView.getText().toString();

                UserProfile profile = UserProfile.getInstance();
                try
                {
                    //TODO: Might need to move this to a thread if it takes too long
                    profile.Login(userName, password, this);
                    dialog = new ProgressDialog(this);
                    dialog.setTitle("Logging In...");
                    dialog.show();

                }
                catch(Exception ex)
                {
                    Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onComplete(Task<AuthResult> task)
    {
        dialog.dismiss();
        if(task.isSuccessful()) {
            if (mRememberCheckBox.isChecked()) {
                String userName = mUsernameView.getText().toString();
                String password = mPasswordView.getText().toString();
                SharedPreferenceHelper.getInstance(this).SaveCredentials(userName, password);
            }
            StartMainActivity();
        } else {
            Exception ex = task.getException();
            if(ex != null)
            {
                Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Login failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void StartMainActivity()
    {
        //TODO: Launch main activity
        Intent temp = new Intent(getApplicationContext(), RecipeActivity.class);
        startActivity(temp);
        finish();
    }
}