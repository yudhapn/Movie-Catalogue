package com.yudhapn.moviecatalogue.repository.cast;

import androidx.annotation.NonNull;
import androidx.paging.DataSource;

import com.yudhapn.moviecatalogue.api.TMDBApi;
import com.yudhapn.moviecatalogue.model.Cast;
import com.yudhapn.moviecatalogue.util.function.RetrofitCallback;

public class CastDataSourceFactory extends DataSource.Factory<Integer, Cast> {
    private String movie_id;
    private String tvShow_id;
    private RetrofitCallback callback;
    private String request;

    public CastDataSourceFactory(String movie_id, String tvShow_id, RetrofitCallback callback, String request) {
        this.movie_id = movie_id;
        this.tvShow_id = tvShow_id;
        this.callback = callback;
        this.request = request;
    }

    @NonNull
    @Override
    public DataSource<Integer, Cast> create() {
        TMDBApi.TMDBService service = TMDBApi.create();
        return new CastDataSource(service, movie_id, tvShow_id, callback, request);
    }
}
