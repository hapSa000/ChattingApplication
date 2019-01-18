package in.test.com.chattingapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    EditText eduser, edpass;
    Button login, googlelogin, facebooklogin;
    FirebaseDatabase database;
    DatabaseReference reference;
    boolean flag = false;
    //private int counter=5;
    TextView noAttempt, register;
    SharedPreferences sharedPreferences;
    mypojo mypojo;
    GoogleSignInOptions options;
    private FirebaseAuth mAuth;
    GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        database = FirebaseDatabase.getInstance();
        mAuth=FirebaseAuth.getInstance();
        reference = database.getReference("Information");
        googlelogin = findViewById(R.id.RegisterNewgoogle);
        facebooklogin = findViewById(R.id.RegisterNewfb);
        // Configure Google Sign In
        options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.api_key))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, options);
        googlelogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = googleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, 101);
            }
        });


        eduser = findViewById(R.id.UserId);
        edpass = findViewById(R.id.UserPass);
        register = findViewById(R.id.registerNew);

        login = findViewById(R.id.btnLogin);


        sharedPreferences = getSharedPreferences("veer", MODE_PRIVATE);
        boolean loginStatus = sharedPreferences.getBoolean("loginStatus", false);
        if (loginStatus == true) {
            Intent intent = new Intent(MainActivity.this, ChattingWindow.class);
            startActivity(intent);
            finish();
        }

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), Register.class);
                startActivity(i);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String en = eduser.getText().toString();
                final String pass = edpass.getText().toString();


                reference.addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            mypojo = data.getValue(mypojo.class);
                            String email = mypojo.getEmail();
                            String password = mypojo.getPassword();
                            if (en.equals(email) && pass.equals(password)) {
                                flag = true;
                                break;
                            } else {
                                flag = false;
                            }

                        }
                        if (flag == true) {
                            Toast.makeText(MainActivity.this, "Login Successfully ", Toast.LENGTH_SHORT).show();
                            SharedPreferences sharedPreferences = getSharedPreferences("veer", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("name", mypojo.getName());
                            editor.putString("email", mypojo.getEmail());
                            editor.putString("mobile", mypojo.getMobile());
                            editor.putString("id", mypojo.getId());
                            editor.putBoolean("loginStatus", true);

                            editor.commit();
                            Intent intent = new Intent(MainActivity.this, ChattingWindow.class);
                            startActivity(intent);
                            finish();

                        } else {
                            Toast.makeText(MainActivity.this, "Login not Successfully ", Toast.LENGTH_SHORT).show();
                        }
//                       {
//                           counter--;
//                           noAttempt.setText("No of Attempts remaining: " + String.valueOf(counter));
//                           if (counter==0)
//                           {
//                               login.setEnabled(false );
//                               Toast.makeText(MainActivity.this, "Login not Successfully open app again ", Toast.LENGTH_SHORT).show();
//
//                           }
//                       }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(MainActivity.this, "DataBase Error", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 101) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("1234", "Google sign in failed", e);
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.d("1234", "firebaseAuthWithGoogle:" + account.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("1234", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("1234", "signInWithCredential:failure", task.getException());

                        }

                        // ...
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        Log.d("1234", "updateUI"+user.getDisplayName());
    }


}

