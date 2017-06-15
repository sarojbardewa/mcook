package sarojbardewa.com.cookhookpro.mainrecipescreen;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import sarojbardewa.com.cookhookpro.R;
import sarojbardewa.com.cookhookpro.newrecipe.RecipeModel;

/**
 * This is the fragment that shows the list of recipes to the user.
 * @author : Saroj Bardewa
 * @since : May 29th, 2017
 */


public class RecipeListFragment extends Fragment {
    // Get access to the database
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mDatabaseReference = database.getReference();

    // Create variables to hold the fields retrieved from the database.
    List<RecipeModel> recipeList;
    String[] mTitles;
    String [] mImageResourceIds;


    // Instantiate the interface
    private OnSelectedRecipeChangeListener mListener;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    List <String> mTitlesList = new ArrayList<String>();
    List <String> mImageIDList = new ArrayList<String>();

    /**
     * Create the fragment with the provided arguments
     */
    public static RecipeListFragment newInstance() {
        RecipeListFragment fragment = new RecipeListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    public RecipeListFragment() {
        // Required empty public constructor
    }

    /**
     * This is the method that communicates with the database
     * everytime it is evoked. This way all the recipe data
     * is in sync and inflate the layout in a recycler view.
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get reference to the database
        database = FirebaseDatabase.getInstance();
        mDatabaseReference = database.getReference();

        // Create a list of RecipeModel, which will be used to store
        // the contents of RecipeModel objects retrived from the database
         recipeList = new ArrayList<RecipeModel>();

         // Get the database
         // This method will be evoked any time the data on the database changes.
        mDatabaseReference.child("recipes").addValueEventListener(new ValueEventListener() {
            //dataShot is the data at the point in time.
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get reference to each of the recipe -unit as a collection
                // Get all the children at this level (alt+enter+v for auto fill)
                Iterable<DataSnapshot> allRecipes = dataSnapshot.getChildren();

                // Shake hands with each of the iterable
                for (DataSnapshot oneRecipe : allRecipes) {
                    // Pull out the recipe as a java object
                    RecipeModel oneRecipeContent = oneRecipe.getValue(RecipeModel.class);
                    // Save the recipes to the recipelist
                    recipeList.add(oneRecipeContent);
                    mTitlesList.add(oneRecipeContent.name);
                    mImageIDList.add(oneRecipeContent.getImageUrl());

//                    Log.d("RETRIEVE mTitles", "i="+oneRecipeContent.description);
                }
                mTitles = mTitlesList.toArray(new String[0]);

                // Convert the mImageResourceIds to the string of arrays of URL's. Note
                // that the adapter will use string of Image URL's
                mImageResourceIds = mImageIDList.toArray(new String [0]);

                // Pass the information to the adapter
                mAdapter = new RecipeAdapter(mTitles, mImageResourceIds, getActivity());
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

               Log.d ("RETRIEVE", " Could not retrieved the database");

            }
        });

        Log.d ("RETRIEVE", "onCreate_end()");


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        container.removeAllViews();  // Remove all paste fragments if any
        View rootView = inflater.inflate(R.layout.fragment_recipe_card, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recipe_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(),
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        //Toast.makeText(getActivity(), mTitles[position], Toast.LENGTH_SHORT).show();

                        // Give back the position of the item selected
                        mListener.onSelectedRecipeChanged(view, position,recipeList.get(position));
                    }
                }));

        return rootView;
    }

    /**
     * This method is called once the fragment is associated with its activity.
     * @param activity
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.i("FRAGMENT", "RecipeListFragment onAttach() called");

        try {
            mListener = (OnSelectedRecipeChangeListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    /**
     * This method is called immediately prior to the fragment no longer
     * being associated with its activity.
     */

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i("FRAGMENT", "RecipeListFragment onDetach() called");
        mListener = null;

    }

}
