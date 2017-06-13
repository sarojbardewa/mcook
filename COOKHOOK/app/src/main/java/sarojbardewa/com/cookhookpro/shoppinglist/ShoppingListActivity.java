package sarojbardewa.com.cookhookpro.shoppinglist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import sarojbardewa.com.cookhookpro.R;
import sarojbardewa.com.cookhookpro.loginandsplashscreens.CheckBoxArrayAdapter;
import sarojbardewa.com.cookhookpro.loginandsplashscreens.CheckBoxDataContainer;
import sarojbardewa.com.cookhookpro.loginandsplashscreens.Recipe;
import sarojbardewa.com.cookhookpro.loginandsplashscreens.UserProfile;
import sarojbardewa.com.cookhookpro.newrecipe.RecipeModel;

public class ShoppingListActivity extends AppCompatActivity {
    private ListView mRecipeListView, mIngredientListView;
    private HashMap<String, RecipeModel> mRecipesInCart, mRecipes;
    private HashMap<String, Boolean> mIngredientsChecked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);
        mRecipesInCart = new HashMap<>();
        mIngredientsChecked = new HashMap<>();

        mRecipeListView = (ListView) findViewById(R.id.recipe_list_view);
        mIngredientListView = (ListView) findViewById(R.id.ingredient_list_view);

        GetRecipeList();
    }

    private void UpdateIngredientList()
    {
        ArrayList<CheckBoxDataContainer> ingredients = new ArrayList<>();
        for(RecipeModel recipe : mRecipesInCart.values())
        {
            for(String ingredient : recipe.getIngredients())
            {
                boolean isChecked = mIngredientsChecked.containsKey(ingredient) && mIngredientsChecked.get(ingredient);
                ingredients.add(new CheckBoxDataContainer(ingredient, isChecked));
            }
        }
        ArrayAdapter<CheckBox> ingredientBoxes = new CheckBoxArrayAdapter(getApplicationContext(), ingredients, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox box = (CheckBox) v;
                String ingredient = box.getText().toString();
                mIngredientsChecked.put(ingredient, box.isChecked());
            }
        });
        mIngredientListView.setAdapter(ingredientBoxes);
    }

    private void GetRecipeList()
    {
        mRecipes = GetRecipes();
        ArrayList<CheckBoxDataContainer> recipeNames = new ArrayList<>();
        for(String recipeName : mRecipes.keySet())
        {
            recipeNames.add(new CheckBoxDataContainer(recipeName, false));
        }
        ArrayAdapter<CheckBox> recipeBoxes = new CheckBoxArrayAdapter(getApplicationContext(), recipeNames, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox box = (CheckBox) v;
                String recipeName = box.getText().toString();
                if (box.isChecked()) {
                    if (!mRecipesInCart.containsKey(recipeName)) {
                        mRecipesInCart.put(recipeName, mRecipes.get(recipeName));
                    }
                } else {
                    if (mRecipesInCart.containsKey(recipeName)) {
                        mRecipesInCart.remove(recipeName);
                    }
                    for(String ingredient : mRecipes.get(recipeName).getIngredients())
                    {
                        if(mIngredientsChecked.containsKey(ingredient))
                        {
                            mIngredientsChecked.remove(ingredient);
                        }
                    }
                }
                UpdateIngredientList();
            }
        });
        mRecipeListView.setAdapter(recipeBoxes);
    }

    private HashMap<String, RecipeModel> GetRecipes()
    {
        HashMap<String, RecipeModel> recipes = new HashMap<>();
        List<RecipeModel> recipesFromShoppingList = ShoppingList.getInstance().GetRecipesInShoppingList();
        for(RecipeModel recipe : recipesFromShoppingList)
        {
            recipes.put(recipe.getName(), recipe);
        }
        return recipes;
    }

}
