package sarojbardewa.com.cookhookpro.mainrecipescreen;

import android.app.FragmentManager;
import android.content.Intent;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import sarojbardewa.com.cookhookpro.ProfileActivity.ProfileActivity;
import sarojbardewa.com.cookhookpro.R;
import sarojbardewa.com.cookhookpro.loginandsplashscreens.UserProfile;
import sarojbardewa.com.cookhookpro.newrecipe.NewRecipeActivity;
import sarojbardewa.com.cookhookpro.newrecipe.RecipeModel;
import sarojbardewa.com.cookhookpro.shoppinglist.ShoppingListActivity;

public class RecipeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnSelectedBookChangeListener {

    private final static String MKEY = "ORIENTATION";
    private final static String MINDEX = "RECIPE_INDEX";
    private final static String MVIEW = "CURRENT_VIEW";
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    public static final String STORAGE_PATH = "image/";
    private final static String TAG = "RecipeActivity";

    private RecipeModel desRecipeModel;  // For passing to recipe description fragment
    int recipePosition;


    //******************
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        // Create references to the database and image file
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Slide slideLeftTransition = new Slide(Gravity.LEFT);
        slideLeftTransition.setDuration(500);

        RecipeListFragment listFragment = RecipeListFragment.newInstance();
        listFragment.setExitTransition(slideLeftTransition);

        FragmentManager fm = getFragmentManager();
        fm.beginTransaction()
                .add(R.id.dashboard_content, listFragment)
                .commit();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userName = user.getDisplayName();
        String email = user.getEmail();
        View nav_header = LayoutInflater.from(this).inflate(R.layout.user_loggedin, null);
        ((TextView) nav_header.findViewById(R.id.header_user)).setText(userName);
        ((TextView) nav_header.findViewById(R.id.header_email)).setText(email);
        navigationView.addHeaderView(nav_header);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.recipe, menu);
        return true;
    }

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
<<<<<<< HEAD
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        } else if (id == R.id.direction) {
            Intent intent = new Intent(this, StxStDirActivity.class);
            startActivity(intent);
        }else if (id == R.id.favorites) {
=======

        } else if (id == R.id.favorites) {
>>>>>>> 20a45efd67e923d58d4cf89a34d1cd22b5f69e76

        } else if (id == R.id.shopping_list) {
            Intent intent = new Intent(this, ShoppingListActivity.class);
            startActivity(intent);
        } else if (id == R.id.new_recipe) {
            Log.i(TAG, " new_recipe activity launched");
            Intent intent = new Intent(this, NewRecipeActivity.class);
            startActivity(intent);

        } else if (id == R.id.sign_out) {
            UserProfile.getInstance().LogoutAndGoToLoginScreen(this);
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //************************
    // TODO : When the recipe is selected, display the book
    @Override
    public void onSelectedBookChanged(View view, int bookIndex, RecipeModel recipeModel) {
        desRecipeModel = recipeModel;  // Save the current recipe model for display
        recipePosition = bookIndex;

        TextView titleTextView = (TextView) view.findViewById(R.id.recipeTitle);
        ImageView bookImageView = (ImageView) view.findViewById(R.id.topImage);

        // Animate the book movements
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
                .addSharedElement(bookImageView, "book_image_" + bookIndex)
                .addSharedElement(titleTextView, "title_text_" + bookIndex)
                .addToBackStack(null)
                .commit();

    }
}