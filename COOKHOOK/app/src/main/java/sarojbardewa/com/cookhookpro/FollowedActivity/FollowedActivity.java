package sarojbardewa.com.cookhookpro.FollowedActivity;

/**
 * Created by b on 6/12/17.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import sarojbardewa.com.cookhookpro.ProfileActivity.ProfileActivity;
import sarojbardewa.com.cookhookpro.R;
import sarojbardewa.com.cookhookpro.profilehelper.User;

/**
 * Created by b on 6/12/17.
 */

/**
 * Created by b on 6/11/17.
 */

public class FollowedActivity extends AppCompatActivity {

    private ImageButton btnEditProfile;
    private Button btnViewRecipes;
    private ImageView mImageView;
    private User mProfileInfo;
    private TextView mName, mLocation, mFavRec, mDietary;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followed);


        mImageView = (ImageView) findViewById(R.id.imgProfilePic);
        mImageView.setImageResource(R.drawable.pic1);

        mName = (TextView) findViewById(R.id.name);
        mLocation = (TextView) findViewById(R.id.location);
        mFavRec = (TextView) findViewById(R.id.favrec);
        mDietary = (TextView) findViewById(R.id.dietary);
        if (mName!=null) {
            mName.setText(mProfileInfo.profileName);
            mLocation.setText(mProfileInfo.profileLocation);
            mFavRec.setText(mProfileInfo.profileFavRecipe);
            mDietary.setText(mProfileInfo.profileDietaryRes);
        }
        btnEditProfile = (ImageButton) findViewById(R.id.imgFloating);
        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Already Following!", Toast.LENGTH_SHORT).show();

            }
        });
        btnViewRecipes = (Button) findViewById(R.id.viewrecipes);
        btnViewRecipes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get and pass user id here
                Intent intent = new Intent(FollowedActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
    }
}

