package sarojbardewa.com.cookhookpro.loginandsplashscreens;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import sarojbardewa.com.cookhookpro.R;

/**
 * Created by Kyle on 6/11/2017.
 */

public class CheckBoxArrayAdapter extends ArrayAdapter<CheckBox> {

    private ArrayList<CheckBoxDataContainer> mItems;
    private Context mContext;
    private View.OnClickListener mClickListener;
    public CheckBoxArrayAdapter(Context context, ArrayList<CheckBoxDataContainer> items, View.OnClickListener listener)
    {
        super(context, R.layout.checkbox_in_listview);
        mItems = items;
        mContext = context;
        mClickListener = listener;
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
        box.setText(mItems.get(position).text);
        box.setChecked(mItems.get(position).isChecked);
        box.setOnClickListener(mClickListener);
        return v;
    }

    @Override
    public int getCount()
    {
        return mItems.size();
    }
}
