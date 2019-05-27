package com.yudhapn.moviecatalogue.view;


import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yudhapn.moviecatalogue.util.NetworkReceiver;
import com.yudhapn.moviecatalogue.R;
import com.yudhapn.moviecatalogue.adapter.SimilarMovieAdapter;
import com.yudhapn.moviecatalogue.adapter.SimilarTvShowAdapter;
import com.yudhapn.moviecatalogue.model.Movie;
import com.yudhapn.moviecatalogue.model.TvShow;
import com.yudhapn.moviecatalogue.util.ItemClickSupport;
import com.yudhapn.moviecatalogue.util.function.RetrofitCallback;
import com.yudhapn.moviecatalogue.viewmodel.MainViewModel;

import java.util.Objects;

public class RelatedFragment extends Fragment implements RetrofitCallback, NetworkReceiver.NetworkCallback {
    private RecyclerView rvRelated;
    public static final String SIMILAR = "similar";
    private NetworkReceiver receiver;

    public RelatedFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_related, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvRelated = view.findViewById(R.id.rv_related);
        receiver = new NetworkReceiver(this);
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        Objects.requireNonNull(getActivity()).registerReceiver(receiver, filter);
        updateUI();
    }

    private void showSelectedMovie(Movie movie, View v) {
        DetailMovieFragmentDirections.ActionShowDetailMovie action = DetailMovieFragmentDirections.actionShowDetailMovie(movie);
        Navigation.findNavController(v).navigate(action);
    }

    private void showSelectedTvShow(TvShow tvShow, View v) {
        DetailTvShowFragmentDirections.ActionShowDetailTvshow action = DetailTvShowFragmentDirections.actionShowDetailTvshow(tvShow);
        Navigation.findNavController(v).navigate(action);
    }

    private void updateUI() {
        MainViewModel viewModel = ViewModelProviders.of(Objects.requireNonNull(getParentFragment())).get(MainViewModel.class);
        if (getParentFragment() instanceof DetailMovieFragment) {
            viewModel.getDetailMovie().observe(this, detailMovie -> viewModel.getSimilarMovie(SIMILAR, this, false, detailMovie.getId().toString())
                    .observe(this, movies -> {
                        SimilarMovieAdapter adapter = new SimilarMovieAdapter();
                        adapter.submitList(movies);
                        rvRelated.setLayoutManager(new GridLayoutManager(getContext(), 3));
                        rvRelated.setAdapter(adapter);
                        ItemClickSupport.addTo(rvRelated).setOnItemClickListener((recyclerView, position, v) -> {
                            if (movies != null) {
                                showSelectedMovie(movies.get(position), v);
                            }
                        });
                    }));
        } else {
            viewModel.getDetailTvShow().observe(this, detailTvShow ->
                    viewModel.getSimilarTvShow(SIMILAR, this, false, detailTvShow.getId().toString())
                            .observe(this, tvShows -> {
                                SimilarTvShowAdapter adapter = new SimilarTvShowAdapter();
                                adapter.submitList(tvShows);
                                rvRelated.setLayoutManager(new GridLayoutManager(getContext(), 3));
                                rvRelated.setAdapter(adapter);
                                ItemClickSupport.addTo(rvRelated).setOnItemClickListener((recyclerView, position, v) -> {
                                    if (tvShows != null) {
                                        showSelectedTvShow(tvShows.get(position), v);
                                    }
                                });
                            }));
        }
    }

    @Override
    public void onResult() {

    }

    @Override
    public void onConnected() {}

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
