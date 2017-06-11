package sarojbardewa.com.cookhookpro.loginandsplashscreens;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

import sarojbardewa.com.cookhookpro.R;

public class ShoppingListActivity extends AppCompatActivity implements View.OnClickListener{
    private ListView mRecipeListView, mIngredientListView;
    private HashMap<String, Recipe> mRecipesInCart, mRecipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        Button b = (Button) findViewById(R.id.logout_button);
        b.setOnClickListener(this);

        mRecipeListView = (ListView) findViewById(R.id.ingredient_list_view);
        mIngredientListView = (ListView) findViewById(R.id.ingredient_list_view);

        mRecipes = GetRecipes();
        ArrayList<String> recipeNames = new ArrayList<>(mRecipes.keySet());

        ArrayAdapter<CheckBox> recipeBoxes = new CheckBoxArrayAdapter(getApplicationContext(), recipeNames);
        mRecipeListView.setAdapter(recipeBoxes);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.logout_button) {
            UserProfile.getInstance().LogoutAndGoToLoginScreen(this);
        }
    }

    private HashMap<String, Recipe> GetRecipes()
    {
        HashMap<String, Recipe> recipes = new HashMap<>();
        String recipeName = "Chicken and rice";
        ArrayList<String> ingredients = new ArrayList<>();
        ingredients.add("Chicken");
        ingredients.add("Rice");
        ingredients.add("Broccoli");
        ingredients.add("Cheese");

        recipes.put(recipeName, new Recipe(recipeName, ingredients));

        recipeName = "Spaghetti and meatballs";
        ArrayList<String> smIngredients = new ArrayList<>();
        smIngredients.add("Spaghetti");
        smIngredients.add("Meatballs");
        smIngredients.add("Marinara sauce");
        recipes.put(recipeName, new Recipe(recipeName, smIngredients));
        return recipes;
    }

}
