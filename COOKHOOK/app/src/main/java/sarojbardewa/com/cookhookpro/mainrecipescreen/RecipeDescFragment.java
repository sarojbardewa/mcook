package sarojbardewa.com.cookhookpro.mainrecipescreen;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import sarojbardewa.com.cookhookpro.R;


public class RecipeDescFragment extends Fragment {

    private static final String ARG_TITLE = "title";
    private static final String ARG_DESCRIPTION = "description";
    private static final String ARG_IMAGE_ID = "image id";
    private static final String ARG_POSITION = "position";


    public static RecipeDescFragment newInstance(String title, String description,
                                                 int imageResourceId, int position) {
        RecipeDescFragment fragment = new RecipeDescFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putString(ARG_DESCRIPTION, description);
        args.putInt(ARG_IMAGE_ID, imageResourceId);
        args.putInt(ARG_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    public RecipeDescFragment() {
        // Required empty public constructor
    }

     @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         View rootView = inflater.inflate(R.layout.fragment_recipe_desc, container, false);

         TextView recipeTitleView = (TextView)rootView.findViewById(R.id.recipeTitle);
         TextView recipeDescriptionView = (TextView)rootView.findViewById(R.id.recipeDescription);
         ImageView topImageView = (ImageView)rootView.findViewById(R.id.topImage);

         Bundle args = getArguments();
         int position = args.getInt(ARG_POSITION);

         recipeTitleView.setText(args.getString(ARG_TITLE));
         //recipeTitleView.setTransitionName("title_text_" + position);
         recipeDescriptionView.setText(args.getString(ARG_DESCRIPTION));
         topImageView.setImageResource(args.getInt(ARG_IMAGE_ID));
         //topImageView.setTransitionName("book_image_" + position);

         return rootView;
    }

}
