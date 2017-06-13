package sarojbardewa.com.cookhookpro.newrecipe;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Saroj on 6/5/17.
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

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public String getCooktime() {
        return cooktime;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public List<String> getDirections() {
        return directions;
    }
}

