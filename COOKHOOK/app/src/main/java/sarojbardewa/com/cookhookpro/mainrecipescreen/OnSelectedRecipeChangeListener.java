package sarojbardewa.com.cookhookpro.mainrecipescreen;

import android.view.View;

import sarojbardewa.com.cookhookpro.newrecipe.RecipeModel;

/**
 * This is the interface that contains the recipe model, recipe index on the recipe
 * list and view.
 *  This interface is used to communicate between the RecipeListFragment and RecipeActivity
 */
public interface OnSelectedRecipeChangeListener {
    void onSelectedRecipeChanged(View view, int RecipeIndex, RecipeModel recipeModel);
}
