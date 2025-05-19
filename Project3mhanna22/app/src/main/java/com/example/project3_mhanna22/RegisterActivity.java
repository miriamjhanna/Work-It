package com.example.project3_mhanna22;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project3_mhanna22.databinding.ActivityRegisterBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding b;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        b = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        auth = FirebaseAuth.getInstance();

        b.btnRegister.setOnClickListener(v -> {
            String first = b.edtFirst.getText().toString().trim();
            String last  = b.edtLast.getText().toString().trim();
            String email = b.edtEmail.getText().toString().trim();
            String pw    = b.edtPassword.getText().toString().trim();
            String pw2   = b.edtPassword2.getText().toString().trim();

            if (first.isEmpty() || last.isEmpty()) {
                toast("Enter first & last name");
                return;
            }
            if (!pw.equals(pw2)) {
                toast("Passwords do not match");
                return;
            }

            auth.createUserWithEmailAndPassword(email, pw)
                    .addOnSuccessListener(res -> {
                        /* -------------- save profile names ---------------- */
                        String uid = auth.getCurrentUser().getUid();
                        Map<String, Object> profile = new HashMap<>();
                        profile.put("firstName", first);
                        profile.put("lastName",  last);
                        FirebaseFirestore.getInstance()
                                .collection("users").document(uid)
                                .set(profile);

                        /* -------------- seed starter exercises ------------ */
                        new ExerciseRepository().seedDefaultsIfEmpty()
                                .addOnCompleteListener(x -> {
                                    startActivity(new Intent(this, MainActivity.class));
                                    finish();
                                });
                    })
                    .addOnFailureListener(e -> {
                        /* --- friendly message if email is still propagating --- */
                        if (e instanceof FirebaseAuthUserCollisionException) {
                            toast("That e‑mail was deleted moments ago.\n" +
                                    "Give Firebase a minute to finish syncing, then try again.");
                        } else {
                            toast(e.getMessage());
                        }
                    });
        });
    }

    private void toast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }
}
