package com.chinhhuynh.gymtracker;

import com.chinhhuynh.gymtracker.model.ExerciseSummary;

public class ExerciseUtils {

    public static String getExerciseSubText(ExerciseSummary exerciseSummary) {
        if (exerciseSummary == null) {
            return null;
        }

        if (exerciseSummary.durationSec < 60) {
            return String.format("%d seconds | %d sets | %s lbs",
                    exerciseSummary.durationSec, exerciseSummary.set, exerciseSummary.weight);
        } else {
            int durationMin = exerciseSummary.durationSec / 60;
            return String.format("%d mins | %d sets | %s lbs",
                    durationMin, exerciseSummary.set, exerciseSummary.weight);
        }
    }

}
