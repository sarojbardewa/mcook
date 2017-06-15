package sarojbardewa.com.cookhookpro.mainrecipescreen;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.ChangeBounds;
import android.transition.ChangeTransform;
import android.transition.Slide;
import android.transition.TransitionSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import sarojbardewa.com.cookhookpro.ProfileActivity.ProfileActivity;
import sarojbardewa.com.cookhookpro.ProfileUpdateActivity.ProfileUpload;
import sarojbardewa.com.cookhookpro.R;
import sarojbardewa.com.cookhookpro.loginandsplashscreens.UserProfile;
import sarojbardewa.com.cookhookpro.newrecipe.NewRecipeActivity;
import sarojbardewa.com.cookhookpro.newrecipe.RecipeModel;
import sarojbardewa.com.cookhookpro.shoppinglist.ShoppingListActivity;

/**
 * <h1>Recipe Main Activity </h1>
 * This is the recipe activity class that gets triggered after the
 * user authentication is complete. The main purpose of this class
 * is to host recipe list and recipe description fragments.
 * This activity further has a navigation launcher bar that allows users to
 * select other user functions such as select user's profile, find the favorite
 * recipe and sign out.
 * @author      : Saroj Bardewa
 * @version     : v.01
 * @since       :05-29-2017
 */
public class RecipeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        OnSelectedRecipeChangeListener {

    private StorageReference mStorageRef;   // Firebase storage reference
    private DatabaseReference mDatabaseRef; // Firebase database reference
    private final static String TAG = "RecipeActivity";
    private ImageView userIconView;
    private Context mContext;
    private RecipeModel desRecipeModel;  // For passing to recipe description fragment

    int recipePosition;

    /**
     * This function returns the context of recipe activity
     * @return Context - The context of RecipeActivity
     */
    public Context getActivityContext(){
        return RecipeActivity.this;
    }

    /**
     * This function inflates the layout and hosts the fragment
     * @param savedInstanceState - The value of saved variable on device configuration change
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        mContext = getActivityContext();  // Get the present activity context

        // Create references to the database and image file
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        // Set the toolbar and the navigation drawer layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Set the transition of the list fragment from the left
        // This will create an animation of the list fragment
        Slide slideLeftTransition = new Slide(Gravity.LEFT);
        slideLeftTransition.setDuration(500);
        RecipeListFragment listFragment = RecipeListFragment.newInstance();
        listFragment.setExitTransition(slideLeftTransition);

        //Evoke the fragment manager to add listFragment to the existing activity
        FragmentManager fm = getFragmentManager();
        fm.beginTransaction()
                .add(R.id.dashboard_content, listFragment)
                .commit();

        // Update the user logged in info
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userName = user.getDisplayName();
        String email = user.getEmail();
        View nav_header = LayoutInflater.from(this).inflate(R.layout.user_loggedin, null);

        // At the top of the navigation header, add the username and email of the current user
        ((TextView) nav_header.findViewById(R.id.header_user)).setText(userName);
        ((TextView) nav_header.findViewById(R.id.header_email)).setText(email);
        userIconView = ((ImageView) nav_header.findViewById(R.id.imageView_user));
        navigationView.addHeaderView(nav_header);

        // Add user photo on his/her navigation icon
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String PATH = "user" + "/" + uid;   // Create path to the user profile to access it.
        FirebaseDatabase.getInstance().getReference(PATH).addValueEventListener(new ValueEventListener() {

            /**
             * Every time a user changes his/her image, the main recipe screen will be updated
             * by their latest user-profile.
             * @param dataSnapshot - The data at a particular point in time
             */
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            ProfileUpload profileUpload = dataSnapshot.getValue(ProfileUpload.class);
            if(profileUpload !=null){
                Log.d("onDataChangeIMAGE", ""+profileUpload.url );
                Glide.with(mContext).load(profileUpload.url).into(userIconView);
            }
        }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * The back press will close the navigation drawer in the main screen if it is open
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * This displays the top bar menu
     * @param menu  - Items in the top bar
     * @return boolean - top bar shown or not shown
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.recipe, menu);
        return true;
    }

    /**
     * When each button on the drawer is selected a new activity corresponding
     * to that button is launched.
     * @param item -An item in the navigative drawer
     * @return boolean -true or false
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.my_profile) {
            // When the MY PROFILE button is pressed, start the profile activity
            // to show the profile
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        } else if (id == R.id.following) {
            // When the MY PROFILE button is pressed, show a message that
            // this functionality is in progress
            Toast.makeText(this, "Feature coming soon!", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.shopping_list) {
            // When the Shopping List button is pressed, the items currently
            // in the shopping list is displayed
            Intent intent = new Intent(this, ShoppingListActivity.class);
            startActivity(intent);
        } else if (id == R.id.new_recipe) {
            // Upload New Recipe button when pressed allows user to add a new recipe
            Log.i(TAG, " new_recipe activity launched");
            Intent intent = new Intent(this, NewRecipeActivity.class);
            startActivity(intent);

        } else if (id == R.id.sign_out) {
            // The sign out button takes the user to the login screen
            UserProfile.getInstance().LogoutAndGoToLoginScreen(this);
        } else if (id == R.id.nav_share) {
            // Open the app store for our app
            Uri webpage = Uri.parse("https://play.google.com/store");
            Intent appStore = new Intent(Intent.ACTION_VIEW, webpage);
            startActivity(appStore);

        } else if (id == R.id.nav_send) {
            //Send email to the developers
            Uri number = Uri.parse ("tel:5034819854");
           Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
            startActivity(callIntent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


   // When the recipe is selected, display the recipe
    @Override
    public void onSelectedRecipeChanged(View view, int recipeIndex, RecipeModel recipeModel) {
        desRecipeModel = recipeModel;  // Save the current recipe model for display
        recipePosition = recipeIndex;

        TextView titleTextView = (TextView) view.findViewById(R.id.recipeTitle);
        ImageView recipeImageView = (ImageView) view.findViewById(R.id.topImage);

        // Animate the recipe movements
        Slide slideBottomTransition = new Slide(Gravity.BOTTOM);
        slideBottomTransition.setDuration(500);

        ChangeBounds changeBoundsTransition = new ChangeBounds();

        ChangeTransform changeTransformTransition = new ChangeTransform();

        TransitionSet transitionSet = new TransitionSet();
        transitionSet.addTransition(changeBoundsTransition);
        transitionSet.addTransition(changeTransformTransition);
        transitionSet.setDuration(500);

        // Get handle to the recipe description fragment

        RecipeDescFragment recipeDescFragment =
                RecipeDescFragment.newInstance(recipeModel); // Get the reference to the fragment
        recipeDescFragment.setEnterTransition(slideBottomTransition);
        recipeDescFragment.setAllowEnterTransitionOverlap(false);
        recipeDescFragment.setSharedElementEnterTransition(transitionSet);


        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.dashboard_content, recipeDescFragment)
                .addSharedElement(recipeImageView, "recipe_image_" + recipeIndex)
                .addSharedElement(titleTextView, "title_text_" + recipeIndex)
                .addToBackStack(null)
                .commit();

    }
}