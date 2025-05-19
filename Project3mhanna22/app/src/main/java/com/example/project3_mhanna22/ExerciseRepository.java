package com.example.project3_mhanna22;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.util.List;
import java.util.stream.Collectors;

public class ExerciseRepository {

    private final CollectionReference userColl;

    public ExerciseRepository() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        userColl = FirebaseFirestore.getInstance()
                .collection("users")
                .document(uid)
                .collection("exercises");
    }

    /* ---------------- CRUD ---------------- */

    public Task<Void> addExercise(Exercise ex) {
        return userColl.document(ex.name).set(ex);
    }

    public Task<Void> updateExercise(Exercise ex) {
        return userColl.document(ex.name).set(ex);
    }

    public Task<Void> deleteExercise(String docId) {
        return userColl.document(docId).delete();
    }

    public interface ListListener { void onLoaded(List<Exercise> list); }
    public void getAllExercises(ListListener cb) {
        userColl.get().addOnSuccessListener(qs -> {
            List<Exercise> list = qs.getDocuments().stream().map(d -> {
                Exercise e = d.toObject(Exercise.class);
                e.id = d.getId();
                return e;
            }).collect(Collectors.toList());
            cb.onLoaded(list);
        });
    }

    /* ---------- helper: does a name already exist? ---------- */
    public interface ExistsListener { void onResult(boolean exists); }
    public void exists(String name, ExistsListener cb) {
        userColl.document(name).get()
                .addOnSuccessListener(doc -> cb.onResult(doc.exists()));
    }

    /* ---------- helper: seed starter exercises once ---------- */
    public Task<Void> seedDefaultsIfEmpty() {
        List<Exercise> defaults = List.of(
                new Exercise("Push‑ups", 12, 3, 0, ""),
                new Exercise("Squats",   15, 3, 0, ""),
                new Exercise("Plank",    45, 3, 0, "seconds"),
                new Exercise("Bicep Curls", 10, 3, 15, "dumbbells")
        );

        return userColl.get().continueWithTask(t -> {
            if (!t.getResult().isEmpty()) return Tasks.forResult(null);
            WriteBatch batch = FirebaseFirestore.getInstance().batch();
            for (Exercise e : defaults)
                batch.set(userColl.document(e.name), e);
            return batch.commit();
        });
    }
}
