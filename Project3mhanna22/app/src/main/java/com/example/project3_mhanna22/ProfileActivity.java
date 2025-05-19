package com.example.project3_mhanna22;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project3_mhanna22.databinding.ActivityProfileBinding;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private ActivityProfileBinding b;
    private FirebaseUser user;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        b = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) { finish(); return; }

        b.txtCurrentEmail.setText(user.getEmail());

        // ---- load first/last name once ----
        db.collection("users").document(user.getUid()).get()
                .addOnSuccessListener(doc -> {
                    b.edtFirstName.setText(doc.getString("firstName"));
                    b.edtLastName.setText(doc.getString("lastName"));
                });

        /* update name */
        b.btnUpdateName.setOnClickListener(v -> {
            String f = b.edtFirstName.getText().toString().trim();
            String l = b.edtLastName.getText().toString().trim();
            if (f.isEmpty() || l.isEmpty()){ toast("Enter first & last name"); return; }
            Map<String,Object> m = new HashMap<>();
            m.put("firstName", f);
            m.put("lastName",  l);
            db.collection("users").document(user.getUid()).update(m)
                    .addOnSuccessListener(a -> toast("Name updated"))
                    .addOnFailureListener(e -> toast(e.getMessage()));
        });

        /* change password */
        b.btnChangePw.setOnClickListener(v -> {
            String newPw = b.edtNewPw.getText().toString().trim();
            if (newPw.length() < 6){ toast("Password ≥ 6 chars"); return; }
            user.updatePassword(newPw)
                    .addOnSuccessListener(a->{ toast("Password changed"); b.edtNewPw.setText(""); })
                    .addOnFailureListener(this::handleAuthError);
        });

        /* logout */
        b.btnLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finishAffinity();
        });

        /* delete account */
        b.btnDelete.setOnClickListener(v ->
                new AlertDialog.Builder(this)
                        .setTitle("Delete account")
                        .setMessage("This cannot be undone. Delete account?")
                        .setPositiveButton("Delete", (d,w) ->
                                user.delete()
                                        .addOnSuccessListener(a -> {
                                            FirebaseAuth.getInstance().signOut();        // <‑‑ guarantees state is clear
                                            toast("Account deleted");
                                            startActivity(new Intent(this, LoginActivity.class));
                                            finishAffinity();
                                        })
                                        .addOnFailureListener(this::handleAuthError))
                        .setNegativeButton("Cancel", null).show());
    }

    /* ---------- helpers ---------- */
    private void toast(String s){ Toast.makeText(this, s, Toast.LENGTH_SHORT).show(); }

    private void handleAuthError(Exception e){
        if (e.getMessage()!=null && e.getMessage().contains("recent")){
            final EditText pw = new EditText(this);
            pw.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);

            new AlertDialog.Builder(this)
                    .setTitle("Re‑authenticate")
                    .setMessage("Please enter your password to continue")
                    .setView(pw)
                    .setPositiveButton("OK", (d,w) -> {
                        user.reauthenticate(
                                        EmailAuthProvider.getCredential(user.getEmail(), pw.getText().toString()))
                                .addOnSuccessListener(a -> toast("Re‑authenticated, try again"))
                                .addOnFailureListener(x -> toast(x.getMessage()));
                    })
                    .setNegativeButton("Cancel", null).show();
        } else toast(e.getMessage());
    }
}
