package sarojbardewa.com.cookhookpro.newrecipe;

import java.io.Serializable;
import java.util.List;

/**
 * This is a model class that stores all the fields of
 * a recipe.
 * @author Saroj
 * @since 6/5/17.
 */

public class RecipeModel implements Serializable{
    public String name;
    public String imageUrl;
    public String description;
    public String cooktime;
    public List <String> ingredients;
    public List<String> directions;
    public String userID;

    public RecipeModel() {
        // Empty Constructor
    }

    /**
     * None empty constructor that saves the
     * contents of model fields
     * @param name          - Name of the recipe
     * @param url           - URL of the recipe image
     * @param description   - description of the recipe
     * @param timeTaken     - time taken to make the recipe
     * @param ingredientList - List of ingredients
     * @param directionList     - List of cooking directions
     * @param userID            - UserID created during login
     */
    public RecipeModel(String name,
                       String url,
                       String description,
                       String timeTaken,
                       List<String> ingredientList,
                       List<String> directionList,
                       String userID) {
        this.name = name;
        this.imageUrl = url;
        this.description = description;
        this.cooktime = timeTaken;
        this.ingredients = ingredientList;
        this.directions = directionList;
        this.userID = userID;
    }

    /**
     * Getter of recipe title
     * @return - Recipe title
     */
    public String getName() {
        return name;
    }

    /**
     * Getter of recipe image URL saved on storage
     * @return - Recipe URL
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * Getter of cooking description
     * @return - Recipe description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Getter of cooktime
     * @return - Cook time
     */
    public String getCooktime() {
        return cooktime;
    }

    /**
     * Getter of the list of ingredients
     * @return - list of string
     */
    public List<String> getIngredients() {
        return ingredients;
    }

    /**
     * Getter of the list of directions
     * @return - list of strings
     */
    public List<String> getDirections() {
        return directions;
    }


    /**
     * Method to check if an object is of type RecipeModel
     * @param o - Passed object
     * @return - true or false
     */
    @Override
    public boolean equals(Object o)
    {
        if(o instanceof RecipeModel)
        {
            RecipeModel other = (RecipeModel) o;
            if(!other.name.equals(name) ||
                    !other.imageUrl.equals(imageUrl) ||
                    !other.cooktime.equals(cooktime) ||
                    !other.description.equals(description) ||
                    !other.userID.equals(userID) ||
                    (other.ingredients.size() != ingredients.size()) ||
                    (other.directions.size() != directions.size()))
            {
                return false;
            }
            for(int i = 0; i < directions.size(); ++i)
            {
                if(other.directions.get(i) != directions.get(i))
                    return false;
            }
            for(int i = 0; i < ingredients.size(); ++i)
            {
                if(other.ingredients.get(i) != ingredients.get(i))
                    return false;
            }
            return true;
        }
        return false;
    }
}

