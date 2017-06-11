package sarojbardewa.com.cookhookpro.profilehelper;

/**
 * Created by b on 6/11/17.
 * HERE IS CODE demonstrating how to use this helper:
 import android.os.Bundle;
 import android.support.v7.app.AppCompatActivity;
 import android.text.TextUtils;

 public class MainActivity extends AppCompatActivity {

 private ProfileHelperActivity mProfileHelper = new ProfileHelperActivity();
 @Override
 protected void onCreate(Bundle savedInstanceState) {
 super.onCreate(savedInstanceState);
 setContentView(R.layout.activity_main);
 String name = "Billy Boy";
 String email = "foosh@fooshy.com";
 String profilename = "booboo";
 String location = "dallas";
 String recipe = "chilli";
 String dietary = "veggies";
 String uri = "lmno";

 if (TextUtils.isEmpty(mProfileHelper.getUserId())) {
 mProfileHelper.createUser(name, email);
 } else {
 mProfileHelper.updateUser(name, email);
 }

 if (TextUtils.isEmpty(mProfileHelper.getUserId())) {
 mProfileHelper.createProfile(profilename, location, recipe, dietary, uri);
 } else {
 mProfileHelper.updateProfile(profilename, location, recipe, dietary, uri);
 }
 }

 }
 */

import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by b on 6/11/17.
 */

public class ProfileHelperActivity extends AppCompatActivity {

    private static final String TAG = "ProfileHelperActivity";
    private String userId;
    private DatabaseReference mFirebaseDatabaseUser;
    private DatabaseReference mFirebaseDatabaseUserList;
    private FirebaseDatabase  mFirebaseInstance;
    private User mUser;
    public ProfileHelperActivity(){
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabaseUser = mFirebaseInstance.getReference("users");
        mFirebaseDatabaseUserList = mFirebaseInstance.getReference("userById");
        mFirebaseInstance.getReference("Root").setValue("TREE: Top is users and list of users");
        mFirebaseInstance.getReference("Root").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e(TAG, "Data Base is Prepared");
                String Root = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(TAG, "Failed to read app title value.", error.toException());
            }
        });
    }
    public ProfileHelperActivity(boolean x){  //constructor for pre-existing data base

    }
    public void setUserlist(String userId) {

        //ref.child(userId).set(1);
    }

    protected void createUser(String name, String email) {
        // TODO
        // In real apps this userId should be fetched
        // by implementing firebase auth
        if (TextUtils.isEmpty(userId)) {
            userId = mFirebaseDatabaseUser.push().getKey();
            mFirebaseDatabaseUserList.child(userId).setValue(1);
            addUserIdChangeListener();
        }

        mUser = new User(name, email);

        mFirebaseDatabaseUser.child(userId).setValue(mUser);

        addUserChangeListener();
    }
    protected void updateUser(String name, String email) {
        // updating the user via child nodes
        if (!TextUtils.isEmpty(name))
            mFirebaseDatabaseUser.child(userId).child("name").setValue(name);

        if (!TextUtils.isEmpty(email))
            mFirebaseDatabaseUser.child(userId).child("email").setValue(email);

    }
    protected void createProfile(String profilename, String profilelocation, String profilefavrec, String profiledietary, String imuri) {
        // TODO
        // In real apps this userId should be fetched
        // by implementing firebase auth
        if (TextUtils.isEmpty(userId)) {
            userId = mFirebaseDatabaseUser.push().getKey();
        }

        //mUser = new User(profilename, profilelocation, profilefavrec, profiledietary, imuri);
        mUser.setProfileName(profilename);
        mUser.setProfileLocation(profilelocation);
        mUser.setProfileFavRecipe(profilefavrec);
        mUser.setProfileDietaryRes(profiledietary);
        mUser.setProfileimURI(imuri);

        mFirebaseDatabaseUser.child(userId).setValue(mUser);

        addUserChangeListener();
    }
    protected void updateProfile(String profilename, String profilelocation, String profilefavrec, String profiledietary, String imuri) {
        // updating the user via child nodes
        if (!TextUtils.isEmpty(profilename))
            mFirebaseDatabaseUser.child(userId).child("profile name").setValue(profilename);

        if (!TextUtils.isEmpty(profilelocation))
            mFirebaseDatabaseUser.child(userId).child("location").setValue(profilelocation);

        if (!TextUtils.isEmpty(profilefavrec))
            mFirebaseDatabaseUser.child(userId).child("recipe").setValue(profilefavrec);

        if (!TextUtils.isEmpty(profiledietary))
            mFirebaseDatabaseUser.child(userId).child("dietary").setValue(profiledietary);

        if (!TextUtils.isEmpty(imuri))
            mFirebaseDatabaseUser.child(userId).child("Profile Picture").setValue(imuri);
    }

    private void addUserChangeListener() {
        // User data change listener
        mFirebaseDatabaseUser.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                // Check for null
                if (user == null) {
                    Log.e(TAG, "User data is null!");
                    return;
                }

                Log.e(TAG, "User data is changed!" + user.mName + ", " + user.mEmail);

                // Display newly updated name and email
                //txtDetails.setText(user.name + ", " + user.email);

                // clear edit text
                //inputEmail.setText("");
                //inputName.setText("");

                //toggleButton();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(TAG, "Failed to read user", error.toException());
            }
        });
    }
    private void addUserIdChangeListener() {
        // User data change listener
        mFirebaseDatabaseUser.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                // Check for null
                if (user == null) {
                    Log.e(TAG, "User data is null!");
                    return;
                }

                Log.e(TAG, "User data is changed!" + user.mName + ", " + user.mEmail);

                // Display newly updated name and email
                //txtDetails.setText(user.name + ", " + user.email);

                // clear edit text
                //inputEmail.setText("");
                //inputName.setText("");

                //toggleButton();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(TAG, "Failed to read user", error.toException());
            }
        });
    }

    public CharSequence getUserId() {
        return userId;
    }
}

