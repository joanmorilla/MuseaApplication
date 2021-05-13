package com.example.museaapplication.ui.quizz;

import androidx.lifecycle.ViewModel;

import com.example.museaapplication.Classes.Dominio.Quizz;
import com.example.museaapplication.Classes.SingletonDataHolder;

import java.util.Random;

public class QuizzQuestionViewModel extends ViewModel {

    public Quizz[] getSubsetQuizzes(int numberOfQuizzes) {
        Quizz[] quizzes = SingletonDataHolder.getInstance().getQuizzes();
        boolean[] takenQuizzes = new boolean[quizzes.length];
        for (int i = 0; i < takenQuizzes.length; i++) takenQuizzes[i] = false;

        Random r = new Random();

        for (int i = 0; i < numberOfQuizzes; ++i) {
            int result = r.nextInt(quizzes.length);
            if (takenQuizzes[result]) i--;
            else takenQuizzes[result] = true;
        }

        Quizz[] subset = new Quizz[numberOfQuizzes];

        int j = 0;
        for (int i = 0; i < takenQuizzes.length; i++) {
            if (takenQuizzes[i]) {
                subset[j] = quizzes[i];
                ++j;
            }
        }

        return subset;
    }
}