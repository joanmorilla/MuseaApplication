package com.example.museaapplication.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.museaapplication.Classes.Dominio.Comment;
import com.example.museaapplication.Classes.Dominio.Work;
import com.example.museaapplication.Classes.OnBackPressed;
import com.example.museaapplication.Classes.RetrofitClient;
import com.example.museaapplication.Classes.ViewModels.SharedViewModel;
import com.example.museaapplication.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Comentaris_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Comentaris_Fragment extends Fragment implements OnBackPressed {
    SharedViewModel sharedViewModel;

    EditText newCommentText;
    ImageButton postButton;

    public Comentaris_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Comentaris_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Comentaris_Fragment newInstance(String param1, String param2) {
        Comentaris_Fragment fragment = new Comentaris_Fragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_comentaris, container, false);

        newCommentText = root.findViewById(R.id.comment_input);
        postButton = root.findViewById(R.id.post_comment_button);

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!newCommentText.getText().toString().equals("")) {
                    YoYo.with(Techniques.SlideInLeft).duration(500).playOn(view);
                    Call<Comment> call = RetrofitClient.getInstance().getMyApi().postComment(sharedViewModel.getCurWork().getValue().get_id(), newCommentText.getText().toString(), "user1");
                    call.enqueue(new Callback<Comment>() {
                        @Override
                        public void onResponse(Call<Comment> call, Response<Comment> response) {
                            sharedViewModel.getCurWork().getValue().addComment(response.body());
                            sharedViewModel.setCurWork(sharedViewModel.getCurWork().getValue());
                            newCommentText.setText("");
                        }

                        @Override
                        public void onFailure(Call<Comment> call, Throwable t) {

                        }
                    });
                } else YoYo.with(Techniques.Shake).duration(500).playOn(view);
            }
        });

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        ImageButton backArrow = root.findViewById(R.id.back_arrow_comments);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnBack();
            }
        });

        sharedViewModel.getCurWork().observe(getViewLifecycleOwner(), new Observer<Work>() {
            @Override
            public void onChanged(Work work) {
                LinearLayout ll = root.findViewById(R.id.linearlayout_comments);
                ll.removeAllViews();
                int i = 0;
                for (Comment c : work.getCommentsObjects()){
                    View v = inflater.inflate(R.layout.custom_comment, ll, false);
                    TextView author = v.findViewById(R.id.user_name_comment);
                    author.setText(c.getAuthor());
                    TextView content = v.findViewById(R.id.comment_content);
                    content.setText(c.getContent());
                    ImageView iv = v.findViewById(R.id.user_image_comment);
                    Picasso.get().load(c.getImage()).into(iv);
                    ll.addView(v);
                    i++;
                }
                if (i == 0) {
                    TextView newText = new TextView(getContext());
                    newText.setText("Wow such empty");
                    ll.addView(newText);
                }
            }
        });

        return root;
    }

    @Override
    public boolean OnBack() {
        getParentFragmentManager().beginTransaction().hide(sharedViewModel.getmCommentsFragment()).show(sharedViewModel.getmExpositionFragment()).commit();
        sharedViewModel.setActive(sharedViewModel.getmExpositionFragment());
        return true;
    }
}