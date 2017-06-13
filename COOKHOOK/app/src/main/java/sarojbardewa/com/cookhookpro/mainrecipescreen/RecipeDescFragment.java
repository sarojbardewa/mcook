package sarojbardewa.com.cookhookpro.mainrecipescreen;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import sarojbardewa.com.cookhookpro.R;
import sarojbardewa.com.cookhookpro.newrecipe.NewRecipeActivity;
import sarojbardewa.com.cookhookpro.newrecipe.RecipeModel;


public class RecipeDescFragment extends Fragment {
    //**********
    // Get access to the database
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mDatabaseReference = database.getReference();

    List<RecipeModel> recipeList;  // List of recipe models

    private static final String ARG_TITLE = "title";
    private static final String ARG_DESCRIPTION = "description";
    private static final String ARG_IMAGE_ID = "image id";
    private static final String ARG_POSITION = "position";
    RecipeModel rm1;


    public static RecipeDescFragment newInstance(String title, String description,
                                                 int imageResourceId, int position, RecipeModel recipeModel) {
        RecipeDescFragment fragment = new RecipeDescFragment(recipeModel);
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putString(ARG_DESCRIPTION, description);
        args.putInt(ARG_IMAGE_ID, imageResourceId);
        args.putInt(ARG_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    public RecipeDescFragment(RecipeModel recipeModel) {
        // Required empty public constructor
        rm1 = recipeModel;

    }

     @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         View rootView = inflater.inflate(R.layout.fragment_recipe_desc, container, false);

         // Get reference to the database
//         database = FirebaseDatabase.getInstance();
//         mDatabaseReference = database.getReference();

         // This holds the array list of recipe titles
         //TODO:
         // mTitles = getResources().getStringArray(R.array.book_titles);

         // Create a list of RecipeModel, which will be used to store
         // the contents of RecipeModel objects retrived from the database
         recipeList = new ArrayList<RecipeModel>();

         /**
          * Get the database
          * This method will be evoked any time the data on the database changes.
          */

//         mDatabaseReference.child("recipes").addValueEventListener(new ValueEventListener() {
//             //dataShot is the data at the point in time.
//             @Override
//             public void onDataChange(DataSnapshot dataSnapshot) {
//                 // Get reference to each of the recipe -unit as a collection
//                 // Get all the children at this level (alt+enter+v for auto fill)
//                 Iterable<DataSnapshot> allRecipes = dataSnapshot.getChildren();
//
//                 int i = 1;
//                 // Shake hands with each of the iterable
//                 for (DataSnapshot oneRecipe : allRecipes) {
//                     // Pull out the recipe as a java object
//                     RecipeModel oneRecipeContent = oneRecipe.getValue(RecipeModel.class);
//                     // Save the recipes to the recipelist
//                     recipeList.add(oneRecipeContent);
//                     Log.d("RETRIEVE receipe", "i="+ i + oneRecipeContent.description);
//                     ++i;
//                 }
//             }
//
//             @Override
//             public void onCancelled(DatabaseError databaseError) {
//
//                 Log.d ("RETRIEVE", " Could not retrieved the database");
//
//             }
//         });

         Log.d ("RETRIEVE", "onCreateView of recipe description()");


//         RecipeModel rm1 = recipeList.get(0);

         TextView recipeTitleView = (TextView)rootView.findViewById(R.id.recipeTitle);
         TextView recipeDescriptionView = (TextView)rootView.findViewById(R.id.recipeDescription);
         ImageView topImageView = (ImageView)rootView.findViewById(R.id.topImage);
         Button recipeBy = (Button) rootView.findViewById(R.id.recipe_by_button);
         TextView recipeIngredients = (TextView)rootView.findViewById(R.id.ingredients_textview);
         TextView timeDesp = (TextView) rootView.findViewById(R.id.time_desp_textview);
         Button startCooking = (Button) rootView.findViewById(R.id.start_cooking_button);
         TextView recipeDirections = (TextView) rootView.findViewById(R.id.directions_textview);
         Button addToShoppingList = (Button) rootView.findViewById(R.id.add_to_shoppinglist_button);

          Bundle args = getArguments();
         int position = args.getInt(ARG_POSITION);
//
//         recipeTitleView.setText(args.getString(ARG_TITLE));
         //recipeTitleView.setTransitionName("title_text_" + position);
//         recipeDescriptionView.setText(args.getString(ARG_DESCRIPTION));
          topImageView.setImageResource(args.getInt(ARG_IMAGE_ID));


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
//    public void startCooking (View view){
//        Intent intent = new Intent(RecipeDescFragment.this, NewRecipeActivity.class);
//        startActivity(intent);
//    }


}
