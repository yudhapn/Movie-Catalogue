package com.yudhapn.moviecatalogue.api;

import com.yudhapn.moviecatalogue.model.Credits;
import com.yudhapn.moviecatalogue.model.DetailMovie;
import com.yudhapn.moviecatalogue.model.DetailTvShow;
import com.yudhapn.moviecatalogue.model.ListMovie;
import com.yudhapn.moviecatalogue.model.ListSearch;
import com.yudhapn.moviecatalogue.model.ListTvShow;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class TMDBApi {
    private static final String ROOT_URL = "https://api.themoviedb.org/3/";

    public static TMDBService create() {
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(1200, TimeUnit.SECONDS)
                .connectTimeout(1200, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ROOT_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(TMDBService.class);
    }

    public interface TMDBService {
        @GET("movie/upcoming")
        Call<ListMovie> getUpcomingMovies(
                @Query("api_key") String apiKey,
                @Query("page") int page
        );

        @GET("movie/popular")
        Call<ListMovie> getPopularMovies(
                @Query("api_key") String apiKey,
                @Query("page") int page
        );

        @GET("movie/{movie_id}/similar")
        Call<ListMovie> getSimilarMovies(
                @Path("movie_id") String movie_id,
                @Query("api_key") String apiKey
        );

        @GET("tv/top_rated")
        Call<ListTvShow> getTvTopRated(
                @Query("api_key") String apiKey,
                @Query("page") int page
        );

        @GET("tv/popular")
        Call<ListTvShow> getTvPopular(
                @Query("api_key") String apiKey,
                @Query("page") int page
        );

        @GET("tv/{tv_id}/similar")
        Call<ListTvShow> getSimilarTvShow(
                @Path("tv_id") String tv_id,
                @Query("api_key") String apiKey
        );

        @GET("movie/{movie_id}")
        Call<DetailMovie> getDetailMovie(
                @Path("movie_id") String movie_id,
                @Query("api_key") String apiKey
        );

        @GET("tv/{tv_id}")
        Call<DetailTvShow> getDetailTvShow(
                @Path("tv_id") String tv_id,
                @Query("api_key") String apiKey
        );

        @GET("movie/{movie_id}/credits")
        Call<Credits> getCreditsMovie(
                @Path("movie_id") String movie_id,
                @Query("api_key") String apiKey
        );

        @GET("tv/{tv_id}/credits")
        Call<Credits> getCreditsTvShow(
                @Path("tv_id") String tv_id,
                @Query("api_key") String apiKey
        );

        @GET("search/multi")
        Call<ListSearch> getSearchResult(
                @Query("query") String query,
                @Query("api_key") String apiKey
        );
    }
}
