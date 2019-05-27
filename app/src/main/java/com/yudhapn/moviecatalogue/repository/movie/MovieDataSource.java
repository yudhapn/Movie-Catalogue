package com.yudhapn.moviecatalogue.repository.movie;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import com.yudhapn.moviecatalogue.BuildConfig;
import com.yudhapn.moviecatalogue.api.TMDBApi;
import com.yudhapn.moviecatalogue.model.ListMovie;
import com.yudhapn.moviecatalogue.model.Movie;
import com.yudhapn.moviecatalogue.util.function.RetrofitCallback;
import com.yudhapn.moviecatalogue.view.MovieFragment;
import com.yudhapn.moviecatalogue.view.RelatedFragment;

import java.lang.ref.WeakReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;

public class MovieDataSource extends PageKeyedDataSource<Integer, Movie> {
    private final TMDBApi.TMDBService service;
    private WeakReference<RetrofitCallback> retrofitCallback;
    private String request;
    private String movieId;
    private boolean showDetail;

    MovieDataSource(TMDBApi.TMDBService service, String request, RetrofitCallback callback, boolean showDetail, String movieId) {
        this.service = service;
        this.request = request;
        this.retrofitCallback = new WeakReference<>(callback);
        this.showDetail = showDetail;
        this.movieId = movieId;
    }

    @Override
    public void loadInitial(@NonNull final LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, Movie> callback) {
        final int page = 1;
        Call<ListMovie> call = getCall(page);

        Callback<ListMovie> requestCallback = new Callback<ListMovie>() {
            @Override
            public void onResponse(@NonNull Call<ListMovie> call, @NonNull Response<ListMovie> response) {
                ListMovie listMovie = response.body();
                if (listMovie == null) {
                    onFailure(call, new HttpException(response));
                    return;
                }
                callback.onResult(
                        listMovie.getResults(),
                        0,
                        listMovie.getResults().size(),
                        null,
                        request.equalsIgnoreCase(RelatedFragment.SIMILAR) ? null : page + 1);
                retrofitCallback.get().onResult();
            }

            @Override
            public void onFailure(@NonNull Call<ListMovie> call, @NonNull Throwable t) {
            }
        };

        assert call != null;
        call.enqueue(requestCallback);
    }

    private Call<ListMovie> getCall(int page) {
        final String API_KEY = BuildConfig.ApiKey;
        switch (this.request) {
            case MovieFragment.POPULAR:
                return service.getPopularMovies(API_KEY, page);
            case MovieFragment.UPCOMING:
                return service.getUpcomingMovies(API_KEY, page);
            case RelatedFragment.SIMILAR:
                return service.getSimilarMovies(movieId, API_KEY);
        }
        return null;
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Movie> callback) {
    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Movie> callback) {
        final int page = params.key;
        Call<ListMovie> call = getCall(page);

        Callback<ListMovie> requestCallback = new Callback<ListMovie>() {
            @Override
            public void onResponse(@NonNull Call<ListMovie> call, @NonNull Response<ListMovie> response) {
                ListMovie listMovie = response.body();

                if (listMovie == null) {
                    onFailure(call, new HttpException(response));
                    return;
                }
                callback.onResult(
                        listMovie.getResults(),
                        showDetail ? page + 1 : null);
            }

            @Override
            public void onFailure(@NonNull Call<ListMovie> call, @NonNull Throwable t) {
            }
        };

        assert call != null;
        call.enqueue(requestCallback);
    }

}