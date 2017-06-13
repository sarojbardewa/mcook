package sarojbardewa.com.cookhookpro.StxStDirActivity;

/**
 * Created by b on 6/12/17.
 */

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import sarojbardewa.com.cookhookpro.R;
import sarojbardewa.com.cookhookpro.newrecipe.RecipeModel;

/**
 * Created by b on 6/3/17.
 */

public class StxStDirActivity extends AppCompatActivity {
    private static final String EXTRA_DIRECTIONS_OBJECT =
            "com.placeholder.noname.cookhook.extra_Directions_object";

    private static RecipeModel rmModel;

    private TextView mDirectionsTextView;
    private Button mNext;
    private int mDirNum;
    private String mAddress;
    private String[] mAddresses;
    private int mNumRecipes;
    //private Recipe mRecipeBank = new Recipe();
    private int mRecipeNum = 0;
    private String mFileName;
    private int mNumDirections;




    public static void setRecipe (RecipeModel rmModel1){

        rmModel = rmModel1;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directions);
        final List<String> mDirections = rmModel.getDirections();




        //mRecipeBank.recipe(this, mFileName);

        //mDirections = mRecipeBank.getDirection();
        mDirNum = 0;
        mNumDirections = mDirections.size();
        mDirectionsTextView = (TextView) findViewById(R.id.directions);
        mDirectionsTextView.setText(mDirections.get(mDirNum));
        mNext = (Button) findViewById(R.id.btnNext);
        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                    //String [] directions = mRecipeBank.getDirection();
                   // mDirectionsTextView.setText(directions[mDirNum]);
                    mDirNum = (mDirNum + 1) % mNumDirections;

                    mDirectionsTextView.setText(mDirections.get(mDirNum));
                }

        });
    }

}
