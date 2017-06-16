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
 * Kyle - Starter code borrowed from Project 3, modified for our purposes to include Firebase Account
 *  authentication.
 */

/**
 * A login screen that offers login via email/password.
 * Provides Functionality to Signup for new account
 */
public class LoginActivity extends AppCompatActivity implements OnClickListener, OnCompleteListener<AuthResult> {
    public static final String EXTRA_EMAIL = "sarojbardewa.com.cookhookpro.loginandsplashscreens.email";
    public static final String EXTRA_PASSWORD = "sarojbardewa.com.cookhookpro.loginandsplashscreens.password";

    // UI references
    private EditText mEmailView;
    private EditText mPasswordView;
    private CheckBox mRememberCheckBox;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmailView = (EditText) findViewById(R.id.login_email);
        mPasswordView = (EditText) findViewById(R.id.password);
        mRememberCheckBox = (CheckBox) findViewById(R.id.remember_me_checkBox);

        //OnClick Listener for SignInButton
        findViewById(R.id.email_sign_in_button).setOnClickListener(this);
        findViewById(R.id.create_account_button).setOnClickListener(this);

        //It's possible that the LoginActivity launched this app in addition to the splash screen.
        //Check to see if an email and password were passed as extras through the Intent so that the
        //email/password fields may be auto-populated with the credentials of the account which was just created.
        Intent temp = getIntent();
        String email = temp.getStringExtra(LoginActivity.EXTRA_EMAIL);
        String password = temp.getStringExtra(EXTRA_PASSWORD);
        if(email != null && password != null) {
            mEmailView.setText(email);
            mPasswordView.setText(password);
            Toast.makeText(getApplicationContext(), "Account Created Successfully", Toast.LENGTH_SHORT).show();
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
                String email = mEmailView.getText().toString();
                String password = mPasswordView.getText().toString();

                UserProfile profile = UserProfile.getInstance();
                try
                {
                    /**
                     * Passing in "this" registers the onComplete callback defined in this class.
                     * Once the login process is started in the background, a progress dialog is
                     * launched, and will remain active until the callback is called.
                     */
                    profile.Login(email, password, this);
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
        /**
         * First thing to do is dismiss the progress dialog, since the login request just completed.
         */
        dialog.dismiss();
        if(task.isSuccessful()) {
            if (mRememberCheckBox.isChecked()) {
                String email = mEmailView.getText().toString();
                String password = mPasswordView.getText().toString();
                SharedPreferenceHelper.getInstance(this).SaveCredentials(email, password);
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
        Intent temp = new Intent(getApplicationContext(), RecipeActivity.class);
        startActivity(temp);
        finish();
    }
}