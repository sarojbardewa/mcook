package sarojbardewa.com.cookhookpro.ProfileUpdateActivity;

/**
 * Created by b on 6/13/17.
 */

public class ProfileUpload {
    public String name;
    public String location;
    public String favrec;
    public String dietary;
    public String url;

    public ProfileUpload(){

    }
    public ProfileUpload(String name, String location, String favrec, String dietary, String url) {
        this.name = name;
        this.location = location;
        this.favrec = favrec;
        this.dietary = dietary;
        this.url = url;
    }

    public String getName() {
        return name;
    }
    public String getLocation() {return location;}
    public String getFavrec() {return favrec; }
    public String getDietary() {return dietary;}
    public String getUrl() {
        return url;
    }


}
