package com.yudhapn.moviecatalogue.repository.tvshow;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import com.yudhapn.moviecatalogue.BuildConfig;
import com.yudhapn.moviecatalogue.api.TMDBApi;
import com.yudhapn.moviecatalogue.model.ListTvShow;
import com.yudhapn.moviecatalogue.model.TvShow;
import com.yudhapn.moviecatalogue.util.function.RetrofitCallback;
import com.yudhapn.moviecatalogue.view.RelatedFragment;
import com.yudhapn.moviecatalogue.view.TVShowFragment;

import java.lang.ref.WeakReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;

public class TvDataSource extends PageKeyedDataSource<Integer, TvShow> {
    private final TMDBApi.TMDBService service;
    private WeakReference<RetrofitCallback> retrofitCallback;
    private String request;
    private String tvShowId;
    private boolean showDetail;

    TvDataSource(TMDBApi.TMDBService service, String request, RetrofitCallback callback, boolean showDetail, String tvShowId) {
        this.service = service;
        this.request = request;
        this.retrofitCallback = new WeakReference<>(callback);
        this.showDetail = showDetail;
        this.tvShowId = tvShowId;
    }

    @Override
    public void loadInitial(@NonNull final LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, TvShow> callback) {
        final int page = 1;
        Call<ListTvShow> call = getCall(page);

        Callback<ListTvShow> requestCallback = new Callback<ListTvShow>() {
            @Override
            public void onResponse(@NonNull Call<ListTvShow> call, @NonNull Response<ListTvShow> response) {
                ListTvShow listTvShow = response.body();
                if (listTvShow == null) {
                    onFailure(call, new HttpException(response));
                    return;
                }
                callback.onResult(
                        listTvShow.getResults(),
                        0,
                        listTvShow.getResults().size(),
                        null,
                        request.equalsIgnoreCase(RelatedFragment.SIMILAR) ? null : page + 1);
                retrofitCallback.get().onResult();
            }

            @Override
            public void onFailure(@NonNull Call<ListTvShow> call, @NonNull Throwable t) {
            }
        };
        assert call != null;
        call.enqueue(requestCallback);
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, TvShow> callback) {
    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, TvShow> callback) {
        final int page = params.key;

        Call<ListTvShow> call = getCall(page);

        Callback<ListTvShow> requestCallback = new Callback<ListTvShow>() {
            @Override
            public void onResponse(@NonNull Call<ListTvShow> call, @NonNull Response<ListTvShow> response) {
                ListTvShow ListTvShow = response.body();

                if (ListTvShow == null) {
                    onFailure(call, new HttpException(response));
                    return;
                }
                callback.onResult(
                        ListTvShow.getResults(),
                        showDetail ? page + 1 : null);
            }

            @Override
            public void onFailure(@NonNull Call<ListTvShow> call, @NonNull Throwable t) {
            }
        };

        assert call != null;
        call.enqueue(requestCallback);
    }

    private Call<ListTvShow> getCall(int page) {
        final String API_KEY = BuildConfig.ApiKey;
        switch (this.request) {
            case TVShowFragment.POPULAR:
                return service.getTvPopular(API_KEY, page);
            case TVShowFragment.UPCOMING:
                return service.getTvTopRated(API_KEY, page);
            case RelatedFragment.SIMILAR:
                return service.getSimilarTvShow(tvShowId, API_KEY);
        }
        return null;
    }

}
