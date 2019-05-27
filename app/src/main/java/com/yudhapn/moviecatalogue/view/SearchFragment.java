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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.yudhapn.moviecatalogue.R;
import com.yudhapn.moviecatalogue.adapter.SearchAdapter;
import com.yudhapn.moviecatalogue.util.NetworkReceiver;
import com.yudhapn.moviecatalogue.util.function.RetrofitCallback;
import com.yudhapn.moviecatalogue.viewmodel.MainViewModel;

import java.util.Objects;

public class SearchFragment extends Fragment implements RetrofitCallback, NetworkReceiver.NetworkCallback {
    private RecyclerView rvSearch;
    private ShimmerFrameLayout shimmer;
    private NetworkReceiver receiver;
    private MainViewModel viewModel;

    public SearchFragment() {
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvSearch = view.findViewById(R.id.rv_search);
        shimmer = view.findViewById(R.id.shimmerLayout);
        receiver = new NetworkReceiver(this);
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        Objects.requireNonNull(getActivity()).registerReceiver(receiver, filter);
        viewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
    }

    private void updateUI() {
        viewModel.getQuerySearch().observe(this, query -> viewModel.getSearchResult(query, this).observe(this, searches -> {
            if (!query.equalsIgnoreCase("")) {
                shimmer.setVisibility(View.VISIBLE);
                shimmer.startShimmer();
                SearchAdapter adapter = new SearchAdapter();
                adapter.submitList(searches);
                rvSearch.setLayoutManager(new LinearLayoutManager(getContext()));
                rvSearch.setAdapter(adapter);
            }
        }));
    }

    @Override
    public void onResult() {
        shimmer.stopShimmer();
        shimmer.setVisibility(View.GONE);
        rvSearch.setVisibility(View.VISIBLE);
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
        viewModel.setQuerySearch("");
        if (receiver != null) {
            Objects.requireNonNull(getActivity()).unregisterReceiver(receiver);
        }
    }
}
