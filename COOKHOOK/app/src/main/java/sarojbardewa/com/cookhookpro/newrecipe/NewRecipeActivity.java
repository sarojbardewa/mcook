package sarojbardewa.com.cookhookpro.newrecipe;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.firebase.auth.FirebaseAuth; // Needed to get current user ID.

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import sarojbardewa.com.cookhookpro.R;

import static android.util.Log.i;
import static sarojbardewa.com.cookhookpro.R.id.imagename_edittext;


public class NewRecipeActivity extends AppCompatActivity {
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private ImageView mImageView;
    private EditText mRecipeName;
    private EditText mTotaltime;
    private EditText mDescription;
    private List<String> ingredientList;
    private List<String > directionList;


    private Uri imgUri;
    private ProgressDialog dialog;
    String myUserId;

    private static final String TAG ="NewRecipeActivity";

    private static final String KEY_INDEX = "all_data";

    public static final String STORAGE_PATH = "image/";
    public static  final  int REQUEST_CODE = 000;
    private static final int REQUEST_INGREDIENT_CODE = 100;
    private static final int REQUEST_DIRECTIONS_CODE = 200;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Add New Recipe");
        setContentView(R.layout.activity_new_recipe);
        mImageView = (ImageView)findViewById(R.id.imageView);
        mRecipeName = (EditText) findViewById(imagename_edittext);
        mTotaltime = (EditText)findViewById(R.id.time_edittext);
        mDescription = (EditText)findViewById(R.id.description_edittext);
        ingredientList = new ArrayList<>();

        // Get current user ID
        myUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.d(TAG,"CurrentUserID :" + myUserId);
        directionList = new ArrayList<>();
        //Get the uri if saved before and display the image
        if(savedInstanceState !=null){
            imgUri = savedInstanceState.getParcelable(KEY_INDEX);
            try{
                Bitmap bm = MediaStore.Images.Media.getBitmap(getContentResolver(), imgUri);
                mImageView.setImageBitmap(bm);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

    }

    public void onAddImageButtonClick(View v) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Image"),REQUEST_CODE);
    }

    /**
     * This method is called after a child activity returns back to the parent activity
     * The returned child is identified first and it's data is retrieved
     * @param requestCode - Request code
     * @param resultCode  - Code that identifies which activity was returned
     * @param data - Data returned by the activity
     */

    @Override
    protected void  onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        i(TAG, "onActivityResult() RESULT_OK " + Integer.toString(RESULT_OK));
        i(TAG, "onActivityResult() RESULT_CANCELED " + Integer.toString(RESULT_CANCELED));

        if(requestCode==REQUEST_CODE && resultCode == RESULT_OK
                && data !=null && data.getData() !=null){
            Log.d(TAG, " onActivityResult() ImageUri called() ");
            i(TAG, "onActivityResult() imgUri requestCode" + Integer.toString(requestCode));
            i(TAG, "onActivityResult() imgUri resultCode " + Integer.toString(resultCode));
            imgUri = data.getData();

            try{
                InputStream inputStream = getContentResolver().openInputStream(imgUri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                mImageView.setImageBitmap(bitmap);
//                Bitmap bm = MediaStore.Images.Media.getBitmap(getContentResolver(), imgUri);
//                mImageView.setImageBitmap(bm);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        // Get the ingrediants
        if(requestCode==REQUEST_INGREDIENT_CODE && resultCode== RESULT_OK ){

            if (data !=null) {
                ingredientList = IngredientListActivity.getIngrediants(data);
                Log.d(TAG, "onActivityResult() Ingredient list data not null");
                i(TAG, "onActivityResult() Ingredient requestCode" + Integer.toString(requestCode));
                i(TAG, "onActivityResult() Ingredient resultCode " + Integer.toString(resultCode));
            } else
            Log.d(TAG, " onActivityResult() Ingredient list  is empty");
        }

        // Get the directions
        if(requestCode==REQUEST_DIRECTIONS_CODE && resultCode== RESULT_OK ){

            if (data !=null) {
                directionList = DirectionsListActivity.getDirections(data);
                Log.d(TAG, "onActivityResult() directions list data not null");
                i(TAG, "onActivityResult() directions requestCode" + Integer.toString(requestCode));
                i(TAG, "onActivityResult() directions resultCode " + Integer.toString(resultCode));
            } else
                Log.d(TAG, " onActivityResult() directions list  is empty");

        }
    }


    public String getImageExt (Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }


    /**
     * For the Publish recipe button
     */
    @SuppressWarnings("VisibleForTests")
    public void onPublishRecipeClick( View view) {
        if (imgUri != null &&
            !mRecipeName.getText().toString().isEmpty() &&
            !mDescription.getText().toString().isEmpty() &&
            !mTotaltime.getText().toString().isEmpty() &&
            !ingredientList.isEmpty() &&
            !directionList.isEmpty()) {
            dialog = new ProgressDialog(this);
            dialog.setTitle("Uploading Recipe");
            dialog.show();


        // Get teh storage reference
        StorageReference sRef = mStorageRef.child(STORAGE_PATH +
                System.currentTimeMillis() + "." + getImageExt(imgUri));
        // Add file to reference
        sRef.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                // Dismiss the dialog
                dialog.dismiss();



        // Display success toast messgae
        Toast.makeText(getApplicationContext(), " Recipe Upload Successful", Toast.LENGTH_SHORT).show();

        RecipeModel recipeModel = new RecipeModel(mRecipeName.getText().toString(),
                taskSnapshot.getDownloadUrl().toString(),
                mDescription.getText().toString(),
                mTotaltime.getText().toString(),
                ingredientList,
                directionList,
                myUserId);

                String uploadID = mDatabaseRef.push().getKey();
                mDatabaseRef.child("recipes").child(uploadID).setValue(recipeModel);
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {

            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                // Show upload progress
                double progress = (100 * taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                dialog.setMessage("Uploaded "+ (int)progress+ "");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Dismiss the dialog
                dialog.dismiss();

                // Display success toast messgae
                Toast.makeText(getApplicationContext(),e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        } else {
            String message = "";
            if (imgUri == null) {
                message = " <select an image>";
            }
            if (mRecipeName.getText().toString().isEmpty()){
                message = message + " <enter a recipe name>";
            }
            if (mDescription.getText().toString().isEmpty()){
                message = message + " <enter a description>";

            } if (mTotaltime.getText().toString().isEmpty() ){
                message = message + " <give an estimated time>";

            }if (ingredientList.isEmpty()) {
                message = message + " <provide ingredients>";

            }if (directionList.isEmpty()) {
                message = message + " <provide directions to cook>";

            }

            // Display success toast messgage
            Toast.makeText(getApplicationContext(), "Please do :"+ message, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Add ingredients
     */

    public void addIngredients(View v){
        Intent intent = new Intent(this, IngredientListActivity.class);
        startActivityForResult(intent, REQUEST_INGREDIENT_CODE);

    }


    public void addDirections(View v){
        i("RECIPE","addDirections() called");
        Intent intent = new Intent(this, DirectionsListActivity.class);
        startActivityForResult(intent, REQUEST_DIRECTIONS_CODE);

    }

    /**
     * Override the onSaveInstanceState, needed while user changes the orientation.
     * Without this method, if user was adding a recipe image, it would be lost
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelable(KEY_INDEX,imgUri);  // Save the ImageUri
    }


    /**
     * We no longer need to store the ingredients and directions as user
     * want to exit adding new activity by pressing back button
     */
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Log.d(TAG, "NewRecipeActivity onBackPressed() called");
        Context thisContext= getApplicationContext();
        thisContext.getSharedPreferences("dbArrayValues", Activity.MODE_PRIVATE).edit().clear().commit();
        thisContext.getSharedPreferences("myDirectionsValues", Activity.MODE_PRIVATE).edit().clear().commit();
    }

}
