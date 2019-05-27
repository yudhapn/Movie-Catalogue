package com.yudhapn.moviecatalogue.util;

import android.os.AsyncTask;

import com.yudhapn.moviecatalogue.db.FavoriteHelper;
import com.yudhapn.moviecatalogue.model.Favorite;
import com.yudhapn.moviecatalogue.util.function.LoadFavoritesCallback;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class LoadFavoritesAsync extends AsyncTask<Void, Void, List<Favorite>> {
    private final WeakReference<FavoriteHelper> weakFavoriteHelper;
    private final WeakReference<LoadFavoritesCallback> weakCallback;
    private String category;

    public LoadFavoritesAsync(FavoriteHelper favoriteHelper, LoadFavoritesCallback callback, String category) {
        weakFavoriteHelper = new WeakReference<>(favoriteHelper);
        weakCallback = new WeakReference<>(callback);
        this.category = category;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        weakCallback.get().preExecute();
    }

    @Override
    protected List<Favorite> doInBackground(Void... voids) {
        List<Favorite> list = weakFavoriteHelper.get().getAllFavorites();
        List<Favorite> favorites = new ArrayList<>();
        for (Favorite favorite : list) {
            if (favorite.getCategory().equalsIgnoreCase(category)) {
                favorites.add(favorite);
            }
        }
        return favorites;
    }

    @Override
    protected void onPostExecute(List<Favorite> favorites) {
        super.onPostExecute(favorites);
        weakCallback.get().postExecute(favorites);
    }
}
