package com.example.museaapplication.ui.quizz;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.museaapplication.Classes.Dominio.Quizz;
import com.example.museaapplication.Classes.RetrofitClient;
import com.example.museaapplication.Classes.SingletonDataHolder;
import com.example.museaapplication.R;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuizzQuestionFragment extends Fragment {

    private QuizzQuestionViewModel mViewModel;
    private View root;

    private int correctColor;
    private int errorColor;
    private int defaultColor;

    private TextView current;
    private TextView total;
    private ImageView image;
    private TextView question;

    private TextView countdownText;
    private CountDownTimer countDownTimer;
    private long timeLeftInMilliseconds = 30000; //30 segundos
    private boolean timerRunning;

    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;

    private Button endButton;

    private boolean hasAnswered;
    private int currentQuizz;

    private int points;
    private int totalPoints;

    Quizz quizz;

    public static QuizzQuestionFragment newInstance() {
        return new QuizzQuestionFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_quizz_question, container, false);
        mViewModel = new QuizzQuestionViewModel();
        correctColor = getContext().getResources().getColor(R.color.green_ok);
        errorColor = getContext().getResources().getColor(R.color.red_alert);
        defaultColor = getContext().getResources().getColor(R.color.grey_700);

        //quizzCard
        current = root.findViewById(R.id.quizz_current);
        total = root.findViewById(R.id.quizz_total);
        image = root.findViewById(R.id.quizz_image);
        question = root.findViewById(R.id.quizz_question);
        question.setMovementMethod(new ScrollingMovementMethod());
        button1 = root.findViewById(R.id.quizz_button1);
        button2 = root.findViewById(R.id.quizz_button2);
        button3 = root.findViewById(R.id.quizz_button3);
        button4 = root.findViewById(R.id.quizz_button4);
        countdownText = root.findViewById(R.id.crono_text);
        hasAnswered = false;
        currentQuizz = 0;

        //endCard
        endButton = root.findViewById(R.id.quizz_back_button);

        // Ahora mismo pilla una solo quizz guardada en el SINGLETON
        // Para cuando hagamos subsets 'quizz' sera una array
        // Ya he medio preparado una funcion para un subset en el QuizzQuestionViewModel pero tampoco la he probado XD
        quizz = SingletonDataHolder.getInstance().getQuizzes()[currentQuizz];
        total.setText(""+ SingletonDataHolder.getInstance().getQuizzes().length);
        Log.d("OneQuizz", quizz.get_id());

        updateCard();
        buttonActions();

        return root;
    }

    private void updateCard() {
        if(timerRunning) stopTimer();
        timeLeftInMilliseconds = 30000;
        startTimer();

        hasAnswered = false;
        current.setText(String.valueOf(currentQuizz+1));
        if (quizz.getImage().isEmpty())
            Picasso.get().load("https://www.zooplus.es/magazine/wp-content/uploads/2018/04/fotolia_169457098.jpg").into(image);
        else
            Picasso.get().load(quizz.getImage()).into(image);

        question.setText(quizz.getQuestion().getText());

        button1.setText(quizz.getAnswers()[0].getText());
        button1.setTextSize(chooseSize(quizz.getAnswers()[0].getText().length()));
        button1.setBackgroundColor(chooseColor(-1));

        button2.setText(quizz.getAnswers()[1].getText());
        button2.setTextSize(chooseSize(quizz.getAnswers()[1].getText().length()));
        button2.setBackgroundColor(chooseColor(-1));

        button3.setText(quizz.getAnswers()[2].getText());
        button3.setTextSize(chooseSize(quizz.getAnswers()[2].getText().length()));
        button3.setBackgroundColor(chooseColor(-1));

        button4.setText(quizz.getAnswers()[3].getText());
        button4.setTextSize(chooseSize(quizz.getAnswers()[3].getText().length()));
        button4.setBackgroundColor(chooseColor(-1));
    }

    public void startStop(){
        if(timerRunning){
            stopTimer();
        } else{
            startTimer();
        }
    }

    public void stopTimer(){
        countDownTimer.cancel();
        timerRunning = false;
    }

    public void startTimer(){
        countDownTimer = new CountDownTimer(timeLeftInMilliseconds,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMilliseconds = millisUntilFinished;
                updateTimer();
            }

            @Override
            public void onFinish() {

            }
        }.start();
        timerRunning = true;
    }

    public void updateTimer(){
        int seconds = (int) timeLeftInMilliseconds % 60000 / 1000;
        if(seconds == 0){
            showcorrect();
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    nextQuizz();
                }
            }, 3000);

        }
        String timelefttext = "";
        timelefttext +=seconds;
        countdownText.setText(timelefttext);
    }

    public void showcorrect(){
        if (quizz.getAnswers()[0].isCorrect()) button1.setBackgroundColor(correctColor);
        else button1.setBackgroundColor(errorColor);
        if (quizz.getAnswers()[1].isCorrect()) button2.setBackgroundColor(correctColor);
        else button2.setBackgroundColor(errorColor);
        if (quizz.getAnswers()[2].isCorrect()) button3.setBackgroundColor(correctColor);
        else button3.setBackgroundColor(errorColor);
        if (quizz.getAnswers()[3].isCorrect()) button4.setBackgroundColor(correctColor);
        else button4.setBackgroundColor(errorColor);
    }

    private void buttonActions() {
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!hasAnswered) {
                    button1.setBackgroundColor(chooseColor(0));
                    nextQuizz();
                }
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!hasAnswered) {
                    button2.setBackgroundColor(chooseColor(1));
                    nextQuizz();
                }
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!hasAnswered) {
                    button3.setBackgroundColor(chooseColor(2));
                    nextQuizz();
                }
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!hasAnswered) {
                    button4.setBackgroundColor(chooseColor(3));
                    nextQuizz();
                }
            }
        });

        endButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }

    private void nextQuizz() {
        hasAnswered = true;
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 1s = 1000ms
                currentQuizz += 1;
                if (currentQuizz >= SingletonDataHolder.getInstance().getQuizzes().length) {
                    // End of the game
                    endQuizz();
                }
                else {
                    quizz = SingletonDataHolder.getInstance().getQuizzes()[currentQuizz];
                    updateCard();
                }
            }
        }, 1000);
    }

    private int chooseColor(int questionIndex) {
        // reset buttons
        if (questionIndex == -1) return defaultColor;

        // answered quizz
        totalPoints += quizz.getPoints();
        if (quizz.getAnswers()[questionIndex].isCorrect()) {
            points += quizz.getPoints();
            return correctColor;
        }
        return errorColor;
    }

    private int chooseSize(int textLength) {
        if (textLength <= 7)
            return 25;
        else if (textLength <= 40)
            return 18;
        return 12;
    }

    private void endQuizz() {
        Log.d("Points", "" + points);
        Log.d("Total points", "" + totalPoints);
        // Show end view

        root.findViewById(R.id.quizzCard).setVisibility(View.GONE);
        root.findViewById(R.id.endCard).setVisibility(View.VISIBLE);

        // Update points
        String username = SingletonDataHolder.getInstance().getLoggedUser();
        Log.d("Check Singleton:","" + username);
        // borrar la siguiente linea cuando tengamos el username
        username = "luisitodescomunica";
        Call<Void> call = RetrofitClient.getInstance().getMyApi().updatePoints(username,String.valueOf(points),String.valueOf(totalPoints));
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                switch (response.code()) {
                    case 200:
                        Log.d("Update Points:","Points updated correctly");
                        break;
                    case 404:
                        Log.d("Update Points:","There is no user for such id");
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("TAG1", t.getLocalizedMessage());
                Log.e("TAG2", t.getMessage());
                t.printStackTrace();
            }
        });




    }
}