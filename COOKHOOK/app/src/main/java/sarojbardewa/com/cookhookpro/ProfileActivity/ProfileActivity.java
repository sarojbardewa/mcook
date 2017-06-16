package sarojbardewa.com.cookhookpro.ProfileActivity;

/**
 * Created by b on 6/12/17.
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import sarojbardewa.com.cookhookpro.ProfileUpdateActivity.ProfileUpload;
import sarojbardewa.com.cookhookpro.ProfileUpdateActivity.UpdateProfileActivity;
import sarojbardewa.com.cookhookpro.R;
import sarojbardewa.com.cookhookpro.mainrecipescreen.RecipeActivity;

/**
 * Created by b on 6/11/17.
 */

public class ProfileActivity extends AppCompatActivity {

    private ImageButton btnEditProfile;
    private Button btnViewRecipes;
    private ImageView mImageView;
    private DatabaseReference mReference;
    private String mURL;
    private TextView mName, mLocation, mFavRec, mDietary;
    private Context mContext = ProfileActivity.this;
    private Context userIconContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_own);

        mImageView = (ImageView) findViewById(R.id.imgProfilePic);
        mImageView.setImageResource(R.drawable.pic1);

        mName = (TextView) findViewById(R.id.name);
        //mName.setText("tester");
        mLocation = (TextView) findViewById(R.id.location);
        mFavRec = (TextView) findViewById(R.id.favrec);
        mDietary = (TextView) findViewById(R.id.dietary);
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        String PATH = "user" + "/" + uid;
        DatabaseReference ref = database.getReference(PATH);

// Attach a listener to read the data at our posts reference
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ProfileUpload profileupload = dataSnapshot.getValue(ProfileUpload.class);
                if (profileupload != null) {
                    mName.setText(profileupload.getName());
                    mLocation.setText(profileupload.getLocation());
                    mFavRec.setText(profileupload.getFavrec());
                    mDietary.setText(profileupload.getDietary());
                    mURL = profileupload.getUrl();
                    Glide.with(mContext).load(mURL).into(mImageView);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });


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
                Intent intent = new Intent(ProfileActivity.this, RecipeActivity.class);
                startActivity(intent);
            }
        });
    }
}