package sarojbardewa.com.cookhookpro.ProfileUpdateActivity;

/**
 * Created by b on 6/12/17.
 */

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import sarojbardewa.com.cookhookpro.ProfileActivity.ProfileActivity;
import sarojbardewa.com.cookhookpro.R;

/**
 * Created by b on 6/10/17.
 */

public class UpdateProfileActivity extends AppCompatActivity {

    private Uri uri;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private ProgressDialog dialog;
    private static final String TAG = "UpdateProfileActivity";


    private static final String KEY_INDEX = "all_data";
    public static final String STORAGE_PATH = "image/";
    private EditText editName, editFavRecipe, editLocation, editDietaryPrefs ;
    private Button btnChoosePersonal, btnUpdateProfile, btnViewProfile;
    private ImageView mImageView;
    final int REQUEST_CODE_GALLERY = 999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);
        setWidgets();
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
        }


        mStorageRef = FirebaseStorage.getInstance().getReference();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("user");
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
            @SuppressWarnings("VisibleForTests")
            public void onClick(View view) {


                if (uri != null) {
                    dialog = new ProgressDialog(UpdateProfileActivity.this);
                    dialog.setTitle("Uploading Profile");
                    dialog.show();
                    // Get storage reference
                    StorageReference sRef = mStorageRef.child(STORAGE_PATH +
                            System.currentTimeMillis() + "." + getImageExt(uri));
                    // Add file to reference
                    sRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            // Dismiss the dialog
                            dialog.dismiss();

                            // Display success toast messgae
                            Toast.makeText(getApplicationContext(), " Profile Upload Successful", Toast.LENGTH_SHORT).show();
                            ProfileUpload profileupload = new ProfileUpload(editName.getText().toString().trim(), editLocation.getText().toString().trim(), editFavRecipe.getText().toString().trim(), editDietaryPrefs.getText().toString().trim(), taskSnapshot.getDownloadUrl().toString());



                            //String uploadID = mDatabaseRef.push().getKey();
                            mDatabaseRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(profileupload);
                            //mDatabaseRef.child(mUid).setValue(profileupload);
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {

                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            // Show upload progress
                            double progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            dialog.setMessage("Uploaded " + (int) progress + "");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Dismiss the dialog
                            dialog.dismiss();

                            // Display success toast messgae
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });

                } else {
                    // Display success toast messgae
                    Toast.makeText(getApplicationContext(), "Please select image", Toast.LENGTH_SHORT).show();
                }
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null){
            uri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);

                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                mImageView.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public String getImageExt (Uri uri){
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

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putParcelable(KEY_INDEX,uri);  // Save the ImageUri
    }

}
