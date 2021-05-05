package com.example.museaapplication.ui;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.museaapplication.Classes.APIRequests;
import com.example.museaapplication.Classes.Dominio.Comment;
import com.example.museaapplication.Classes.Dominio.Work;
import com.example.museaapplication.Classes.Json.CommentsValue;
import com.example.museaapplication.Classes.OnBackPressed;
import com.example.museaapplication.Classes.RetrofitClient;
import com.example.museaapplication.Classes.ViewModels.SharedViewModel;
import com.example.museaapplication.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

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
    SwipeRefreshLayout refresLayout;
    int progress;

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
        refresLayout = root.findViewById(R.id.refresh_layout_comments);

        refresLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Work w = sharedViewModel.getCurWork().getValue();
                    sharedViewModel.getCurWork().getValue().setCommentsObjects(new ArrayList<>());
                Call<CommentsValue> call = RetrofitClient.getInstance().getMyApi().getComments(w.get_id());
                call.enqueue(new Callback<CommentsValue>() {
                    @Override
                    public void onResponse(Call<CommentsValue> call, Response<CommentsValue> response) {
                        if (response.body() != null) {
                            Comment[] comments = response.body().getComments();
                            for (Comment c : comments){
                                Log.d("Comment", c.getContent() + " " + w.get_id());
                                w.addComment(c);
                            }
                        }
                        sharedViewModel.setCurWork(w);
                        refresLayout.setRefreshing(false);
                    }

                    @Override
                    public void onFailure(Call<CommentsValue> call, Throwable t) {
                        Log.e("TAG1Comment", t.getLocalizedMessage());
                        Log.e("TAG2Comment", t.getMessage());

                        t.printStackTrace();
                        refresLayout.setRefreshing(false);
                    }
                });
                //sharedViewModel.setCurWork(null);

            }
        });

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!newCommentText.getText().toString().equals("")) {
                    YoYo.with(Techniques.SlideInLeft).duration(200).playOn(view);
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
                    SeekBar seekBar = v.findViewById(R.id.seek_bar_comment);

                    seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                            Drawable clone = getResources().getDrawable(R.drawable.ic_round_delete_outline_24).mutate();
                            //clone.setColorFilter(Color.RED, PorterDuff.Mode.CLEAR);
                            seekBar.setThumb(clone);
                            seekBar.getThumb().setAlpha(i);
                            seekBar.setProgress(i);
                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {
                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {
                            if (seekBar.getProgress() <= 150) {
                                seekBar.setProgress(0);
                            }else{
                                seekBar.setProgress(255);
                                // Aqui borramos
                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                builder.setCancelable(true);
                                builder.setTitle("Delete");
                                builder.setMessage("Are you sure you want to delete?");
                                builder.setPositiveButton("Confirm",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Call<Void> call = RetrofitClient.getInstance().getMyApi().deleteComment(c.get_id());
                                                call.enqueue(new Callback<Void>() {
                                                    @Override
                                                    public void onResponse(Call<Void> call, Response<Void> response) {
                                                        if (response.code() == 200) {
                                                            ll.removeView(v);
                                                            sharedViewModel.getCurWork().getValue().removeComment(c);
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Call<Void> call, Throwable t) {

                                                    }
                                                });
                                            }
                                        });
                                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        seekBar.setProgress(0);
                                    }
                                });
                                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialogInterface) {
                                        seekBar.setProgress(0);
                                    }
                                });

                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }
                        }
                    });
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