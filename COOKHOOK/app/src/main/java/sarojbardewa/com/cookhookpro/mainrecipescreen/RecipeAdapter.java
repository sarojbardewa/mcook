package sarojbardewa.com.cookhookpro.mainrecipescreen;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import sarojbardewa.com.cookhookpro.R;

/**
 * This recipe adapter manages the recipe list and description fragments.
 * The main RecipeActivity calls this adapter to launch the fragment.
 * The RecipeListFragment implements a recycler view.
 * @author : Saroj Bardewa
 * @since  : May 29th, 2017
 */
public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {
    private String[] mTitles;
    private String [] mImageResourceIds;
    Context mContext;

    /**
     * Constructor of the fragment adapter
     * @param titles  - Title of the recipe list
     * @param imageResourceIds  - URL to the database image
     * @param context       - Context of the activity
     */
    public RecipeAdapter(String[] titles, String[] imageResourceIds, Context context) {
        mTitles = titles;
        mImageResourceIds = imageResourceIds;
        mContext = context;

    }

    /**
     *  This inflates the recycler view  for the recipe list
     * @param parent  - Parent activity
     * @param viewType - and int
     * @return - nul
     */
    @Override
    public RecipeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        View rootView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_card_view, parent, false);

        ViewHolder vh = new ViewHolder(rootView);
        return vh;
    }

    /**
     * For each recycler view object, add the image of the recipe and
     * its description
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTextView.setText(mTitles[position]);
        /**
         * Use Glide to display the recipe image.
         */
        Glide.with(mContext)
                .load(mImageResourceIds[position])
                .into(holder.mImageView);

    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mTitles.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;
        public ImageView mImageView;
        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView)v.findViewById(R.id.recipeTitle);
            mImageView = (ImageView)v.findViewById(R.id.topImage);
        }
    }


}