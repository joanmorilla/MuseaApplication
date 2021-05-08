package com.example.museaapplication.ui;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.widget.Adapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.museaapplication.Classes.DepthPageTransformer;
import com.example.museaapplication.Classes.Dominio.Exhibition;
import com.example.museaapplication.Classes.Dominio.Work;
import com.example.museaapplication.Classes.OnBackPressed;
import com.example.museaapplication.Classes.SingletonDataHolder;
import com.example.museaapplication.Classes.ViewModels.SharedViewModel;
import com.example.museaapplication.R;
import com.github.siyamed.shapeimageview.mask.PorterShapeImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExpositionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExpositionFragment extends Fragment implements OnBackPressed {

    Exhibition curExpo;
    TextView txt;
    View root;
    Boolean love = false;
    ViewPager viewPager2;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    SharedViewModel sharedViewModel;

    public ExpositionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ExpositionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ExpositionFragment newInstance(String param1, String param2) {
        ExpositionFragment fragment = new ExpositionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_exposition, container, false);

        txt = root.findViewById(R.id.expo_title);
        viewPager2 = root.findViewById(R.id.view_pager_works);

        ImageButton backArrow = root.findViewById(R.id.back_arrow_work);
        backArrow.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
            @Override
            public WindowInsets onApplyWindowInsets(View v, WindowInsets insets) {
                ((ViewGroup.MarginLayoutParams) v.getLayoutParams()).topMargin = insets.getSystemWindowInsetTop();
                return insets;
            }
        });
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnBack();
            }
        });

        viewPager2.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
            @Override
            public WindowInsets onApplyWindowInsets(View v, WindowInsets insets) {
                ((ViewGroup.MarginLayoutParams) v.getLayoutParams()).bottomMargin = insets.getSystemWindowInsetBottom();
                return insets;
            }
        });
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        sharedViewModel.getCurExposition().observe(getViewLifecycleOwner(), new Observer<Exhibition>() {
            @Override
            public void onChanged(Exhibition exhibition) {
                ImageView imageView = root.findViewById(R.id.image_holder_expo);
                Picasso.get().load(validateUrl(exhibition.getImage())).centerCrop().fit().into(imageView);
                txt.setText(exhibition.getName());
                ArrayList<Work> works = exhibition.getWorkObjects();
                Log.e("ExpoSal", "" + exhibition);
                if (exhibition.getWorkObjects() != null) {
                    MyViewPagerAdapter adapter = new MyViewPagerAdapter(getContext(), works, sharedViewModel, getParentFragmentManager());
                    viewPager2.setPageTransformer(true, new DepthPageTransformer());
                    viewPager2.setAdapter(adapter);
                }else{
                    viewPager2.setAdapter(null);
                }
                /*LinearLayout rl = root.findViewById(R.id.linear_layout_expo);
                View v = getLayoutInflater().inflate(R.layout.work_card_layout, rl);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(pixToDp(450), pixToDp(600));
                params.setMargins(pixToDp(10), pixToDp(10), pixToDp(10), 0);
                params.addRule(RelativeLayout.BELOW, txt.getId());
                v.setLayoutParams(params);
                ImageButton ib = v.findViewById(R.id.heart_button_work);
                ib.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("UseCompatLoadingForDrawables")
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onClick(View v) {
                        love = !love;
                        if (love) v.setBackground(getContext().getDrawable(R.drawable.ic_baseline_favorite_24));
                        else v.setBackground(getContext().getDrawable(R.drawable.ic_baseline_favorite_border_24));
                        YoYo.with(Techniques.ZoomIn).duration(300).playOn(ib);
                    }
                });
                ImageView imageHolder = v.findViewById(R.id.image_holder_work);
                Picasso.get().load(validateUrl(exhibition.getWorkObjects().get(0).getImage())).centerCrop().fit().into(imageHolder);*/
            }
        });

        return root;
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                getParentFragmentManager().beginTransaction().hide(sharedViewModel.getmExpositionFragment()).show(sharedViewModel.getmMuseoFragment()).commit();
                //getActivity().setTitle(sharedViewModel.getCurMuseo().getName());
                sharedViewModel.setActive(sharedViewModel.getmMuseoFragment());
                return true;
            case R.id.mybutton:
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public boolean OnBack() {
        getParentFragmentManager().beginTransaction().hide(sharedViewModel.getmExpositionFragment()).show(sharedViewModel.getmMuseoFragment()).commit();
        //getActivity().setTitle(sharedViewModel.getCurMuseo().getName());
        sharedViewModel.setActive(sharedViewModel.getmMuseoFragment());
        return true;
    }

    private String validateUrl(String url) {
        if (!url.contains("https")) url = url.replace("http", "https");
        return url;
    }

    int pixToDp(int value) {
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, root.getResources().getDisplayMetrics()));
    }
}

class MyViewPagerAdapter extends PagerAdapter {
    private Context context;
    private LayoutInflater inflater;
    private FragmentManager fm;
    private ArrayList<Work> works = new ArrayList<>();
    private boolean love = false;
    private SharedViewModel sharedViewModel;


    public MyViewPagerAdapter(Context c, ArrayList<Work> w, SharedViewModel svm, FragmentManager fragm) {
        context = c;
        works = w;
        inflater = LayoutInflater.from(context);
        sharedViewModel = svm;
        fm = fragm;
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        /*View v = views.get (position);
        container.addView (v, 0);/*/
        container.setBackground(new ColorDrawable(Color.TRANSPARENT));
        ViewGroup v = (ViewGroup) inflater.inflate(R.layout.work_card_layout, container, false);
        ImageButton ib = v.findViewById(R.id.heart_button_work);
        ImageButton ib2 = v.findViewById(R.id.comments_button_work);
        // Is it liked already
        if (works.get(position).isLoved()) ib.setBackground(context.getDrawable(R.drawable.ic_baseline_favorite_24));
        else
            ib.setBackground(context.getDrawable(R.drawable.ic_baseline_favorite_border_24));
        ib.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                //SingletonDataHolder.userViewModel.loadlikes();
                sharedViewModel.likeWork(works.get(position).get_id());
                if (works.get(position).likeWork()) {
                    v.setBackground(context.getDrawable(R.drawable.ic_baseline_favorite_24));
                }
                else
                    v.setBackground(context.getDrawable(R.drawable.ic_baseline_favorite_border_24));
                YoYo.with(Techniques.ZoomIn).duration(300).playOn(ib);
            }
        });
        ib2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedViewModel.setCurWork(works.get(position));
                sharedViewModel.setActive(sharedViewModel.getmCommentsFragment());
                fm.beginTransaction().hide(sharedViewModel.getmExpositionFragment()).show(sharedViewModel.getmCommentsFragment()).commit();
            }
        });
        TextView title = v.findViewById(R.id.title_text_work);
        SpannableString content = new SpannableString(works.get(position).getTitle());
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        title.setText(content);
        title = v.findViewById(R.id.desc_text_work);
        title.setText(works.get(position).getDescriptions().getText());
        title = v.findViewById(R.id.text_author_work);
        title.setText(works.get(position).getAuthor());
        ImageView imageHolder = v.findViewById(R.id.image_holder_work);
        imageHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog(validateUrl(works.get(position).getImage()));
            }
        });

        Picasso.get().load(validateUrl(works.get(position).getImage())).fit().into(imageHolder);
        //imageHolder.setImageBitmap(draw);

        container.addView(v);
        return v;
    }

    /*@Override
    public int getItemPosition(@NonNull Object object) {
        int index = views.indexOf (object);
        if (index == -1)
            return POSITION_NONE;
        else
            return index;
    }*/

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return works.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    private void openDialog(String imageUrl) {
        Dialog dialog = new Dialog(context);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View v = dialog.getLayoutInflater().inflate(R.layout.tap_work_dialog, null);
        ImageView image = v.findViewById(R.id.dialog_work_image);
        Picasso.get().load(imageUrl).rotate(90).fit().into(image);
        dialog.setContentView(v);
        dialog.show();
    }

    private String validateUrl(String url) {
        if (!url.contains("https")) url = url.replace("http", "https");
        return url;
    }
    int pixToDp(int value){
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, context.getResources().getDisplayMetrics()));
    }
}