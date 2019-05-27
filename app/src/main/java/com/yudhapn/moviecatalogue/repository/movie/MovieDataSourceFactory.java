package com.yudhapn.moviecatalogue.repository.movie;

import com.yudhapn.moviecatalogue.api.TMDBApi;
import com.yudhapn.moviecatalogue.model.Movie;
import com.yudhapn.moviecatalogue.util.function.RetrofitCallback;

import androidx.annotation.NonNull;
import androidx.paging.DataSource;

public class MovieDataSourceFactory extends DataSource.Factory<Integer, Movie> {
    private String request;
    private RetrofitCallback callback;
    private boolean showDetail;
    private String movieId;

    public MovieDataSourceFactory(String request, RetrofitCallback callback, boolean showDetail) {
        this.request = request;
        this.callback = callback;
        this.showDetail = showDetail;
        this.movieId = "";
    }

    public MovieDataSourceFactory(String request, RetrofitCallback callback, boolean showDetail, String movieId) {
        this.request = request;
        this.callback = callback;
        this.showDetail = showDetail;
        this.movieId = movieId;
    }

    @NonNull
    @Override
    public DataSource<Integer, Movie> create() {
        TMDBApi.TMDBService service = TMDBApi.create();
        return new MovieDataSource(service, request, callback, showDetail, movieId);
    }
}
