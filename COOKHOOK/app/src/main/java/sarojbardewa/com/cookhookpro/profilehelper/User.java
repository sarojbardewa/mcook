package sarojbardewa.com.cookhookpro.profilehelper;

/**
 * Created by b on 6/11/17.
 */

import com.google.firebase.database.IgnoreExtraProperties;



@IgnoreExtraProperties
public class User {

    public String mName;
    public String mEmail;
    public String profileName;
    public String profileLocation;
    public String profileFavRecipe;
    public String profileDietaryRes;
    public String profileimURI;
    // Default constructor required for calls to

    public User(){

    }
    public User(String name, String email) {
        setUser(name);
        setEmail(email);
        //DataSnapshot.getValue(User.class);
    }

    public void setUser(String name) {
        this.mName = name;

    }
    public void setEmail(String email){
        this.mEmail = email;
    }
    public void setProfileName(String profilename){
        this.profileName = profilename;
    }
    public void setProfileLocation(String profilelocation){
        this.profileLocation = profilelocation;
    }
    public void setProfileFavRecipe(String profilefavrecipe){
        this.profileFavRecipe = profilefavrecipe;
    }
    public void setProfileDietaryRes(String profiledietaryRes){
        this.profileDietaryRes = profiledietaryRes;
    }
    public void setProfileimURI(String imuri){
        this.profileimURI = imuri;
    }

}


