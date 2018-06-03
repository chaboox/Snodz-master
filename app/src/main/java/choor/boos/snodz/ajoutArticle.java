package choor.boos.snodz;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ajoutArticle extends AppCompatActivity {
    String id;
    int key;
    long idArticle;
    EditText nom, prixAchat,prixVente,taxe,quantite,type;
    private ImageView profile_img;

    private Button camera;

    private String userChoosenTask;

    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;

    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private ImageView imageView;
    private EditText txtImageName;
    private Uri imgUri;


    public static final String FB_STORAGE_PATH = "image/";
    public static final String FB_DATABASE_PATH = "image";
    public static final int REQUEST_CODE = 1234;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajout_article);

        mStorageRef = FirebaseStorage.getInstance().getReference();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(FB_DATABASE_PATH);

        SharedPreferences prefs = getSharedPreferences("CachSnodz", MODE_PRIVATE);
        id = prefs.getString("id", "");
        nom = (EditText)findViewById(R.id.nom);
        prixAchat = (EditText)findViewById(R.id.prixachat);
        prixVente = (EditText)findViewById(R.id.prixvente);
        taxe = (EditText)findViewById(R.id.taxe);
        type = (EditText)findViewById(R.id.type);
        quantite = (EditText)findViewById(R.id.quan);
        Button ajouter = (Button)findViewById(R.id.ajouter);

        try {



            setUI();

            setUITEXT();

        }catch (Exception e){

            e.printStackTrace();

        }


        ajouter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              final  DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("article").child(id);
                final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("article").child(id);;
                Query referenceUser = databaseReference.orderByKey().limitToLast(1);
                referenceUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                         key=0;
                       // idArticle =  dataSnapshot.getChildrenCount()+1;
                        for (DataSnapshot child: dataSnapshot.getChildren()) {
                            Log.d("User key", child.getKey());
                            if(child.getChildrenCount()==0)
                                key= 0;
                                        else
                            key = Integer.valueOf(child.getKey()) + 1;

                        }
                       DatabaseReference refArcticle = myRef.child(String.valueOf(key));
                       refArcticle.child("nom").setValue(nom.getText().toString());
                       refArcticle.child("prix_achat").setValue(prixAchat.getText().toString());
                       refArcticle.child("prix_vente").setValue(prixVente.getText().toString());
                       refArcticle.child("type").setValue(type.getText().toString());
                       refArcticle.child("taxe").setValue(taxe.getText().toString());
                       refArcticle.child("quantite").setValue(quantite.getText().toString());

                       ////////ajout photo firebase




                        if (imgUri != null) {
                            final ProgressDialog dialog = new ProgressDialog(ajoutArticle.this);
                            dialog.setTitle("Uploading image");
                            dialog.show();

                            //Get the storage reference
                            StorageReference ref = mStorageRef.child(FB_STORAGE_PATH + System.currentTimeMillis() + "." + getImageExt(imgUri));

                            //Add file to reference

                            ref.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                                    //Dimiss dialog when success
                                    dialog.dismiss();
                                    //Display success toast msg
                                    Toast.makeText(getApplicationContext(), "Image uploaded", Toast.LENGTH_SHORT).show();
                                    ImageUpload imageUpload = new ImageUpload( taskSnapshot.getDownloadUrl().toString());

                                    //Save image info in to firebase database
                                    //String uploadId = mDatabaseRef.push().getKey();
                                   // mDatabaseRef.child(uploadId).setValue(imageUpload);
                                    DatabaseReference refArcticle = myRef.child(String.valueOf(key));
                                    refArcticle.child("imageUrl").setValue(imageUpload.getUrl());

                                }
                            })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                            //Dimiss dialog when error
                                            dialog.dismiss();
                                            //Display err toast msg
                                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {

                                        @Override
                                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                                            //Show upload progress

                                            double progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                                            dialog.setMessage("Uploaded " + (int) progress + "%");
                                        }
                                    });
                        } else {
                            Toast.makeText(getApplicationContext(), "Please select image", Toast.LENGTH_SHORT).show();
                        }
                    }





                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }


    private void setUITEXT() {





    }



    private void setUI() {



        profile_img = (ImageView)findViewById(R.id.imageView);

        camera = (Button) findViewById(R.id.parcourImg);

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectImage();
            }
        });





    }

/*

    @Override

    public void onClick(View view) {



        switch (view.getId()){



            case R.id.parcourImg:



                selectImage();



                break;



        }







    }*/



    @Override

    protected void onResume() {

        super.onResume();

    }





    @Override

    protected void onPause() {

        super.onPause();

    }





    @Override

    protected void onDestroy() {

        super.onDestroy();

    }







    private void selectImage() {



        final CharSequence[] items = { "Take Photo", "Choose from Library",

                "Cancel" };



        AlertDialog.Builder builder = new AlertDialog.Builder(ajoutArticle.this);

        builder.setTitle("Add Photo!");

        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int item) {

                boolean result=Utility.checkPermission(ajoutArticle.this);



                if (items[item].equals("Take Photo")) {

                    userChoosenTask ="Take Photo";

                    if(result)

                        cameraIntent();



                } else if (items[item].equals("Choose from Library")) {

                    userChoosenTask ="Choose from Library";

                    if(result)

                        galleryIntent();



                } else if (items[item].equals("Cancel")) {

                    dialog.dismiss();

                }

            }

        });

        builder.show();





    }



    private void galleryIntent() {



        Intent intent = new Intent();

        intent.setType("image/*");

        intent.setAction(Intent.ACTION_GET_CONTENT);//

        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);



    }



    private void cameraIntent() {



        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        startActivityForResult(intent, REQUEST_CAMERA);



    }





    @Override

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {

            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if(userChoosenTask.equals("Take Photo"))

                        cameraIntent();

                    else if(userChoosenTask.equals("Choose from Library"))

                        galleryIntent();

                } else {

                    //code for deny

                }

                break;

        }

    }



    @Override

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);



        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == SELECT_FILE)

                onSelectFromGalleryResult(data);

            else if (requestCode == REQUEST_CAMERA)

                onCaptureImageResult(data);

        }

    }



    private void onCaptureImageResult(Intent data) {
      //  imgUri = data.getData();

        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);



        File destination = new File(Environment.getExternalStorageDirectory(),

                System.currentTimeMillis() + ".jpg");

imgUri = Uri.fromFile(destination);

        FileOutputStream fo;

        try {

            destination.createNewFile();

            fo = new FileOutputStream(destination);

            fo.write(bytes.toByteArray());

            fo.close();

        } catch (FileNotFoundException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }


        profile_img.setImageBitmap(thumbnail);

    }



    @SuppressWarnings("deprecation")

    private void onSelectFromGalleryResult(Intent data) {

       // imgUri = data.getData();











        Bitmap bm=null;

        if (data != null) {

            try {

                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());


                //Bitmap thumbnail = (Bitmap) data.getExtras().get("data");

                ByteArrayOutputStream bytes = new ByteArrayOutputStream();

                bm.compress(Bitmap.CompressFormat.JPEG, 50, bytes);



                File destination = new File(Environment.getExternalStorageDirectory(),

                        System.currentTimeMillis() + ".jpg");

                imgUri = Uri.fromFile(destination);

                FileOutputStream fo;

                try {

                    destination.createNewFile();

                    fo = new FileOutputStream(destination);

                    fo.write(bytes.toByteArray());

                    fo.close();

                } catch (FileNotFoundException e) {

                    e.printStackTrace();

                } catch (IOException e) {

                    e.printStackTrace();

                }



            } catch (IOException e) {

                e.printStackTrace();

            }

        }



        profile_img.setImageBitmap(bm);

    }

    public String getImageExt(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }




}
//version 2