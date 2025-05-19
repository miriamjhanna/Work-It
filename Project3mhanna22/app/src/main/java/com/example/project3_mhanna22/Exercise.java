package com.example.project3_mhanna22;

import java.io.Serializable;

public class Exercise implements Serializable {
    private static final long serialVersionUID = 1L;

    public String id;      // Firestore document id
    public String name;
    public int reps;
    public int sets;
    public int weight;
    public String notes;

    public Exercise() {}   // Firestore needs this

    public Exercise(String name, int reps, int sets, int weight, String notes) {
        this.name = name;
        this.reps = reps;
        this.sets = sets;
        this.weight = weight;
        this.notes = notes;
    }

    @Override public String toString() { return name; }
}
