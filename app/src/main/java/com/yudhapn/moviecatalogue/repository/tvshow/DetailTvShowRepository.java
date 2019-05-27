package com.yudhapn.moviecatalogue.repository.tvshow;

import com.yudhapn.moviecatalogue.api.TMDBApi;
import com.yudhapn.moviecatalogue.model.DetailTvShow;
import com.yudhapn.moviecatalogue.util.function.RetrofitCallback;

import java.lang.ref.WeakReference;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailTvShowRepository {
    private TMDBApi.TMDBService service;

    private static class SingletonHelper {
        private static final DetailTvShowRepository INSTANCE = new DetailTvShowRepository();
    }
    public static DetailTvShowRepository getInstance() {
        return DetailTvShowRepository.SingletonHelper.INSTANCE;
    }

    private DetailTvShowRepository() {
        service = TMDBApi.create();
    }

    public LiveData<DetailTvShow> getDetailTvShow(String idTv, RetrofitCallback retroCallback) {
        final MutableLiveData<DetailTvShow> data = new MutableLiveData<>();
        WeakReference<RetrofitCallback> callbackWeakReference = new WeakReference<>(retroCallback);
        String API_KEY = "9d7cbfbe1af5f7b14988560744f1da06";
        service.getDetailTvShow(idTv, API_KEY).enqueue(new Callback<DetailTvShow>() {
            @Override
            public void onResponse(@NonNull Call<DetailTvShow> call, @NonNull Response<DetailTvShow> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                }
                callbackWeakReference.get().onResult();
            }

            @Override
            public void onFailure(@NonNull Call<DetailTvShow> call, @NonNull Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }
}
