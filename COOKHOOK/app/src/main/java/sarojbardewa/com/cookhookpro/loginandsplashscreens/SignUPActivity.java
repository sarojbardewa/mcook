package sarojbardewa.com.cookhookpro.loginandsplashscreens;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import sarojbardewa.com.cookhookpro.R;
import sarojbardewa.com.cookhookpro.mainrecipescreen.RecipeActivity;

public class SignUPActivity extends AppCompatActivity
{
    EditText editTextUserName,editTextPassword,editTextConfirmPassword;
    Button btnCreateAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        editTextUserName = (EditText) findViewById(R.id.sign_up_username);
        editTextPassword = (EditText) findViewById(R.id.sign_up_password);
        editTextConfirmPassword = (EditText) findViewById(R.id.sign_up_confirm_password);

        btnCreateAccount = (Button) findViewById(R.id.sign_up_create_account_button);

        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String userName=editTextUserName.getText().toString();
                String password=editTextPassword.getText().toString();
                String confirmPassword=editTextConfirmPassword.getText().toString();

                // check if any of the fields are vacant
                if(userName.equals("")||password.equals("")||confirmPassword.equals(""))
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
                        user.CreateAccount(userName, password);
                        user.Login(userName, password);
                        Intent temp = new Intent(getApplicationContext(), RecipeActivity.class);
                        startActivity(temp);
                        finish();
                    } catch (Exception ex) {
                        Toast.makeText(getApplicationContext(), "Account creation failed: " + ex.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

            }
        });
    }
}
