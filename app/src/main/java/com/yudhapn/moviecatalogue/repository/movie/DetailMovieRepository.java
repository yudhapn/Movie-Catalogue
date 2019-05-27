package com.yudhapn.moviecatalogue.repository.movie;

import android.util.Log;

import com.yudhapn.moviecatalogue.api.TMDBApi;
import com.yudhapn.moviecatalogue.model.DetailMovie;
import com.yudhapn.moviecatalogue.util.function.RetrofitCallback;

import java.lang.ref.WeakReference;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailMovieRepository {
    private TMDBApi.TMDBService service;

    private static class SingletonHelper {
        private static final DetailMovieRepository INSTANCE = new DetailMovieRepository();
    }
    public static DetailMovieRepository getInstance() {
        return SingletonHelper.INSTANCE;
    }

    private DetailMovieRepository() {
        service = TMDBApi.create();
    }

    public LiveData<DetailMovie> getDetailMovie(String idMovie, RetrofitCallback retroCallback) {
        final MutableLiveData<DetailMovie> data = new MutableLiveData<>();
        WeakReference<RetrofitCallback> callbackWeakReference = new WeakReference<>(retroCallback);
        String API_KEY = "9d7cbfbe1af5f7b14988560744f1da06";
        service.getDetailMovie(idMovie, API_KEY).enqueue(new Callback<DetailMovie>() {
                    @Override
                    public void onResponse(@NonNull Call<DetailMovie> call, @NonNull Response<DetailMovie> response) {
                        if (response.isSuccessful()) {
                            data.setValue(response.body());
                        }
                        callbackWeakReference.get().onResult();
                    }

                    @Override
                    public void onFailure(@NonNull Call<DetailMovie> call, @NonNull Throwable t) {
                        data.setValue(null);
                    }
                });
        return data;
    }
}
