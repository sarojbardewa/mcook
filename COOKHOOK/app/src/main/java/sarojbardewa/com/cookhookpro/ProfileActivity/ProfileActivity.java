package sarojbardewa.com.cookhookpro.ProfileActivity;

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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import sarojbardewa.com.cookhookpro.ProfileUpdateActivity.UpdateProfileActivity;
import sarojbardewa.com.cookhookpro.R;
import sarojbardewa.com.cookhookpro.profilehelper.ProfileHelperActivity;
import sarojbardewa.com.cookhookpro.profilehelper.User;

/**
 * Created by b on 6/11/17.
 */

public class ProfileActivity extends AppCompatActivity {

    private ImageButton btnEditProfile;
    private Button btnViewRecipes;
    private ImageView mImageView;
    private User mProfileInfo;
    private TextView mName, mLocation, mFavRec, mDietary;
    private String name, location;
    private ProfileHelperActivity mHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_own);


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();


        Query query = reference.child("users").child((String) mHelper.getUserId()).child("profileName");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // dataSnapshot is the "issue" node with all children with id 0
                    dataSnapshot.getChildren();


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mImageView = (ImageView) findViewById(R.id.imgProfilePic);
        mImageView.setImageResource(R.drawable.pic1);

        mName = (TextView) findViewById(R.id.name);
        mName.setText(mProfileInfo.profileName);
        mLocation = (TextView) findViewById(R.id.location);
        mFavRec = (TextView) findViewById(R.id.favrec);
        mDietary = (TextView) findViewById(R.id.dietary);
        if (mName!=null) {

            mLocation.setText(mProfileInfo.profileLocation);
            mFavRec.setText(mProfileInfo.profileFavRecipe);
            mDietary.setText(mProfileInfo.profileDietaryRes);
        }
        btnEditProfile = (ImageButton) findViewById(R.id.btnEdit);
        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, UpdateProfileActivity.class);
                startActivity(intent);

            }
        });
        btnViewRecipes = (Button) findViewById(R.id.viewrecipes);
        btnViewRecipes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pass user info here
                Intent intent = new Intent(ProfileActivity.this, UpdateProfileActivity.class);
                startActivity(intent);
            }
        });
    }
}