package com.yudhapn.moviecatalogue.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.yudhapn.moviecatalogue.R;
import com.yudhapn.moviecatalogue.view.FavoriteFragment;
import com.yudhapn.moviecatalogue.view.InfoFragment;
import com.yudhapn.moviecatalogue.view.MovieFavoriteFragment;
import com.yudhapn.moviecatalogue.view.MovieFragment;
import com.yudhapn.moviecatalogue.view.RelatedFragment;
import com.yudhapn.moviecatalogue.view.TvShowFavoriteFragment;

public class FixedTabsPagerAdapter extends FragmentPagerAdapter {
    private Context context;
    private String tag;
    public FixedTabsPagerAdapter(@NonNull FragmentManager fm, Context context, String tag) {
        super(fm);
        this.context = context;
        this.tag = tag;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (tag.equalsIgnoreCase(FavoriteFragment.TAG)) {
            switch (position) {
                case 0:
                    return new MovieFavoriteFragment();
                case 1:
                    return new TvShowFavoriteFragment();
                default:
                    return new MovieFavoriteFragment();
            }
        } else {
            switch (position) {
                case 0:
                    return new InfoFragment();
                case 1:
                    return new RelatedFragment();
                default:
                    return new MovieFragment();
            }
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (tag.equalsIgnoreCase(FavoriteFragment.TAG)) {
            switch (position) {
                case 0:
                    return context.getString(R.string.movies);
                case 1:
                    return context.getString(R.string.tvshows);
                default:
                    return null;
            }
        } else {
            switch (position) {
                case 0:
                    return context.getString(R.string.info);
                case 1:
                    return context.getString(R.string.related);
                default:
                    return null;
            }
        }
    }
}
