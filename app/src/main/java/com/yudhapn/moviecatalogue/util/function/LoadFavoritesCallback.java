package com.yudhapn.moviecatalogue.util.function;

import com.yudhapn.moviecatalogue.model.Favorite;

import java.util.List;

public interface LoadFavoritesCallback {
    void preExecute();
    void postExecute(List<Favorite> favorites);
}
