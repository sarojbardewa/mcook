package sarojbardewa.com.cookhookpro.ProfileUpdateActivity;

/**
 * Created by b on 6/12/17.
 */

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import sarojbardewa.com.cookhookpro.ProfileActivity.ProfileActivity;
import sarojbardewa.com.cookhookpro.R;
import sarojbardewa.com.cookhookpro.profilehelper.ProfileHelperActivity;

/**
 * Created by b on 6/10/17.
 */

public class UpdateProfileActivity extends AppCompatActivity {





    private Uri uri;

    private ProfileHelperActivity mProfileHelper;
    private static final String TAG = "UpdateProfileActivity";
    private static final String KEY_INDEX = "Profile Image";
    private static final String KEY_INDEX1 = "Name";
    private static final String KEY_INDEX2 = "Location";
    private static final String KEY_INDEX3 = "Favorite Recipe";
    private static final String KEY_INDEX4 = "Dietary Preferences";
    private String mUid;





    private EditText editName, editFavRecipe, editLocation, editDietaryPrefs ;
    private Button btnChoosePersonal, btnUpdateProfile, btnViewProfile;

    private ImageView mImageView;

    final int REQUEST_CODE_GALLERY = 999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);
        setWidgets();

        AsyncTaskRunner runner = new AsyncTaskRunner();


        runner.execute();
        setprofile();

        if (savedInstanceState != null) {
            uri = savedInstanceState.getParcelable(KEY_INDEX);
            try {
                Bitmap bm = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                mImageView.setImageBitmap(bm);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            setWidgets();
        }


        btnChoosePersonal = (Button) findViewById(R.id.btnChoose);
        btnChoosePersonal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(
                        UpdateProfileActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_GALLERY
                );
            }
        });


        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //put firebase code here
                editName.getText().toString().trim();
                editLocation.getText().toString().trim();
                editFavRecipe.getText().toString().trim();
                editDietaryPrefs.getText().toString().trim();
                String profilename = editName.getText().toString().trim();
                String location = editLocation.getText().toString().trim();
                String recipe = editFavRecipe.getText().toString().trim();
                String dietary = editDietaryPrefs.getText().toString().trim();
                //String URI = getImageExt(uri);
                if (TextUtils.isEmpty(mUid)) {
                    mProfileHelper.createProfile(profilename, location, recipe, dietary, "URI");
                } else {
                    mProfileHelper.updateProfile(profilename, location, recipe, dietary, "URI");
                }


                Toast.makeText(getApplicationContext(), "Added successfully!", Toast.LENGTH_SHORT).show();
            }
        });

        btnViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UpdateProfileActivity.this, ProfileActivity.class);
                startActivity(intent);

            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_CODE_GALLERY){
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_GALLERY);
            }
            else {
                Toast.makeText(getApplicationContext(), "You don't have permission to access file location!", Toast.LENGTH_SHORT).show();
            }
            return;
        }


    }


    private byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable)mImageView.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);

                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                mImageView.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }


    }

    public String getImageExt(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void setWidgets(){

        editName = (EditText) findViewById(R.id.editName);
        editLocation = (EditText) findViewById(R.id.editlocation);
        editDietaryPrefs = (EditText) findViewById(R.id.editdietary);
        editFavRecipe = (EditText) findViewById(R.id.editrecipe);
        btnChoosePersonal = (Button) findViewById(R.id.btnChoose);
        btnUpdateProfile = (Button) findViewById(R.id.btnUpdateProfile);
        btnViewProfile = (Button) findViewById(R.id.btnViewProfile);
        mImageView = (ImageView) findViewById(R.id.imageView);
    }
    private void setprofile(){
        mProfileHelper = new ProfileHelperActivity();
        mUid = (String) mProfileHelper.getUserId();
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putParcelable(KEY_INDEX,uri);  // Save the ImageUri
        savedInstanceState.putString(KEY_INDEX1, editName.getText().toString());
        savedInstanceState.putString(KEY_INDEX2, editLocation.getText().toString());
        savedInstanceState.putString(KEY_INDEX3, editFavRecipe.getText().toString());
        savedInstanceState.putString(KEY_INDEX4, editDietaryPrefs.getText().toString());
    }

    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        private String resp;


        @Override
        protected String doInBackground(String... params) {
            // Calls onProgressUpdate()
            try {
                setprofile();
                resp = "Slept for " + params[0] + " seconds";
            }  catch (Exception e) {
                e.printStackTrace();
                resp = e.getMessage();
            }
            return resp;
        }


        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation

        }


        @Override
        protected void onPreExecute() {

        }
        @Override
        protected void onProgressUpdate(String... text) {
        }
    }
}
