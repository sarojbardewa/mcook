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

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import sarojbardewa.com.cookhookpro.FollowedActivity.FollowedActivity;
import sarojbardewa.com.cookhookpro.ProfileUpdateActivity.ProfileUpload;
import sarojbardewa.com.cookhookpro.R;
import sarojbardewa.com.cookhookpro.newrecipe.RecipeModel;

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



        final FirebaseDatabase database1 = FirebaseDatabase.getInstance();

        DatabaseReference refr = database1.getReference("recipes/");

// Attach a listener to read the data at our posts reference
        refr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                RecipeModel uidupload = dataSnapshot.getValue(RecipeModel.class);
                if (uidupload != null) {
                    UID = uidupload.userID;

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
        //String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        String PATH = "user" + "/" + UID;
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
                    Glide.with(FollowActivity.this).load(mURL).into(mImageView);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });






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