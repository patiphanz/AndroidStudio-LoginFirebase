package com.example.loginfirebase;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText email, password;
    private Button signin, signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        signin = (Button) findViewById(R.id.signin);
        signup = (Button) findViewById(R.id.signup);

        // check if user is logged in
        if(mAuth.getCurrentUser() != null) {
            // User not login
            finish();
            startActivity(new Intent(getApplicationContext(), SignIn.class));
        }

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String getemail = email.getText().toString();
                String getpassword = password.getText().toString();

                callsignin(getemail, getpassword);
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String getemail = email.getText().toString();
                String getpassword = password.getText().toString();

                callsignup(getemail, getpassword);
            }
        });
    }

    private void callsignup(final String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("test", "createUserWithEmail:success");
                            Toast.makeText(MainActivity.this, "Account Created.",
                                    Toast.LENGTH_SHORT).show();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(email).build();
                            FirebaseUser user = mAuth.getCurrentUser();

                            user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        Log.d("test", "User profile updated");
                                    }
                                }
                            });
//                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("test", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Sign Up failed.",
                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void callsignin(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d("test", "Sign in is successful: " + task.isSuccessful());

                if(!task.isSuccessful()) {
                    Log.d("test", "Sign in with email failed: ", task.getException());
                    Toast.makeText(MainActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                } else {
                    Intent i = new Intent(MainActivity.this, SignIn.class);
                    finish();
                    startActivity(i);
                }
            }
        });
    }

}
