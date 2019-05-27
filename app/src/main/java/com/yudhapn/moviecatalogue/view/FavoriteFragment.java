package com.yudhapn.moviecatalogue.view;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.yudhapn.moviecatalogue.R;
import com.yudhapn.moviecatalogue.adapter.FixedTabsPagerAdapter;

public class FavoriteFragment extends Fragment {
    public static final String TAG = "FavoriteFragment";
    private ViewPager viewPager;
    private TabLayout tabLayout;

    public FavoriteFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorite, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewPager = view.findViewById(R.id.favorite_view_pager);
        tabLayout = view.findViewById(R.id.favorite_tab_layout);
        setUpViewPager();
    }

    private void setUpViewPager() {
        FixedTabsPagerAdapter adapter = new FixedTabsPagerAdapter(getChildFragmentManager(), getContext(), TAG);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}