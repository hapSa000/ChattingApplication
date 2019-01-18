package in.test.com.chattingapplication;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Register extends AppCompatActivity {
    EditText editName,editEmail,editPassword,editconfirmpassword,editMobile;
    Button buttonRegister;
    FirebaseDatabase database;
    DatabaseReference reference;
    TextView back;
    SelectImageHelper helper;
    FirebaseStorage storage;
    ImageView circleImageView;
    StorageReference storageReference;
    String imageUrl="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        FirebaseApp.initializeApp(Register.this);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Information");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        circleImageView = findViewById(R.id.profil_image);
        helper = new SelectImageHelper(Register.this, circleImageView);
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                helper.selectImageOption();

            }
        });
        editName = findViewById(R.id.UserName);
        editEmail = findViewById(R.id.UserEmail);
        editPassword = findViewById(R.id.UserPasssword);

        editMobile = findViewById(R.id.UserMobile);
        back = findViewById(R.id.backtoLogin);
        buttonRegister = findViewById(R.id.btn_Rigister);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = editName.getText().toString();
                final String email = editEmail.getText().toString();
                final String mobile = editMobile.getText().toString();
                final String password = editPassword.getText().toString();
                final Uri imageUri = helper.getURI_FOR_SELECTED_IMAGE();
                StorageReference stoRef = storageReference.child("image/" + email);
                stoRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        imageUrl = taskSnapshot.getDownloadUrl().toString();
                        Log.d("1234", "onSuccess: " + imageUrl);


                        String id = reference.push().getKey();
                        mypojo mypojo = new mypojo();
                        mypojo.setName(name);
                        mypojo.setEmail(email);
                        mypojo.setMobile(mobile);
                        mypojo.setPassword(password);
                        mypojo.setId(id);
                        mypojo.setImageUrl(imageUrl);
                        // mypojo.setConfirmpassrord(confirmpassword);
                        reference.child(id).setValue(mypojo);
                        Toast.makeText(Register.this, "Registration Successfully Done", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Register.this, MainActivity.class);
                        startActivity(intent);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Register.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                });
            }

//                back.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        Toast.makeText(Register.this, "Back to LOGIN", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(Register.this, MainActivity.class);
//                        startActivity(intent);
//                    }
//                });


        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        helper.handleResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, final @NonNull String[] permissions, final @NonNull int[] grantResults) {
        helper.handleGrantedPermisson(requestCode, grantResults);   // call this helper class method
    }


}