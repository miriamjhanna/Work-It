package com.example.project3_mhanna22;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class NewExercise extends AppCompatActivity {

    private EditText edName, edReps, edSets, edWeight, edNotes;
    private ExerciseRepository repo;
    private String editingName = null;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.new_exercise);
        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.new_exercise_main), (v, insets) -> {
                    Insets bar = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                    v.setPadding(bar.left, bar.top, bar.right, bar.bottom);
                    return insets;
                });

        repo = new ExerciseRepository();

        edName   = findViewById(R.id.new_ex_workout_name_edit_txt);
        edReps   = findViewById(R.id.new_ex_num_reps_edit_txt);
        edSets   = findViewById(R.id.new_ex_num_sets_edit_txt);
        edWeight = findViewById(R.id.new_ex_weight_edit_txt);
        edNotes  = findViewById(R.id.new_ex_notes_edit_txt);

        /* are we editing an existing exercise? */
        editingName = getIntent().getStringExtra("name");
        if (editingName != null)
            repo.getAllExercises(list -> list.stream()
                    .filter(e -> e.name.equals(editingName))
                    .findFirst()
                    .ifPresent(ex -> {
                        edName.setText(ex.name);
                        edReps.setText(String.valueOf(ex.reps));
                        edSets.setText(String.valueOf(ex.sets));
                        edWeight.setText(String.valueOf(ex.weight));
                        edNotes.setText(ex.notes);
                    }));
    }

    public void onButtonClickNew(View v) {
        if (v.getId() == R.id.new_ex_cancel_button) {
            startActivity(new Intent(this, ManageExercises.class));
            finish();
            return;
        }

        /* ------------ validate inputs ---------------- */
        String name = edName.getText().toString().trim();
        String setsS = edSets.getText().toString().trim();
        if (name.isEmpty() || setsS.isEmpty()) {
            toast("Name & #sets required");
            return;
        }
        int sets = Integer.parseInt(setsS);
        int reps = edReps.getText().toString().trim().isEmpty() ? 0 :
                Integer.parseInt(edReps.getText().toString().trim());
        int weight = edWeight.getText().toString().trim().isEmpty() ? 0 :
                Integer.parseInt(edWeight.getText().toString().trim());
        String notes = edNotes.getText().toString().trim();

        Exercise ex = new Exercise(name, reps, sets, weight, notes);

        /* ------------ save --------------- */
        if (editingName == null) {          // *** ADDING ***
            repo.exists(name, exists -> {
                if (exists) {
                    toast("Exercise name already used.\nChoose a different name.");
                } else {
                    repo.addExercise(ex).addOnSuccessListener(a -> finishScreen());
                }
            });
        } else {                            // *** UPDATING existing ***
            repo.updateExercise(ex).addOnSuccessListener(a -> finishScreen());
        }
    }

    private void toast(String s){ Toast.makeText(this, s, Toast.LENGTH_SHORT).show(); }

    private void finishScreen() {
        toast("Saved");
        startActivity(new Intent(this, ManageExercises.class));
        finish();
    }
}
