package com.example.museaapplication.ui.dashboard;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.museaapplication.R;
import com.example.museaapplication.ui.PropersFragment;
import com.example.museaapplication.ui.user.UserFragment;
import com.google.android.material.tabs.TabLayout;

public class DashboardFragment extends Fragment {

    public static TabLayout tabLayout;
    public static ViewPager viewPager;
    private DashboardViewModel dashboardViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        viewPager = root.findViewById(R.id.view_pager);
        PagerAdapter pa = new PagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(pa);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tabLayout = view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }
}

// Since this is an object collection, use a FragmentStatePagerAdapter,
// and NOT a FragmentPagerAdapter.
class PagerAdapter extends FragmentPagerAdapter {

    private final Fragment f1 = new PropersFragment();;
    private final Fragment f2 = new PropersFragment();
    private final Fragment f3 = new UserFragment();

    FragmentManager fragmentManager;


    public PagerAdapter(FragmentManager fm) {
        super(fm);
        fragmentManager = fm;
    }

    @Override
    public Fragment getItem(int i) {

        Fragment f = new Fragment();
        switch (i){
            case 0:
                 f = f2;
                 break;
            case 1:
                 f = f1;
                 break;
            case 2:
                f = f3;
                break;
            default:
                throw new IllegalArgumentException();
        }
        return f;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String s = "";
        switch (position){
            case 0:
                s = "Propers";
                break;
            case 1:
                s = "Pais";
                break;

            case 2:
                s = "Favorits";
                break;
        }
        return s;
    }
    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
    }
    @Override
    public Parcelable saveState() {
        // Do Nothing
        return null;
    }

}