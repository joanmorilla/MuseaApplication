package com.example.museaapplication.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Vibrator;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

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
    Vibrator vib;

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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    @SuppressLint("ResourceType")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_comentaris, container, false);
        vib = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        TextView newText = root.findViewById(R.id.such_empty_text);
        newCommentText = root.findViewById(R.id.comment_input);
        postButton = root.findViewById(R.id.post_comment_button);
        refresLayout = root.findViewById(R.id.refresh_layout_comments);
        RelativeLayout bottompannel = root.findViewById(R.id.bottom_panel);

        bottompannel.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
            @Override
            public WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
                ((ViewGroup.MarginLayoutParams) view.getLayoutParams()).bottomMargin = windowInsets.getSystemWindowInsetBottom();
                return windowInsets;
            }
        });

        refresLayout.setColorSchemeColors(Color.BLUE, Color.BLACK, Color.RED);
        //refresLayout.setProgressBackgroundColorSchemeResource(R.attr.colorSurface);
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
                //vib.vibrate(50);

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
                for (int index = work.getCommentsObjects().size()-1; index >= 0; index--){
                    Comment c = work.getCommentsObjects().get(index);
                    View v = inflater.inflate(R.layout.custom_comment, ll, false);
                    TextView author = v.findViewById(R.id.user_name_comment);
                    author.setText(c.getAuthor());
                    TextView content = v.findViewById(R.id.comment_content);
                    content.setText(c.getContent());
                    ImageView iv = v.findViewById(R.id.user_image_comment);
                    Picasso.get().load(c.getImage()).into(iv);
                    SeekBar seekBar = v.findViewById(R.id.seek_bar_comment);
                    SeekBar seekBarReport = v.findViewById(R.id.seek_bar_report_comment);
                    TextView elapsed = v.findViewById(R.id.elapsed_time_comment);
                    // Different behaviour for current user's comments and others'
                    if (!c.getAuthor().equals("user1")){
                        seekBar.setEnabled(false);
                        seekBarReport.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                            @Override
                            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                                Drawable clone = getResources().getDrawable(R.drawable.custom_thumb_report).mutate();
                                int color = Color.argb(seekBar.getProgress() / 2, 232, 220, 56);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    v.setBackgroundTintList(ColorStateList.valueOf(color));
                                }
                                //v.setBackground(new ColorDrawable(color));
                                //clone.setColorFilter(Color.RED, PorterDuff.Mode.CLEAR);
                                seekBar.setThumb(clone);
                                seekBar.getThumb().setAlpha(seekBar.getProgress());
                            }

                            @Override
                            public void onStartTrackingTouch(SeekBar seekBar) {

                            }

                            @Override
                            public void onStopTrackingTouch(SeekBar seekBar) {
                                if (seekBar.getProgress() <= 175) {
                                    seekBar.setProgress(0);
                                } else {
                                    seekBar.setProgress(0);
                                    Toast.makeText(getContext(), "Baneaso Perro", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }else {
                        author.setText("You");
                        seekBarReport.setEnabled(false);
                        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                            @SuppressLint({"UseCompatLoadingForColorStateLists", "ResourceType"})
                            @Override
                            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                                Drawable clone = getResources().getDrawable(R.drawable.custom_thumb).mutate();
                                int color = Color.argb(seekBar.getProgress() / 2, 224, 86, 76);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    v.setBackgroundTintList(ColorStateList.valueOf(color));
                                }
                                //v.setBackground(new ColorDrawable(color));
                                //clone.setColorFilter(Color.RED, PorterDuff.Mode.CLEAR);
                                seekBar.setThumb(clone);
                                seekBar.getThumb().setAlpha(seekBar.getProgress());
                                //seekBar.setProgress(i);
                            }

                            @Override
                            public void onStartTrackingTouch(SeekBar seekBar) {
                            }

                            @Override
                            public void onStopTrackingTouch(SeekBar seekBar) {
                                if (seekBar.getProgress() <= 175) {
                                    seekBar.setProgress(0);
                                } else {
                                    seekBar.setProgress(255);
                                    // Aqui borramos
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                    builder.setCancelable(true);
                                    builder.setTitle(R.string.delete_dialog);
                                    builder.setMessage(R.string.delete_confirmation);
                                    builder.setPositiveButton(R.string.confirm,
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Call<Void> call = RetrofitClient.getInstance().getMyApi().deleteComment(c.get_id());
                                                    call.enqueue(new Callback<Void>() {
                                                        @Override
                                                        public void onResponse(Call<Void> call, Response<Void> response) {
                                                            if (response.code() == 200) {
                                                                ll.removeView(v);
                                                                newText.setVisibility(View.VISIBLE);
                                                                sharedViewModel.getCurWork().getValue().removeComment(c);
                                                            }
                                                        }

                                                        @Override
                                                        public void onFailure(Call<Void> call, Throwable t) {

                                                        }
                                                    });
                                                }
                                            });
                                    builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
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
                    }
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd,HH:mm:ss");
                    Date date = null;
                    try {
                        Date now = Calendar.getInstance().getTime();
                        date = sdf.parse(c.getDatetime().replace("T", ","));
                        Log.d("Timezone", "" + TimeZone.getDefault().getRawOffset());
                        long res = now.getTime() - date.getTime();
                        elapsed.setText(elapsedTime(res));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    ll.addView(v);
                    i++;
                }

                newText.setVisibility(View.GONE);
                if (i == 0) {
                    newText.setVisibility(View.VISIBLE);
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
    private String elapsedTime(long milisec){
        milisec -= TimeZone.getDefault().getOffset(System.currentTimeMillis());
        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;
        long monthsInMilli = daysInMilli * 30;
        Log.d("Milisecs", ""+milisec);

        long elapsedMonths = milisec / monthsInMilli;
        milisec = milisec % monthsInMilli;

        long elapsedDays = milisec / daysInMilli;
        milisec = milisec % daysInMilli;

        long elapsedHours = milisec / hoursInMilli;
        milisec = milisec % hoursInMilli;

        long elapsedMinutes = milisec / minutesInMilli;
        milisec = milisec % minutesInMilli;

        long elapsedSeconds = milisec / secondsInMilli;

        if (elapsedDays == 0){
            if (elapsedHours == 0){
                if (elapsedMinutes == 0){
                    return elapsedSeconds + "s";
                }
                return elapsedMinutes + "m";
            }
            return elapsedHours + "h";
        }
        return elapsedDays + "d";
    }
}