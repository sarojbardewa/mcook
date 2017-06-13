package sarojbardewa.com.cookhookpro.mainrecipescreen;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import sarojbardewa.com.cookhookpro.FollowActivity.FollowActivity;
import sarojbardewa.com.cookhookpro.R;
import sarojbardewa.com.cookhookpro.StxStDirActivity.StxStDirActivity;
import sarojbardewa.com.cookhookpro.newrecipe.RecipeModel;
import sarojbardewa.com.cookhookpro.shoppinglist.ShoppingList;


public class RecipeDescFragment extends Fragment{
    //**********
    // Get access to the database
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mDatabaseReference = database.getReference();

    List<RecipeModel> recipeList;  // List of recipe models

    private static final String RKEY = "RECIPE_KEY";
    private static final String ARG_IMAGE_ID = "image id";
    private static final String ARG_POSITION = "position";
    private RecipeModel rm1;  // Get a private reference to a RecipeModel
    Context mContext;

    public static RecipeDescFragment newInstance(RecipeModel recipeModel) {

        // Instantiate the fragment
        RecipeDescFragment fragment = new RecipeDescFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(RKEY, recipeModel);
        fragment.setArguments(bundle);
        return fragment;
    }

    public RecipeDescFragment() {
        // Required empty public constructor

    }

     @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         mContext = getActivity(); // Get the current activity
         container.removeAllViews();  // Remove all paste fragments if any
         View rootView = inflater.inflate(R.layout.fragment_recipe_desc, container, false);

         rm1 = (RecipeModel) getArguments().getSerializable(RKEY);

         /**
          * Get all the handle to the widgets
          */
         TextView recipeTitleView = (TextView)rootView.findViewById(R.id.recipeTitle);
         TextView recipeDescriptionView = (TextView)rootView.findViewById(R.id.recipeDescription);
         ImageView topImageView = (ImageView)rootView.findViewById(R.id.topImage);
         Button recipeBy = (Button) rootView.findViewById(R.id.recipe_by_button);
         TextView recipeIngredients = (TextView)rootView.findViewById(R.id.ingredients_textview);
         TextView timeDesp = (TextView) rootView.findViewById(R.id.time_desp_textview);
         Button startCooking = (Button) rootView.findViewById(R.id.start_cooking_button);
         TextView recipeDirections = (TextView) rootView.findViewById(R.id.directions_textview);
         Button addToShoppingList = (Button) rootView.findViewById(R.id.add_to_shoppinglist_button);

         recipeBy.setOnClickListener(new View.OnClickListener() { @Override
         public void onClick(View view) {

             Intent intent = new Intent(getActivity(), FollowActivity.class);
             startActivity(intent);
         }
         });
         startCooking.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 StxStDirActivity.setRecipe(rm1);
                 Intent intent = new Intent(getActivity(), StxStDirActivity.class);
                 startActivity(intent);
             }
         });

         addToShoppingList.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 ShoppingList.getInstance().AddRecipe(rm1);
                 Toast.makeText(getActivity().getApplicationContext(), "Added " + rm1.getName() + " to shopping list.", Toast.LENGTH_SHORT).show();
             }
         });

         /**
          * Use glide to show the image
          */
         Glide.with(mContext)
                 .load(rm1.imageUrl)
                 .into(topImageView);


         recipeTitleView.setText(rm1.name);
         recipeBy.setText(rm1.userID);
         timeDesp.setText(rm1.cooktime);
         recipeDescriptionView.setText(rm1.description);

         String temp = "";
         for (String s :rm1.ingredients)
         {
             temp += s + "\n";
         }

        recipeIngredients.setText(temp);

         temp = "";
         for (String s :rm1.directions)
         {
             temp += s + "\n";
         }
         recipeDirections.setText(temp);
         //topImageView.setTransitionName("book_image_" + position);

         /**
          * Add to the shopping list if user presses the shopping list button
          */


         /**
          * When start cooking is created
          */
         return rootView;


    }

    /**
     * Lock the fragment to portrait mode only
     */

    @Override
    public void onResume(){
        super.onResume();
        if(getActivity() !=null) {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

    }

    @Override
    public void onPause() {
        if (getActivity() != null) {
            super.onPause();
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        }
    }

}
