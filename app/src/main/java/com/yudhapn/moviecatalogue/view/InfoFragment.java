package com.yudhapn.moviecatalogue.view;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.yudhapn.moviecatalogue.util.NetworkReceiver;
import com.yudhapn.moviecatalogue.R;
import com.yudhapn.moviecatalogue.adapter.CastAdapter;
import com.yudhapn.moviecatalogue.databinding.FragmentInfoMovieBinding;
import com.yudhapn.moviecatalogue.model.Genre;
import com.yudhapn.moviecatalogue.model.SpokenLanguage;
import com.yudhapn.moviecatalogue.util.function.RetrofitCallback;
import com.yudhapn.moviecatalogue.viewmodel.MainViewModel;

import java.util.Objects;

public class InfoFragment extends Fragment implements RetrofitCallback, NetworkReceiver.NetworkCallback {
    private FragmentInfoMovieBinding binding;
    private RecyclerView rvCast;
    public final static String INFO_MOVIE = "info_movie";
    public final static String INFO_TVSHOW = "info_tv";
    private ShimmerFrameLayout shimmer;
    private ConstraintLayout layout;
    private NetworkReceiver receiver;

    public InfoFragment() {
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_info_movie, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvCast = view.findViewById(R.id.rv_cast);
        shimmer = view.findViewById(R.id.shimmerLayout);
        layout = view.findViewById(R.id.root_layout);
        receiver = new NetworkReceiver(this);
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        Objects.requireNonNull(getActivity()).registerReceiver(receiver, filter);
        shimmer.startShimmer();
    }

    private void updateUI() {
        MainViewModel viewModel = ViewModelProviders.of(Objects.requireNonNull(getParentFragment())).get(MainViewModel.class);
        if (getParentFragment() instanceof DetailMovieFragment) {
            viewModel.getDetailMovie().observe(this, detailMovie -> {
                binding.setDetailMovie(detailMovie);
                StringBuilder genreStr = new StringBuilder();
                StringBuilder languageStr = new StringBuilder();
                for (Genre genre : detailMovie.getGenres()) {
                    genreStr.append(genre.getName()).append(", ");
                }
                for (SpokenLanguage language : detailMovie.getSpokenLanguages()) {
                    languageStr.append(language.getName()).append(", ");
                }
                languageStr.substring(0, (languageStr.length() - 2));
                binding.setGenre(genreStr.toString());
                binding.setLanguage(languageStr.toString());
                viewModel.getCast(detailMovie.getId().toString(), "", this, INFO_MOVIE).observe(this, casts -> {
                    CastAdapter adapter = new CastAdapter();
                    adapter.submitList(casts);
                    rvCast.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                    rvCast.setAdapter(adapter);
                });
            });
        } else {
            viewModel.getDetailTvShow().observe(this, detailTvShow -> {
                binding.setDetailTvShow(detailTvShow);
                StringBuilder genreStr = new StringBuilder();
                StringBuilder languageStr = new StringBuilder();
                for (Genre genre : detailTvShow.getGenres()) {
                    genreStr.append(genre.getName()).append(", ");
                }
                for (String language : detailTvShow.getLanguages()) {
                    languageStr.append(language).append(", ");
                }
                languageStr.substring(0, (languageStr.length() - 2));
                binding.setGenre(genreStr.toString());
                binding.setLanguage(languageStr.toString());
                viewModel.getCast("", detailTvShow.getId().toString(), this, INFO_TVSHOW).observe(this, casts -> {
                    CastAdapter adapter = new CastAdapter();
                    adapter.submitList(casts);
                    rvCast.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                    rvCast.setAdapter(adapter);
                });
            });
        }
    }

    @Override
    public void onConnected() {
        updateUI();
    }

    @Override
    public void onDisconnected() {
    }

    @Override
    public void onResult() {
        shimmer.stopShimmer();
        shimmer.setVisibility(View.GONE);
        layout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            Objects.requireNonNull(getActivity()).unregisterReceiver(receiver);
        }
    }
}
