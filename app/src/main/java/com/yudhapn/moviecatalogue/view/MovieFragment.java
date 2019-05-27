package com.yudhapn.moviecatalogue.view;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.yudhapn.moviecatalogue.util.NetworkReceiver;
import com.yudhapn.moviecatalogue.R;
import com.yudhapn.moviecatalogue.adapter.PopularMovieAdapter;
import com.yudhapn.moviecatalogue.model.Movie;
import com.yudhapn.moviecatalogue.util.ItemClickSupport;
import com.yudhapn.moviecatalogue.util.function.RetrofitCallback;
import com.yudhapn.moviecatalogue.viewmodel.MainViewModel;

import java.util.Objects;

public class MovieFragment extends Fragment implements RetrofitCallback, NetworkReceiver.NetworkCallback {
    public static final String POPULAR = "popular_movies";
    public static final String UPCOMING = "upcoming_movies";
    private RecyclerView rvTheaterMovie, rvPopularMovie;
    private LinearLayout layout;
    private ShimmerFrameLayout shimmer;
    private NetworkReceiver receiver;

    public MovieFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movie, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvPopularMovie = view.findViewById(R.id.rv_popular);
        rvTheaterMovie = view.findViewById(R.id.rv_theater);
        shimmer = view.findViewById(R.id.shimmerLayout);
        layout = view.findViewById(R.id.root_layout);
        receiver = new NetworkReceiver(this);
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        Objects.requireNonNull(getActivity()).registerReceiver(receiver, filter);
        shimmer.startShimmer();
    }

    private void initAdapter() {
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        viewModel.getUpcomingMovie(UPCOMING, this, false).observe(this, movies -> {
            PopularMovieAdapter adapter = new PopularMovieAdapter();
            adapter.submitList(movies);
            rvTheaterMovie.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            rvTheaterMovie.setAdapter(adapter);
            ItemClickSupport.addTo(rvTheaterMovie).setOnItemClickListener((recyclerView, position, v) -> showSelectedMovie(movies.get(position), v));
        });
        viewModel.getPopularMovie(POPULAR, this, false).observe(this, movies -> {
            PopularMovieAdapter adapter = new PopularMovieAdapter();
            adapter.submitList(movies);
            rvPopularMovie.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            rvPopularMovie.setAdapter(adapter);
            ItemClickSupport.addTo(rvPopularMovie).setOnItemClickListener((recyclerView, position, v) -> showSelectedMovie(movies.get(position), v));
        });
    }

    private void showSelectedMovie(Movie movie, View v) {
        MovieFragmentDirections.ActionShowDetailMovie action = MovieFragmentDirections.actionShowDetailMovie(movie);
        Navigation.findNavController(v).navigate(action);
    }

    @Override
    public void onResult() {
        shimmer.stopShimmer();
        shimmer.setVisibility(View.GONE);
        layout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onConnected() {
        initAdapter();
    }

    @Override
    public void onDisconnected() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            Objects.requireNonNull(getActivity()).unregisterReceiver(receiver);
        }
    }
}