package sarojbardewa.com.cookhookpro.mainrecipescreen;

import android.view.View;

import sarojbardewa.com.cookhookpro.newrecipe.RecipeModel;

/**
 * Created by Jim on 11/24/2015.
 */
public interface OnSelectedBookChangeListener {
    void onSelectedBookChanged(View view, int bookIndex, RecipeModel recipeModel);
}
