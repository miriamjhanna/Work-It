package com.example.project3_mhanna22;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class ManageExercises extends AppCompatActivity {

    private ExerciseRepository repo;
    private List<Exercise> list = new ArrayList<>();
    private ArrayAdapter<Exercise> adapter;
    private ListView lv;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.manage_exercises);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.manage_exercises_main), (v, insets) -> {
            Insets bar = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(bar.left, bar.top, bar.right, bar.bottom);
            return insets;
        });

        repo = new ExerciseRepository();
        lv   = findViewById(R.id.mylist);
        adapter = new ArrayAdapter<>(this, R.layout.exercise_text_format, list);
        lv.setAdapter(adapter);

        load();

        lv.setOnItemClickListener((p, v, pos, id) -> {
            Intent it = new Intent(this, NewExercise.class);
            it.putExtra("name", list.get(pos).name);
            startActivity(it);
        });

        lv.setOnItemLongClickListener((p, v, pos, id) -> {
            Exercise ex = list.get(pos);
            new AlertDialog.Builder(this)
                    .setMessage("Delete " + ex.name + "?")
                    .setPositiveButton("Yes", (d, w) ->
                            repo.deleteExercise(ex.name)
                                    .addOnSuccessListener(a -> {
                                        Toast.makeText(this,"Deleted",Toast.LENGTH_SHORT).show();
                                        load();
                                    }))
                    .setNegativeButton("No", null).show();
            return true;
        });
    }

    private void load() {
        repo.getAllExercises(newList -> {
            list.clear();
            list.addAll(newList);
            adapter.notifyDataSetChanged();
        });
    }

    public void onButtonClickManage(View v) {
        if (v.getId() == R.id.add_exercise_button)
            startActivity(new Intent(this, NewExercise.class));
        else if (v.getId() == R.id.manage_ex_ret_home_button)
            startActivity(new Intent(this, MainActivity.class));
    }

    @Override protected void onResume() { super.onResume(); load(); }
}
