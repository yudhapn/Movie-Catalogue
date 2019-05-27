package com.yudhapn.moviecatalogue.viewmodel;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.yudhapn.moviecatalogue.db.FavoriteHelper;
import com.yudhapn.moviecatalogue.model.Cast;
import com.yudhapn.moviecatalogue.model.DetailMovie;
import com.yudhapn.moviecatalogue.model.DetailTvShow;
import com.yudhapn.moviecatalogue.model.Favorite;
import com.yudhapn.moviecatalogue.model.Movie;
import com.yudhapn.moviecatalogue.model.Search;
import com.yudhapn.moviecatalogue.model.TvShow;
import com.yudhapn.moviecatalogue.repository.cast.CastDataSourceFactory;
import com.yudhapn.moviecatalogue.repository.movie.DetailMovieRepository;
import com.yudhapn.moviecatalogue.repository.movie.MovieDataSourceFactory;
import com.yudhapn.moviecatalogue.repository.search.SearchDataSourceFactory;
import com.yudhapn.moviecatalogue.repository.tvshow.DetailTvShowRepository;
import com.yudhapn.moviecatalogue.repository.tvshow.TvDataSourceFactory;
import com.yudhapn.moviecatalogue.util.LoadFavoritesAsync;
import com.yudhapn.moviecatalogue.util.MainThreadExecutor;
import com.yudhapn.moviecatalogue.util.function.LoadFavoritesCallback;
import com.yudhapn.moviecatalogue.util.function.RetrofitCallback;

public class MainViewModel extends ViewModel {
    private MediatorLiveData<PagedList<Movie>> upComingMovie = new MediatorLiveData<>();
    private MediatorLiveData<PagedList<Movie>> popularMovie = new MediatorLiveData<>();
    private MediatorLiveData<PagedList<Movie>> similarMovie = new MediatorLiveData<>();
    private MediatorLiveData<PagedList<TvShow>> popularTv = new MediatorLiveData<>();
    private MediatorLiveData<PagedList<TvShow>> topTv = new MediatorLiveData<>();
    private MediatorLiveData<PagedList<TvShow>> similarTvShow = new MediatorLiveData<>();
    private MediatorLiveData<PagedList<Cast>> cast = new MediatorLiveData<>();
    private MediatorLiveData<PagedList<Search>> search = new MediatorLiveData<>();
    private LiveData<DetailMovie> detailMovie = new MediatorLiveData<>();
    private LiveData<DetailTvShow> detailTvShow = new MediatorLiveData<>();
    private MutableLiveData<Boolean> isFavorite = new MutableLiveData<>();
    private MutableLiveData<String> querySearch = new MutableLiveData<>();

    public LiveData<PagedList<Movie>> getPopularMovie(String request, RetrofitCallback callback, boolean detail) {
        MovieDataSourceFactory factory = new MovieDataSourceFactory(request, callback, detail);
        LiveData<PagedList<Movie>> data = createMoviePagedList(factory);
        popularMovie.addSource(data, movies -> popularMovie.setValue(movies));
        return popularMovie;
    }

    public LiveData<PagedList<Movie>> getUpcomingMovie(String request, RetrofitCallback callback, boolean detail) {
        MovieDataSourceFactory factory = new MovieDataSourceFactory(request, callback, detail);
        LiveData<PagedList<Movie>> data = createMoviePagedList(factory);
        upComingMovie.addSource(data, movies -> upComingMovie.setValue(movies));
        return upComingMovie;
    }

    public LiveData<PagedList<Movie>> getSimilarMovie(String request, RetrofitCallback callback, boolean detail, String movieId) {
        MovieDataSourceFactory factory = new MovieDataSourceFactory(request, callback, detail, movieId);
        LiveData<PagedList<Movie>> data = createMoviePagedList(factory);
        similarMovie.addSource(data, movies -> similarMovie.setValue(movies));
        return similarMovie;
    }

    private LiveData<PagedList<Movie>> createMoviePagedList(MovieDataSourceFactory factory) {
        return new LivePagedListBuilder<>(factory, setConfig())
                .setFetchExecutor(new MainThreadExecutor())
                .build();
    }

    public LiveData<PagedList<Cast>> getCast(String movie_id, String tvShow_id, RetrofitCallback callback, String request) {
        CastDataSourceFactory factory = new CastDataSourceFactory(movie_id, tvShow_id, callback, request);
        LiveData<PagedList<Cast>> data = createCastPagedList(factory);
        cast.addSource(data, casts -> cast.setValue(casts));
        return cast;
    }

    private LiveData<PagedList<Cast>> createCastPagedList(CastDataSourceFactory factory) {
        return new LivePagedListBuilder<>(factory, setConfig())
                .setFetchExecutor(new MainThreadExecutor())
                .build();
    }

    public LiveData<PagedList<TvShow>> getPopularTv(String request, RetrofitCallback callback, boolean detail) {
        TvDataSourceFactory factory = new TvDataSourceFactory(request, callback, detail);
        LiveData<PagedList<TvShow>> data = createTvPagedList(factory);
        popularTv.addSource(data, tv -> popularTv.setValue(tv));
        return popularTv;
    }

    public LiveData<PagedList<TvShow>> getTopTv(String request, RetrofitCallback callback, boolean detail) {
        TvDataSourceFactory factory = new TvDataSourceFactory(request, callback, detail);
        LiveData<PagedList<TvShow>> data = createTvPagedList(factory);
        topTv.addSource(data, tv -> topTv.setValue(tv));
        return topTv;
    }

    public LiveData<PagedList<TvShow>> getSimilarTvShow(String request, RetrofitCallback callback, boolean detail, String tvShowId) {
        TvDataSourceFactory factory = new TvDataSourceFactory(request, callback, detail, tvShowId);
        LiveData<PagedList<TvShow>> data = createTvPagedList(factory);
        similarTvShow.addSource(data, tv -> similarTvShow.setValue(tv));
        return similarTvShow;
    }

    private LiveData<PagedList<TvShow>> createTvPagedList(TvDataSourceFactory factory) {
        return new LivePagedListBuilder<>(factory, setConfig())
                .setFetchExecutor(new MainThreadExecutor())
                .build();
    }

    public LiveData<PagedList<Search>> getSearchResult(String query, RetrofitCallback callback) {
        SearchDataSourceFactory factory = new SearchDataSourceFactory(callback, query);
        LiveData<PagedList<Search>> data = createSearchPagedList(factory);
        search.addSource(data, searches -> search.setValue(searches));
        return search;
    }

    private LiveData<PagedList<Search>> createSearchPagedList(SearchDataSourceFactory factory) {
        return new LivePagedListBuilder<>(factory, setConfig())
                .setFetchExecutor(new MainThreadExecutor())
                .build();
    }

    public LiveData<DetailMovie> getDetailMovie(FavoriteHelper favoriteHelper, String idMovie, RetrofitCallback callback) {
        DetailMovieRepository repo = DetailMovieRepository.getInstance();
        isFavorite.postValue(favoriteHelper.isFavorite(idMovie));
        detailMovie = repo.getDetailMovie(idMovie, callback);
        return detailMovie;
    }

    public LiveData<DetailMovie> getDetailMovie() {
        return detailMovie;
    }

    public LiveData<DetailTvShow> getDetailTvShow(FavoriteHelper favoriteHelper, String idTv, RetrofitCallback callback) {
        DetailTvShowRepository repo = DetailTvShowRepository.getInstance();
        isFavorite.postValue(favoriteHelper.isFavorite(idTv));
        detailTvShow = repo.getDetailTvShow(idTv, callback);
        return detailTvShow;
    }

    public LiveData<DetailTvShow> getDetailTvShow() {
        return detailTvShow;
    }

    private PagedList.Config setConfig() {
        return new PagedList.Config.Builder()
                .setPageSize(5)
                .setInitialLoadSizeHint(10)
                .setEnablePlaceholders(true)
                .build();
    }

    public void getAllFavorites(FavoriteHelper helper, LoadFavoritesCallback callback, String category) {
        new LoadFavoritesAsync(helper, callback, category).execute();
    }

    public void setFavorite(FavoriteHelper helper, Object object) {
        Favorite favorite;
        if (object instanceof DetailMovie) {
            DetailMovie detailMovie = (DetailMovie) object;
            favorite = new Favorite(
                    Integer.toString(detailMovie.getId()),
                    "movie",
                    detailMovie.getTitle(),
                    detailMovie.getPosterPath(),
                    detailMovie.getReleaseDate(),
                    Integer.toString(detailMovie.getRuntime()),
                    Double.toString(detailMovie.getVoteAverage()));
        } else {
            DetailTvShow detailTvShow = (DetailTvShow) object;
            favorite = new Favorite(
                    Integer.toString(detailTvShow.getId()),
                    "tvshow",
                    detailTvShow.getName(),
                    detailTvShow.getPosterPath(),
                    detailTvShow.getFirstAirDate(),
                    Integer.toString(detailTvShow.getEpisodeRunTime().get(0)),
                    Double.toString(detailTvShow.getVoteAverage()));
        }
        boolean isFav = isFavorite.getValue();
        if (isFav) {
            helper.deleteFavorite(favorite.getId());
        } else {
            helper.insertFavorite(favorite);
        }
        isFavorite.postValue(!isFav);
    }

    public LiveData<Boolean> isFavorite() {
        return isFavorite;
    }

    public void setQuerySearch(String query) {
        querySearch.postValue(query);
    }

    public MutableLiveData<String> getQuerySearch() {
        return querySearch;
    }
}