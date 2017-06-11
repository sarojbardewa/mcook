package sarojbardewa.com.cookhookpro.loginandsplashscreens;

import java.util.ArrayList;

/**
 * Created by Kyle on 6/11/2017.
 */

public class Recipe {
    private ArrayList<String> mIngredients;
    private ArrayList<String> mInstructions;
    private int mCookTimeMinutes;
    private String mName;

    public Recipe(String name, ArrayList<String> ingredients)
    {
        mName = name;
        mIngredients = ingredients;
    }

    public String GetName() { return mName; }
    public ArrayList<String> GetIngredients() { return mIngredients; }
}
