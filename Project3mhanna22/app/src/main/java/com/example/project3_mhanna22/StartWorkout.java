package com.example.project3_mhanna22;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class StartWorkout extends AppCompatActivity {

    private ExerciseRepository repo;
    private final List<Wrap> list = new ArrayList<>();
    private ArrayAdapter<Wrap> adapter;

    private static class Wrap {
        final Exercise ex; int remaining;
        Wrap(Exercise e) { ex = e; remaining = e.sets; }
        @Override public String toString() {
            return ex.name + " – Remaining Sets: " + remaining;
        }
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.start_workout);

        View root = findViewById(R.id.start_workout_main);
        ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
            Insets bar = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(bar.left, bar.top, bar.right, bar.bottom);
            return insets;
        });

        repo = new ExerciseRepository();

        ListView lv = findViewById(R.id.start_workout_list);
        adapter = new ArrayAdapter<>(this, R.layout.exercise_text_format, list);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener((p, v, pos, id) -> {
            Wrap w = list.get(pos);
            w.remaining--;
            Toast.makeText(this,
                    "1 set of " + w.ex.name + " done! Keep it up!",
                    Toast.LENGTH_SHORT).show();
            if (w.remaining <= 0) list.remove(pos);
            adapter.notifyDataSetChanged();
            if (list.isEmpty())
                Toast.makeText(this, "Workout finished! Great job!", Toast.LENGTH_LONG).show();
        });

        findViewById(R.id.start_workout_ret_home_button).setOnClickListener(v ->
                new AlertDialog.Builder(this)
                        .setTitle("Exit workout")
                        .setMessage("Leave workout? Progress will be lost.")
                        .setPositiveButton("Yes", (d, w) -> {
                            startActivity(new Intent(this, MainActivity.class));
                            finish();
                        })
                        .setNegativeButton("No", null).show());

        initialiseList();
    }

    private void initialiseList() {
        /* did LogExercise pass a custom list? */
        ArrayList<Exercise> fromIntent =
                (ArrayList<Exercise>) getIntent().getSerializableExtra(LogExercise.EXTRA_EX_LIST);

        if (fromIntent != null) {
            for (Exercise e : fromIntent) list.add(new Wrap(e));
            adapter.notifyDataSetChanged();
        } else {
            /* launched directly – load full DB */
            repo.getAllExercises(all -> {
                all.forEach(e -> list.add(new Wrap(e)));
                adapter.notifyDataSetChanged();
            });
        }
    }
}
