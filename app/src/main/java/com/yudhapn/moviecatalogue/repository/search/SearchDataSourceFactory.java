package com.yudhapn.moviecatalogue.repository.search;

import androidx.annotation.NonNull;
import androidx.paging.DataSource;

import com.yudhapn.moviecatalogue.api.TMDBApi;
import com.yudhapn.moviecatalogue.model.Cast;
import com.yudhapn.moviecatalogue.model.Search;
import com.yudhapn.moviecatalogue.util.function.RetrofitCallback;

public class SearchDataSourceFactory extends DataSource.Factory<Integer, Search> {
    private RetrofitCallback callback;
    private String query;

    public SearchDataSourceFactory(RetrofitCallback callback, String query) {
        this.callback = callback;
        this.query = query;
    }

    @NonNull
    @Override
    public DataSource<Integer, Search> create() {
        TMDBApi.TMDBService service = TMDBApi.create();
        return new SearchDataSource(service, callback, query);
    }
}
