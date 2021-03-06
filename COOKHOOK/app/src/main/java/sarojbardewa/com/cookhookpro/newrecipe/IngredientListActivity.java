package sarojbardewa.com.cookhookpro.newrecipe;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import sarojbardewa.com.cookhookpro.R;

/**
 * This is the class that allows users to add ingredients
 * on the new recipe they are creating.
 * @author Saroj Bardewa
 * @since May 29th, 2017
 */

public class IngredientListActivity extends AppCompatActivity {
    ArrayList<String> ingredientList = null;
    ArrayAdapter<String> adapter = null;
    ListView lv = null;
    private static final String  TAG = "IngredientListActivity";
    private static final String EXTRA_INGREDLIST = "edu.pdx.ece558sp17group3.ingredientlist.extra";

    /**
     * In the oncreate method, inflate the layout and retrieve data
     * if saved earlier
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Ingredient List");

        setContentView(R.layout.activity_ingredient_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ingredientList = getArrayVal(getApplicationContext());

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, ingredientList);
        lv = (ListView) findViewById(R.id.listView);
        lv.setAdapter(adapter);

        // When we click the list, we can delete it

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View view, final int position, long id) {
                String selectedItem = ((TextView) view).getText().toString();
                if (selectedItem.trim().equals(ingredientList.get(position).trim())) {
                    // If user no longer would like to keep the ingredients they added
                    // to the list, they can clear it all by calling this method.
                    removeElement(selectedItem, position);
                } else {
                    Toast.makeText(getApplicationContext(), "Error Removing Element", Toast.LENGTH_LONG).show();
                }
            }
        });


        /**
         * This is the floating button that will prompt user to add new ingredents with
         * quantity, unit and description fields when clicked.
         */

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onChageItemList(-1);
            }
        });

        // Set the value to be returned to the parent
        setReturnIngrediantList(ingredientList);
    }

    // Inflate the layout
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

        // If the clear action back is selected, we can give option for them
        // to confirm if they want to delete before deleting.
        // A dialog box is shown for that purpose.

        if (id == R.id.action_clear) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Clear Entire List");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ingredientList.clear();
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

    /**
     * This is the method that allows user to save their ingredients without
     * loss. For instance, a user might wasnt to come back and edit their ingredient
     * list once created.
     * @param inArrayList
     * @param context
     */
    public static void storeArrayVal(ArrayList<String> inArrayList, Context context) {
        // Set is a collection
        Set<String> WhatToWrite = new HashSet<String>(inArrayList);
        // Save the settings
        SharedPreferences WordSearchPutPrefs = context.getSharedPreferences("dbArrayValues", Activity.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = WordSearchPutPrefs.edit();
        prefEditor.putStringSet("myArray", WhatToWrite);
        prefEditor.commit();
    }

    public static ArrayList getArrayVal(Context thisContext) {
        // Get back the list that we saved
        SharedPreferences WordSearchGetPrefs = thisContext.getSharedPreferences("dbArrayValues", Activity.MODE_PRIVATE);
        Set<String> tempSet = new HashSet<String>();
        tempSet = WordSearchGetPrefs.getStringSet("myArray", tempSet);
        return new ArrayList<String>(tempSet);
    }

    // Remove the shopping list element
    public void removeElement(String selectedItem, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit or delete this item ?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ingredientList.remove(position);
                storeArrayVal(ingredientList, getApplicationContext());
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
        String[] mWords = null;
        String mQuantityField = "";
        String mUnitsField = "";
        String mItemField = "";
        Context context = IngredientListActivity.this;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        if(position < 0)
            builder.setTitle("Add Item");
        else {

            builder.setTitle("Edit Item");
            String textContent= ingredientList.get(position);
            // Split the string based on whitespace
            if (textContent !=null) {
                mWords = textContent.split("\\s");//splits the string based on whitespace
                mQuantityField = mWords[0];
                mUnitsField = mWords[1];

                // If the item field is more than one word, concatenate
                // all the remaining words
                for (int i = 2; i < mWords.length; i++) {
                    mItemField = mItemField + mWords[i] + " ";
                }
            }
        }


        // The multiple fields will be wrapped in a linear layout.
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        // The following are the fields in the dialogbox
        final EditText mQuantity = new EditText(context);
        final EditText mUnits = new EditText(context);
        final EditText mItem = new EditText(context);
        mItem.setBackground(ContextCompat.getDrawable(context, R.drawable.style_edittext));

        // If user is adding a new item, show hints, else display the items
        // that already exit
        if(position <0) {
            mQuantity.setHint("Quantity Eg. 1/2,3 or na ");
            mUnits.setHint("Units Eg. ml/lbs/cups or na");
            mItem.setHint("Item Eg.cheese/egg ");
        } else {
                mQuantity.setText(mQuantityField);
                mUnits.setText(mUnitsField);
                mItem.setText(mItemField);
            }

        layout.addView(mQuantity);
        layout.addView(mUnits);
        layout.addView(mItem);

        builder.setView(layout);

        // When the okay button is pressed, based on whether
        // te user click on edit text or delete, take the respective action

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (position >= 0) {
                    // If user just edited the text, go ahead and remove the item from the shopping
                    // list first
                    ingredientList.remove(position);
                }
                // If user missed to enter any of the fields, exit with a try again toast.
                if (mQuantity.getText().toString().isEmpty() || mUnits.getText().toString().isEmpty()
                        || mItem.getText().toString().isEmpty()) {
                    Toast.makeText(IngredientListActivity.this, "None of the fields can be empty. Try again", Toast.LENGTH_SHORT).show();
                } else {
                    ingredientList.add(mQuantity.getText().toString() + " " +
                            mUnits.getText().toString() + " " + mItem.getText().toString());

                    //Collections.sort(ingredientList);
                    // Store the store values in the memory
                    storeArrayVal(ingredientList, getApplicationContext());
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



    /**
     *  This starts an intent to pass result back to its calling
     *  activity
     * @param mIngredientList - Array of ingrediant list
     */
    private  void setReturnIngrediantList(ArrayList<String> mIngredientList){
        Intent data = new Intent();
        data.putExtra(EXTRA_INGREDLIST, mIngredientList);
        setResult(RESULT_OK,data);
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Log.d(TAG, "IngredientListActivity onBackPressed() called");
        setReturnIngrediantList(ingredientList);
    }

    /**
     * Decode the parent intent
     * @param result
     * @return
     */

    public static List<String> getIngrediants (Intent result) {
        return result.getStringArrayListExtra(EXTRA_INGREDLIST);
    }



}
