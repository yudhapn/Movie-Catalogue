package com.yudhapn.moviecatalogue.repository.tvshow;

import com.yudhapn.moviecatalogue.api.TMDBApi;
import com.yudhapn.moviecatalogue.model.TvShow;
import com.yudhapn.moviecatalogue.util.function.RetrofitCallback;

import androidx.annotation.NonNull;
import androidx.paging.DataSource;

public class TvDataSourceFactory extends DataSource.Factory<Integer, TvShow> {
    private String request;
    private RetrofitCallback callback;
    private boolean showDetail;
    private String tvShowId;

    public TvDataSourceFactory(String request, RetrofitCallback callback, boolean showDetail) {
        this.request = request;
        this.callback = callback;
        this.showDetail = showDetail;
        this.tvShowId = "";
    }

    public TvDataSourceFactory(String request, RetrofitCallback callback, boolean showDetail, String tvShowId) {
        this.request = request;
        this.callback = callback;
        this.showDetail = showDetail;
        this.tvShowId = tvShowId;
    }

    @NonNull
    @Override
    public DataSource<Integer, TvShow> create() {
        TMDBApi.TMDBService service = TMDBApi.create();
        return new TvDataSource(service, request, callback, showDetail, tvShowId);
    }
}
