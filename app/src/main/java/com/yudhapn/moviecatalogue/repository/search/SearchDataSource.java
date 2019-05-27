package com.yudhapn.moviecatalogue.repository.search;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import com.yudhapn.moviecatalogue.BuildConfig;
import com.yudhapn.moviecatalogue.api.TMDBApi;
import com.yudhapn.moviecatalogue.model.Cast;
import com.yudhapn.moviecatalogue.model.Credits;
import com.yudhapn.moviecatalogue.model.ListSearch;
import com.yudhapn.moviecatalogue.model.Search;
import com.yudhapn.moviecatalogue.util.function.RetrofitCallback;
import com.yudhapn.moviecatalogue.view.InfoFragment;

import java.lang.ref.WeakReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;

public class SearchDataSource extends PageKeyedDataSource<Integer, Search> {
    private final TMDBApi.TMDBService service;
    private WeakReference<RetrofitCallback> retrofitCallback;
    private String query;

    SearchDataSource(TMDBApi.TMDBService service, RetrofitCallback callback, String query) {
        this.service = service;
        this.retrofitCallback = new WeakReference<>(callback);
        this.query = query;
    }

    @Override
    public void loadInitial(@NonNull final LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, Search> callback) {
        Call<ListSearch> call = getCall();

        Callback<ListSearch> requestCallback = new Callback<ListSearch>() {
            @Override
            public void onResponse(@NonNull Call<ListSearch> call, @NonNull Response<ListSearch> response) {
                ListSearch listSearch = response.body();
                if (listSearch == null) {
                    onFailure(call, new HttpException(response));
                    return;
                }
                callback.onResult(
                        listSearch.getResults(),
                        0,
                        listSearch.getResults().size(),
                        null,
                        null);
                retrofitCallback.get().onResult();
            }

            @Override
            public void onFailure(@NonNull Call<ListSearch> call, @NonNull Throwable t) {
            }
        };

        assert call != null;
        call.enqueue(requestCallback);
    }

    private Call<ListSearch> getCall() {
        final String API_KEY = BuildConfig.ApiKey;
        return service.getSearchResult(query, API_KEY);
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Search> callback) {
    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Search> callback) {
        Call<ListSearch> call = getCall();

        Callback<ListSearch> requestCallback = new Callback<ListSearch>() {
            @Override
            public void onResponse(@NonNull Call<ListSearch> call, @NonNull Response<ListSearch> response) {
                ListSearch listSearch = response.body();

                if (listSearch == null) {
                    onFailure(call, new HttpException(response));
                    return;
                }
                callback.onResult(
                        listSearch.getResults(),
                        null);
            }

            @Override
            public void onFailure(@NonNull Call<ListSearch> call, @NonNull Throwable t) {
            }
        };

        assert call != null;
        call.enqueue(requestCallback);
    }
}