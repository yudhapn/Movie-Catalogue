package com.yudhapn.moviecatalogue.repository.cast;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import com.yudhapn.moviecatalogue.BuildConfig;
import com.yudhapn.moviecatalogue.api.TMDBApi;
import com.yudhapn.moviecatalogue.model.Cast;
import com.yudhapn.moviecatalogue.model.Credits;
import com.yudhapn.moviecatalogue.util.function.RetrofitCallback;
import com.yudhapn.moviecatalogue.view.InfoFragment;

import java.lang.ref.WeakReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;

public class CastDataSource extends PageKeyedDataSource<Integer, Cast> {
    private final TMDBApi.TMDBService service;
    private WeakReference<RetrofitCallback> retrofitCallback;
    private String movie_id;
    private String tvShow_id;
    private String request;

    CastDataSource(TMDBApi.TMDBService service, String movie_id, String tvShow_id, RetrofitCallback callback, String request) {
        this.service = service;
        this.movie_id = movie_id;
        this.tvShow_id = tvShow_id;
        this.retrofitCallback = new WeakReference<>(callback);
        this.request = request;
    }

    @Override
    public void loadInitial(@NonNull final LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, Cast> callback) {
        Call<Credits> call = getCall();

        Callback<Credits> requestCallback = new Callback<Credits>() {
            @Override
            public void onResponse(@NonNull Call<Credits> call, @NonNull Response<Credits> response) {
                Credits credits = response.body();
                if (credits == null) {
                    onFailure(call, new HttpException(response));
                    return;
                }
                callback.onResult(
                        credits.getCast(),
                        0,
                        credits.getCast().size(),
                        null,
                        null);
                retrofitCallback.get().onResult();
            }

            @Override
            public void onFailure(@NonNull Call<Credits> call, @NonNull Throwable t) {
            }
        };

        assert call != null;
        call.enqueue(requestCallback);
    }

    private Call<Credits> getCall() {
        final String API_KEY = BuildConfig.ApiKey;
        switch (request) {
            case InfoFragment.INFO_MOVIE:
                return service.getCreditsMovie(movie_id, API_KEY);
            case InfoFragment.INFO_TVSHOW:
                return service.getCreditsTvShow(tvShow_id, API_KEY);
        }
        return service.getCreditsMovie(movie_id, API_KEY);
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Cast> callback) {
    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Cast> callback) {
        Call<Credits> call = getCall();

        Callback<Credits> requestCallback = new Callback<Credits>() {
            @Override
            public void onResponse(@NonNull Call<Credits> call, @NonNull Response<Credits> response) {
                Credits credits = response.body();

                if (credits == null) {
                    onFailure(call, new HttpException(response));
                    return;
                }
                callback.onResult(
                        credits.getCast(),
                        null);
            }

            @Override
            public void onFailure(@NonNull Call<Credits> call, @NonNull Throwable t) {
            }
        };

        assert call != null;
        call.enqueue(requestCallback);
    }
}