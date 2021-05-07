package com.example.museaapplication.ui.quizz;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.museaapplication.Classes.Dominio.Quizz;
import com.example.museaapplication.Classes.Json.QuizzValue;
import com.example.museaapplication.Classes.RetrofitClient;
import com.example.museaapplication.Classes.SingletonDataHolder;
import com.example.museaapplication.R;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuizzMenuFragment extends Fragment {

    private QuizzMenuViewModel mViewModel;
    private View root;

    public static QuizzMenuFragment newInstance() {
        return new QuizzMenuFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_quizz_menu, container, false);
        initializeQuizzes();


        // Botones
        final ImageButton backArrow = root.findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        final Button start = root.findViewById(R.id.start_button);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Singleton de los quizzes
                Log.d("Quizzes:", "Presionado el botón");
                QuizzQuestionFragment newFragment = new QuizzQuestionFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.QuizzActivity, newFragment).addToBackStack(null).commit();
            }
        });

        CardView helpCard = root.findViewById(R.id.helpCard);
        final Button helpOpen = root.findViewById(R.id.help_button);
        helpOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helpCard.setVisibility(View.VISIBLE);
                helpOpen.setVisibility(View.GONE);
                start.setVisibility(View.GONE);
            }
        });

        final Button helpClose = root.findViewById(R.id.help_button_close);
        helpClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helpCard.setVisibility(View.GONE);
                helpOpen.setVisibility(View.VISIBLE);
                start.setVisibility(View.VISIBLE);
            }
        });

        final ImageView imageView = root.findViewById(R.id.idImagenHelp);
        // TODO: Imagen DEMO cuando este la view del quizz
        Picasso.get().load("https://upload.wikimedia.org/wikipedia/commons/5/55/International_System_of_Units_Logo.png").into(imageView);



        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(QuizzMenuViewModel.class);
    }

    void initializeQuizzes() {
        if (SingletonDataHolder.getInstance().getQuizzes() == null) {
            Log.d("Quizzes:", "Singleton es nulo");
            Call<QuizzValue> call = RetrofitClient.getInstance().getMyApi().getQuizzes();
            call.enqueue(new Callback<QuizzValue>() {
                @Override
                public void onResponse(Call<QuizzValue> call, Response<QuizzValue> response) {
                    QuizzValue myQuizzObject = response.body();

                    Log.d("Quizzes:", String.valueOf(response.body()));
                    Log.d("Quizzes:", String.valueOf(response.code()));
                    Log.d("Quizzes:", String.valueOf(myQuizzObject.getQuizzes()));

                    Log.d("Quizzes:", String.valueOf(myQuizzObject.getQuizzes()[0].getAnswers()[0].getText()));
                    Log.d("Quizzes:", String.valueOf(myQuizzObject.getQuizzes()[0].getQuestion().getText()));

                    if (myQuizzObject != null) {
                        Quizz[] quizzes = myQuizzObject.getQuizzes();
                        SingletonDataHolder.getInstance().setQuizzes(quizzes);
                        Log.d("Quizzes:", "Singleton con cosas");
                        Log.d("Quizzes:", String.valueOf(quizzes.length));

                        Log.d("Gioconda 1:", "" + quizzes[0].getAnswers()[0].isCorrect());
                        Log.d("Gioconda 2:", "" + quizzes[0].getAnswers()[1].isCorrect());
                        Log.d("Gioconda 3:", "" + quizzes[0].getAnswers()[2].isCorrect());
                        Log.d("Gioconda 4:", "" + quizzes[0].getAnswers()[3].isCorrect());

                        Log.d("Singleton:", String.valueOf(SingletonDataHolder.getInstance()));
                        Log.d("Singleton:", String.valueOf(SingletonDataHolder.getInstance().getQuizzes()[0].get_id()));
                    }
                }

                @Override
                public void onFailure(Call<QuizzValue> call, Throwable t) {
                    Log.e("TAG1", t.getLocalizedMessage());
                    Log.e("TAG2", t.getMessage());
                    t.printStackTrace();
                    SingletonDataHolder.getInstance().setQuizzes(null);
                }
            });
        }
    }
}