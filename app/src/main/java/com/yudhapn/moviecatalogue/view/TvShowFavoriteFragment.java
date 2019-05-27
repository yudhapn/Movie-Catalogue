package com.yudhapn.moviecatalogue.view;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yudhapn.moviecatalogue.R;
import com.yudhapn.moviecatalogue.adapter.FavoriteAdapter;
import com.yudhapn.moviecatalogue.db.FavoriteHelper;
import com.yudhapn.moviecatalogue.model.Favorite;
import com.yudhapn.moviecatalogue.model.TvShow;
import com.yudhapn.moviecatalogue.util.ItemClickSupport;
import com.yudhapn.moviecatalogue.util.function.LoadFavoritesCallback;
import com.yudhapn.moviecatalogue.viewmodel.MainViewModel;

import java.util.List;
import java.util.Objects;

public class TvShowFavoriteFragment extends Fragment implements LoadFavoritesCallback {
    private static final String CATEGORY = "tvshow";
    private RecyclerView rvFavorite;
    private FavoriteHelper favoriteHelper;
    private FavoriteAdapter adapter;
    private List<Favorite> favorites;

    public TvShowFavoriteFragment() {
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tv_show_favorite, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvFavorite = view.findViewById(R.id.rv_fav_tvshow);
        rvFavorite.setLayoutManager(new LinearLayoutManager(getContext()));
        favoriteHelper = FavoriteHelper.getInstance(Objects.requireNonNull(getActivity()).getApplicationContext());
        favoriteHelper.open();
        adapter = new FavoriteAdapter();
        rvFavorite.setAdapter(adapter);
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getAllFavorites(favoriteHelper, this, CATEGORY);
    }

    private void updateUI() {
        adapter.setListNotes(favorites);
        adapter.notifyDataSetChanged();
        ItemClickSupport.addTo(rvFavorite).setOnItemClickListener((recyclerView, position, v) -> {
            if (favorites != null) {
                showSelectedTvShow(new TvShow(Integer.parseInt(favorites.get(position).getId())), v);
            }
        });
    }

    private void showSelectedTvShow(TvShow tvShow, View v) {
        FavoriteFragmentDirections.ActionShowDetailTvshow action = FavoriteFragmentDirections.actionShowDetailTvshow(tvShow);
        Navigation.findNavController(v).navigate(action);
    }

    @Override
    public void preExecute() {

    }

    @Override
    public void postExecute(List<Favorite> favorites) {
        this.favorites = favorites;
        updateUI();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        favoriteHelper.close();
    }
}
