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

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class LogExercise extends AppCompatActivity {

    public static final String EXTRA_EX_LIST = "exercise_list";

    private ExerciseRepository repo;
    private final ArrayList<Exercise> list = new ArrayList<>();
    private ArrayAdapter<Exercise> adapter;
    private View root;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.log_exercise);
        root = findViewById(R.id.log_exercise_main);

        ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
            Insets bar = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(bar.left, bar.top, bar.right, bar.bottom);
            return insets;
        });

        repo = new ExerciseRepository();

        ListView lv = findViewById(R.id.log_ex_list);
        adapter = new ArrayAdapter<>(this, R.layout.exercise_text_format, list);
        lv.setAdapter(adapter);

        /* short click – details */
        lv.setOnItemClickListener((p, v, pos, id) -> {
            Exercise ex = list.get(pos);
            String msg = ex.name + "\nSets: " + ex.sets +
                    ", Reps: " + ex.reps +
                    (ex.weight > 0 ? ", Weight: " + ex.weight : "") +
                    (ex.notes.isEmpty() ? "" : ", Notes: " + ex.notes);
            Snackbar sb = Snackbar.make(root, msg, Snackbar.LENGTH_LONG);
            android.widget.TextView tv = sb.getView().findViewById(
                    com.google.android.material.R.id.snackbar_text);
            tv.setMaxLines(6);
            sb.show();
        });

        /* long click – remove from today’s list only */
        lv.setOnItemLongClickListener((p, v, pos, id) -> {
            Exercise ex = list.get(pos);
            new AlertDialog.Builder(this)
                    .setTitle("Remove Exercise")
                    .setMessage("Remove \"" + ex.name + "\" from today’s log?")
                    .setPositiveButton("Yes", (d, w) -> {
                        list.remove(pos);
                        adapter.notifyDataSetChanged();
                    })
                    .setNegativeButton("No", null).show();
            return true;
        });

        load();
    }

    private void load() {
        repo.getAllExercises(newList -> {
            list.clear();
            list.addAll(newList);
            adapter.notifyDataSetChanged();
        });
    }

    public void onButtonClickLog(View v) {
        if (v.getId() == R.id.log_ex_start_workout_button) {
            /* pass today's list to StartWorkout */
            Intent it = new Intent(this, StartWorkout.class);
            it.putExtra(EXTRA_EX_LIST, list);
            startActivity(it);
        } else if (v.getId() == R.id.log_ex_ret_home_button) {
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    @Override protected void onResume() { super.onResume(); load(); }
}
