package com.yudhapn.moviecatalogue.view;


import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.yudhapn.moviecatalogue.util.NetworkReceiver;
import com.yudhapn.moviecatalogue.R;
import com.yudhapn.moviecatalogue.adapter.FixedTabsPagerAdapter;
import com.yudhapn.moviecatalogue.databinding.FragmentDetailMovieBinding;
import com.yudhapn.moviecatalogue.db.FavoriteHelper;
import com.yudhapn.moviecatalogue.model.Genre;
import com.yudhapn.moviecatalogue.model.Movie;
import com.yudhapn.moviecatalogue.util.function.RetrofitCallback;
import com.yudhapn.moviecatalogue.viewmodel.MainViewModel;

import java.util.Objects;

public class DetailMovieFragment extends Fragment implements RetrofitCallback, NetworkReceiver.NetworkCallback {
    private static final String TAG = "DetailMovieFragment";
    private FragmentDetailMovieBinding binding;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private MaterialButton likeButton;
    private FavoriteHelper favoriteHelper;
    private ShimmerFrameLayout shimmer;
    private AppBarLayout appBarLayout;
    private NetworkReceiver receiver;
    private MainViewModel viewModel;

    public DetailMovieFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail_movie, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewPager = view.findViewById(R.id.movie_view_pager);
        tabLayout = view.findViewById(R.id.movie_tab_layout);
        favoriteHelper = FavoriteHelper.getInstance(Objects.requireNonNull(getActivity()).getApplicationContext());
        favoriteHelper.open();
        likeButton = view.findViewById(R.id.like_button);
        receiver = new NetworkReceiver(this);
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        Objects.requireNonNull(getActivity()).registerReceiver(receiver, filter);
        shimmer = view.findViewById(R.id.shimmerLayout);
        appBarLayout = view.findViewById(R.id.appBarMovieLayout);
        shimmer.startShimmer();
    }

    private void updateUI() {
        assert getArguments() != null;
        Movie movie = DetailMovieFragmentArgs.fromBundle(getArguments()).getMovieArgs();
        viewModel.getDetailMovie(favoriteHelper, movie.getId().toString(), this).observe(this, detailMovie -> {
            binding.setDetailMovie(detailMovie);
            StringBuilder genreStr = new StringBuilder();
            for (Genre genre : detailMovie.getGenres()) {
                genreStr.append(genre.getName()).append(", ");
            }
            genreStr.substring(0, (genreStr.length() - 2));
            binding.setGenre(genreStr.toString());
            Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity()))
                    .getSupportActionBar()).setTitle(detailMovie.getTitle());
            likeButton.setOnClickListener(v -> viewModel.setFavorite(favoriteHelper, detailMovie));
            viewModel.isFavorite().observe(this, binding::setIsFavorite);
        });
        setUpViewPager();
    }

    private void setUpViewPager() {
        FixedTabsPagerAdapter adapter = new FixedTabsPagerAdapter(getChildFragmentManager(), getContext(), TAG);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onResult() {
        shimmer.stopShimmer();
        shimmer.setVisibility(View.GONE);
        appBarLayout.setVisibility(View.VISIBLE);

    }

    @Override
    public void onConnected() {
        updateUI();
    }

    @Override
    public void onDisconnected() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        favoriteHelper.close();
        if (receiver != null) {
            Objects.requireNonNull(getActivity()).unregisterReceiver(receiver);
        }
    }
}