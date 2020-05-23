package com.candra.ukmupb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import androidx.appcompat.widget.AppCompatTextView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.candra.ukmupb.fragment.UserFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;


public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText etName_r, etEmail_r, etPassword_r, etNIM_r;
    private ProgressBar progressBar_r;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        etName_r = findViewById(R.id.etName_r);
        etEmail_r = findViewById(R.id.etEmail_r);
        etNIM_r = findViewById(R.id.etNIM_r);
        etPassword_r = findViewById(R.id.etPassword_r);
        progressBar_r = findViewById(R.id.progressBar_r);


        TextView btnLogin = findViewById(R.id.etLogin_r);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goLogin = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(goLogin);
                finish();
            }
        });
        Button btnDaftar = findViewById(R.id.btnDaftar_r);
        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = etEmail_r.getText().toString();
                final String password = etPassword_r.getText().toString();

                if (email.equals("")){
                    Toast.makeText(SignUpActivity.this, "Email Tidak Boleh Kosong",
                            Toast.LENGTH_SHORT).show();
                } else if (password.equals("")) {
                    Toast.makeText(SignUpActivity.this, "Password Tidak Boleh Kosong",
                            Toast.LENGTH_SHORT).show();
                } else if (password.length()<6) {
                    Toast.makeText(SignUpActivity.this, "Password minimum 6 karakter",
                            Toast.LENGTH_SHORT).show();

                }else {
                    progressBar_r.setVisibility(View.VISIBLE);
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Objects.requireNonNull(mAuth.getCurrentUser()).sendEmailVerification()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(SignUpActivity.this, "Daftar berhasil, Silahkan cek email untuk verifikasi.",
                                                            Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                                                } else {
                                                    Toast.makeText(SignUpActivity.this, task.getException().getMessage(),
                                                            Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(SignUpActivity.this, "Authentication failed "+task.getException().getMessage(),
                                                Toast.LENGTH_SHORT).show();
                                    }
                                    progressBar_r.setVisibility(View.GONE);

                                    // ...
                                }
                            });
                }
            }
        });
    }
    @Override
    public void onBackPressed(){
        Intent goLogin = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(goLogin);
        finish();
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }
}

