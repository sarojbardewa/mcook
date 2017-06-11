package sarojbardewa.com.cookhookpro.newrecipe;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import sarojbardewa.com.cookhookpro.R;

public class DirectionsListActivity extends AppCompatActivity {
    ArrayList<String> directionList = null;
    ArrayAdapter<String> adapter = null;
    ListView lv = null;
    private static final String  TAG = "DirectionsListActivity";
    private static final String EXTRA_DIRECTIONS = "edu.pdx.ece558sp17group3.ingredientlist.directions";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Directions To Cook");
        setContentView(R.layout.activity_ingredient_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        directionList = getArrayVal(getApplicationContext());

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, directionList);
        lv = (ListView) findViewById(R.id.listView);
        lv.setAdapter(adapter);

        // When we click the list, we can delete it

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View view, final int position, long id) {
                String selectedItem = ((TextView) view).getText().toString();
                if (selectedItem.trim().equals(directionList.get(position).trim())) {
                    removeElement(selectedItem, position);
                } else {
                    Toast.makeText(getApplicationContext(), "Error Removing Element", Toast.LENGTH_LONG).show();
                }
            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onChageItemList(-1);
            }
        });

        setReturnDirectionsList(directionList);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ingredient_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        /**
         * WHen the action_add is clicked, user
         */
//        if (id==R.id.action_add){
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setTitle("Add Item");
//            final EditText input = new EditText(this);
//            builder.setView(input);
//            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    directionList.add(preferredCase(input.getText().toString()));
//                    Collections.sort(directionList);
//                    // Store the store values in the memory
//                    storeArrayVal(directionList, getApplicationContext());
//                    lv.setAdapter(adapter);
//                }
//            });
//            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.cancel();
//                }
//            });
//            builder.show();
//            return true;
//        }

        if (id == R.id.action_clear) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Clear Entire List");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    directionList.clear();
                    lv.setAdapter(adapter);
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();
        }

        return super.onOptionsItemSelected(item);
    }

    public static void storeArrayVal(ArrayList<String> inArrayList, Context context) {
        // Set is a collection
        Set<String> WhatToWrite = new HashSet<String>(inArrayList);
        // Save the settings
        SharedPreferences WordSearchPutPrefs = context.getSharedPreferences("myDirectionsValues", Activity.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = WordSearchPutPrefs.edit();
        prefEditor.putStringSet("myDirections", WhatToWrite);
        prefEditor.commit();
    }

    public static ArrayList getArrayVal(Context thisContext) {
        // Get back the list that we saved
        SharedPreferences WordSearchGetPrefs = thisContext.getSharedPreferences("myDirectionsValues", Activity.MODE_PRIVATE);
        Set<String> tempSet = new HashSet<String>();
        tempSet = WordSearchGetPrefs.getStringSet("myDirections", tempSet);
        return new ArrayList<String>(tempSet);
    }

    // Remove the shopping list element
    public void removeElement(String selectedItem, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit or delete this item ?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                directionList.remove(position);
                storeArrayVal(directionList, getApplicationContext());
                lv.setAdapter(adapter);
            }
        });
        builder.setNegativeButton("Edit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onChageItemList(position);

            }
        });

        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }

        });

        builder.show();
    }

    /**
     * In this on ChangeItemList, we will first check, whether the user
     * wants to edit the item already in the list or add a new item. This
     * decision will be provided by the "position" in parameter. If position < 0,
     * will indicate that user is only interested to add a new item. If position >=0,
     * user wants to edit the fields already in the list. So, we'll have to
     * parse the fields and allow user to edit the items.
     * @param position - Index of an item in the list
     */

    public void onChageItemList(final int position){
        String[] mWords;
        String mStepField = "";
        String mDirectionField = "";
        Context context = DirectionsListActivity.this;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        if(position < 0)
            builder.setTitle("Add Item");
        else {

            builder.setTitle("Edit Item");
            String textContent= directionList.get(position);
            // Split the string based on whitespace
            if (textContent !=null) {
                mWords = textContent.split("\\s");//splits the string based on whitespace
                mStepField = mWords[0];
                // If the item field is more than one word, concatenate
                // all the remaining words
                for (int i = 1; i < mWords.length; i++) {
                    mDirectionField = mDirectionField + mWords[i] + " ";
                }
            }
        }


        // The multiple fields will be wrapped in a linear layout.
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        // The following are the fields in the dialogbox
        final EditText mStep = new EditText(context);
        //mStep.setBackground(ContextCompat.getDrawable(context, R.drawable.style_edittext));
        final EditText mDirection = new EditText(context);
        mDirection.setBackground(ContextCompat.getDrawable(context, R.drawable.style_edittext));

        // If user is adding a new item, show hints, else display the items
        // that already exit
        if(position <0) {
            mStep.setHint("Step 1/2/3...");
            mDirection.setHint("Direction");
        } else {
                mStep.setText(mStepField);
                mDirection.setText(mDirectionField);
            }

        layout.addView(mStep);
        layout.addView(mDirection);
        builder.setView(layout);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (position >= 0) {
                    // If user just edited the text, go ahead and remove the item from the shopping
                    // list first
                    directionList.remove(position);
                }
                // If user missed to enter any of the fields, exit with a try again toast.
                if (mStep.getText().toString().isEmpty()
                        || mDirection.getText().toString().isEmpty()) {
                    Toast.makeText(DirectionsListActivity.this, "None of the fields can be empty. Try again", Toast.LENGTH_SHORT).show();
                } else {
                    directionList.add(mStep.getText().toString() + " " +
                             mDirection.getText().toString());

                    Collections.sort(directionList);
                    // Store the store values in the memory
                    storeArrayVal(directionList, getApplicationContext());
                    lv.setAdapter(adapter);
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        //Rotation kills the dialog. Make a fix if time permitted.
        Toast.makeText(DirectionsListActivity.this,"Android internal configuration changed..", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Log.d(TAG, "IngredientListActivity onBackPressed() called");
        setReturnDirectionsList(directionList);
    }

    /**
     * Decode the parent intent
     * @param result
     * @return
     */

    public static List<String> getDirections (Intent result) {
        return result.getStringArrayListExtra(EXTRA_DIRECTIONS);
    }

    /**
     *  This starts an intent to pass result back to its calling
     *  activity
     * @param mIngredientList - Array of ingrediant list
     */
    private  void setReturnDirectionsList(ArrayList<String> mIngredientList){
        Intent data = new Intent();
        data.putExtra(EXTRA_DIRECTIONS, mIngredientList);
        setResult(RESULT_OK,data);
    }

}
