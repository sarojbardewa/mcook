package sarojbardewa.com.cookhookpro.loginandsplashscreens;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import sarojbardewa.com.cookhookpro.R;
import sarojbardewa.com.cookhookpro.mainrecipescreen.RecipeActivity;

/**
 * This class was borrowed from Project 3's signup activity and adapted for our purposes.
 * The GPS and local database code was removed, and replaced with firebase authentication and account creation.
 */

public class SignUPActivity extends AppCompatActivity implements OnCompleteListener<AuthResult>
{
    EditText editTextUserName, editTextEmail, editTextPassword, editTextConfirmPassword;
    Button btnCreateAccount;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        editTextUserName = (EditText) findViewById(R.id.sign_up_username);
        editTextEmail = (EditText) findViewById(R.id.sign_up_email);
        editTextPassword = (EditText) findViewById(R.id.sign_up_password);
        editTextConfirmPassword = (EditText) findViewById(R.id.sign_up_confirm_password);

        btnCreateAccount = (Button) findViewById(R.id.sign_up_create_account_button);

        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String userName = editTextUserName.getText().toString();
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();
                String confirmPassword = editTextConfirmPassword.getText().toString();

                // check if any of the fields are vacant
                if(userName.equals("") || email.equals("") || password.equals("") || confirmPassword.equals(""))
                {
                    Toast.makeText(getApplicationContext(), "Field Vacant", Toast.LENGTH_LONG).show();
                    return;
                }
                // check if both password matches
                if(!password.equals(confirmPassword))
                {
                    Toast.makeText(getApplicationContext(), "Password does not match", Toast.LENGTH_LONG).show();
                    return;
                }
                else
                {
                    UserProfile user = UserProfile.getInstance();
                    try {
                        //Similar to the login activity, create a progress dialog when login starts.
                        //This activity implements the onCompleteListener interface, which is why "this"
                        //is passed to the CreateAccount function.
                        user.CreateAccount(userName, email, password, SignUPActivity.this);
                        dialog = new ProgressDialog(SignUPActivity.this);
                        dialog.setTitle("Creating account...");
                        dialog.show();
                    } catch (Exception ex) {
                        Toast.makeText(getApplicationContext(), "Account creation failed: " + ex.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

            }
        });
    }

    @Override
    public void onComplete(Task<AuthResult> task)
    {
        //Dismiss the progress dialog when the login task is completed
        dialog.dismiss();
        if(task.isSuccessful())
        {
            String email = editTextEmail.getText().toString();
            String password = editTextPassword.getText().toString();
            Intent temp = new Intent(getApplicationContext(), LoginActivity.class);
            temp.putExtra(LoginActivity.EXTRA_EMAIL, email);
            temp.putExtra(LoginActivity.EXTRA_PASSWORD, password);
            startActivity(temp);
            finish();
        }
        else
        {
            Exception ex = task.getException();
            if(ex != null)
            {
                Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Account creation failed", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
