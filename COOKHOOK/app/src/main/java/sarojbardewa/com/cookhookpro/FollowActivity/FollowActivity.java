package sarojbardewa.com.cookhookpro.FollowActivity;

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

import sarojbardewa.com.cookhookpro.FollowedActivity.FollowedActivity;
import sarojbardewa.com.cookhookpro.R;

/**
 * Created by b on 6/12/17.
 */

/**
 * Created by b on 6/11/17.
 */

public class FollowActivity extends AppCompatActivity {

    private ImageButton btnEditProfile;
    private Button btnViewRecipes;
    private ImageView mImageView;
    private String UID;
    private TextView mName, mLocation, mFavRec, mDietary;
    private String mURL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xfollowed);


        mImageView = (ImageView) findViewById(R.id.imgProfilePic);
        mImageView.setImageResource(R.drawable.pic1);

        mName = (TextView) findViewById(R.id.name);
        mLocation = (TextView) findViewById(R.id.location);
        mFavRec = (TextView) findViewById(R.id.favrec);
        mDietary = (TextView) findViewById(R.id.dietary);










        btnEditProfile = (ImageButton) findViewById(R.id.imgFloating);
        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(FollowActivity.this, FollowedActivity.class);
                //intent.putExtra("message", mAddress);
                startActivity(intent);


            }
        });
        btnViewRecipes = (Button) findViewById(R.id.viewrecipes);
        btnViewRecipes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "You must follow this person first.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}