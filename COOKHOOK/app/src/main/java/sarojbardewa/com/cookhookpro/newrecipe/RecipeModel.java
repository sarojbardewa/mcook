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

