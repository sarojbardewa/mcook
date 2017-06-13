package sarojbardewa.com.cookhookpro.shoppinglist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import sarojbardewa.com.cookhookpro.newrecipe.RecipeModel;

/**
 * Created by Kyle on 6/11/2017.
 */

public class ShoppingList {
    private static final ShoppingList ourInstance = new ShoppingList();
    private List<RecipeModel> mRecipesInList;

    public static ShoppingList getInstance() {
        return ourInstance;
    }

    private ShoppingList() {
        mRecipesInList = new ArrayList<>();
    }

    public void AddRecipe(RecipeModel recipe)
    {
        if(!IsRecipeAlreadyInList(recipe)) {
            mRecipesInList.add(recipe);
        }
    }

    public void RemoveRecipeFromList(RecipeModel toRemove)
    {
        for(int i = 0; i < mRecipesInList.size(); i++)
        {
            if(toRemove.equals(mRecipesInList.get(i)))
            {
                mRecipesInList.remove(i);
                break;
            }
        }
    }

    public List<RecipeModel> GetRecipesInShoppingList()
    {
        return mRecipesInList;
    }

    public List<String> GetAllIngredientsForList()
    {
        List<String> ingredients = new ArrayList<>();
        for(RecipeModel recipe : mRecipesInList)
        {
            ingredients.addAll(recipe.getIngredients());
        }
        return ingredients;
    }

    private boolean IsRecipeAlreadyInList(RecipeModel newRecipe)
    {
        for(RecipeModel recipe : mRecipesInList)
        {
            if(newRecipe.equals(recipe))
            {
                return true;
            }
        }
        return false;
    }
}
