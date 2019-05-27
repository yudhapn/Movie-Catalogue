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
import com.yudhapn.moviecatalogue.adapter.PopularShowAdapter;
import com.yudhapn.moviecatalogue.model.TvShow;
import com.yudhapn.moviecatalogue.util.ItemClickSupport;
import com.yudhapn.moviecatalogue.util.function.RetrofitCallback;
import com.yudhapn.moviecatalogue.viewmodel.MainViewModel;

import java.util.Objects;

public class TVShowFragment extends Fragment implements RetrofitCallback, NetworkReceiver.NetworkCallback {
    public static final String POPULAR = "popular_tvshow";
    public static final String UPCOMING = "upcoming_tvshow";
    private RecyclerView rvRatedShow, rvPopularShow;
    private ShimmerFrameLayout shimmer;
    private LinearLayout layout;
    private NetworkReceiver receiver;

    public TVShowFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tvshow, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvPopularShow = view.findViewById(R.id.rv_popular_show);
        rvRatedShow = view.findViewById(R.id.rv_rated_show);
        shimmer = view.findViewById(R.id.shimmerLayout);
        layout = view.findViewById(R.id.root_layout);
        receiver = new NetworkReceiver(this);
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        Objects.requireNonNull(getActivity()).registerReceiver(receiver, filter);
        shimmer.startShimmer();
    }

    private void initAdapter() {
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getPopularTv(POPULAR, this, false).observe(this, tvShows -> {
            PopularShowAdapter adapter = new PopularShowAdapter();
            adapter.submitList(tvShows);
            rvPopularShow.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            rvPopularShow.setAdapter(adapter);
            ItemClickSupport.addTo(rvPopularShow).setOnItemClickListener((recyclerView, position, v) -> {
                if (tvShows != null) {
                    showSelectedShow(tvShows.get(position), v);
                }
            });
        });

        viewModel.getTopTv(UPCOMING, this, false).observe(this, tvShows -> {
            PopularShowAdapter adapter = new PopularShowAdapter();
            adapter.submitList(tvShows);
            rvRatedShow.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            rvRatedShow.setAdapter(adapter);
            ItemClickSupport.addTo(rvRatedShow).setOnItemClickListener((recyclerView, position, v) -> {
                if (tvShows != null) {
                    showSelectedShow(tvShows.get(position), v);
                }
            });
        });
    }

    private void showSelectedShow(TvShow tvShow, View v) {
        TVShowFragmentDirections.ActionShowDetailTvshow action = TVShowFragmentDirections.actionShowDetailTvshow(tvShow);
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