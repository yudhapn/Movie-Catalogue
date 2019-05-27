package com.yudhapn.moviecatalogue.util;

import android.view.View;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.yudhapn.moviecatalogue.model.Movie;
import com.yudhapn.moviecatalogue.model.TvShow;
import com.yudhapn.moviecatalogue.view.SearchFragmentDirections;

public class CustomHandler {
    @BindingAdapter("backDropUrl")
    public static void loadBackDrop(ImageView view, String img) {
        String url = "https://image.tmdb.org/t/p/w780" + img;
        Glide.with(view.getContext()).load(url).into(view);
    }

    @BindingAdapter("posterUrl")
    public static void loadPoster(ImageView view, String img) {
        String url = "https://image.tmdb.org/t/p/w154" + img;
        Glide.with(view.getContext()).load(url).into(view);
    }

    public void showSelectedItem(View v, String id, String mediaType) {
        if (mediaType.equalsIgnoreCase("movie")) {
            SearchFragmentDirections.ActionShowDetailMovie action
                    = SearchFragmentDirections.actionShowDetailMovie(new Movie(Integer.parseInt(id)));
            Navigation.findNavController(v).navigate(action);
        } else {
            SearchFragmentDirections.ActionShowDetailTvshow action
                    = SearchFragmentDirections.actionShowDetailTvshow(new TvShow(Integer.parseInt(id)));
            Navigation.findNavController(v).navigate(action);
        }
    }
}