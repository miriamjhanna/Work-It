package com.example.project3_mhanna22;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project3_mhanna22.databinding.ActivityLoginBinding;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding b;
    private FirebaseAuth auth;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        b = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        auth = FirebaseAuth.getInstance();

        /* ---- LOG‑IN button ---- */
        b.btnLogin.setOnClickListener(v -> {
            String email = b.edtEmail.getText().toString().trim();
            String pw    = b.edtPassword.getText().toString().trim();
            auth.signInWithEmailAndPassword(email, pw)
                    .addOnSuccessListener(res ->
                            // seed defaults *after* we are signed in
                            new ExerciseRepository().seedDefaultsIfEmpty()
                                    .addOnCompleteListener(x -> {
                                        startActivity(new Intent(this, MainActivity.class));
                                        finish();
                                    }))
                    .addOnFailureListener(e ->
                            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show());
        });

        /* ---- “Go to register” link ---- */
        b.btnGoRegister.setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class)));
    }
}
