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
 * Created by Jim on 12/29/2015.
 */
public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {
    private String[] mTitles;
    private String [] mImageResourceIds;
    Context mContext;

    public RecipeAdapter(String[] titles, String[] imageResourceIds, Context context) {
        mTitles = titles;
        mImageResourceIds = imageResourceIds;
        mContext = context;

    }

    @Override
    public RecipeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        View rootView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_card_view, parent, false);

        ViewHolder vh = new ViewHolder(rootView);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTextView.setText(mTitles[position]);
       // holder.mTextView.setTransitionName("title_text_" + position);
        /**
         * Use Glide to display the recipe image.
         */
        Glide.with(mContext)
                .load(mImageResourceIds[position])
                .into(holder.mImageView);

       // holder.mImageView.setTransitionName("recipe_image_" + position);

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