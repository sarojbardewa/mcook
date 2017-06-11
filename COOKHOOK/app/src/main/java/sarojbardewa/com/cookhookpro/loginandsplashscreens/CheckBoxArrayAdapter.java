package sarojbardewa.com.cookhookpro.loginandsplashscreens;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;

import java.util.ArrayList;

import sarojbardewa.com.cookhookpro.R;

/**
 * Created by Kyle on 6/11/2017.
 */

public class CheckBoxArrayAdapter extends ArrayAdapter<CheckBox> {
    private ArrayList<String> mItems;
    private Context mContext;
    public CheckBoxArrayAdapter(Context context, ArrayList<String> items)
    {
        super(context, R.layout.checkbox_in_listview);
        mItems = items;
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.checkbox_in_listview, null);
        }
        CheckBox box = ((CheckBox) v.findViewById(R.id.listview_checkbox));
        box.setText(mItems.get(position));
        return v;
    }

    @Override
    public int getCount()
    {
        return mItems.size();
    }
}
