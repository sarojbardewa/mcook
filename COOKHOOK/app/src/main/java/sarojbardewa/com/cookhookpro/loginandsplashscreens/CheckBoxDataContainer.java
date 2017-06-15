package sarojbardewa.com.cookhookpro.loginandsplashscreens;

/**
 * Created by Kyle on 6/13/2017.
 */

/**
 * @brief Data container class used to pass information to the CheckBoxArrayAdapter class.
 * Allows setting the default isChecked value of the checkboxes in the adapter.
 */
public class CheckBoxDataContainer
{
    public String text;
    public boolean isChecked;

    public CheckBoxDataContainer(String _text, boolean _isChecked)
    {
        text = _text;
        isChecked = _isChecked;
    }
}
