package com.yudhapn.moviecatalogue.adapter;

import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.yudhapn.moviecatalogue.R;
import com.yudhapn.moviecatalogue.databinding.ItemPopularMovieBinding;
import com.yudhapn.moviecatalogue.model.Movie;

public class PopularMovieAdapter extends PagedListAdapter<Movie, PopularMovieAdapter.ViewHolder> {
    public PopularMovieAdapter() {
        super(Movie.DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        ItemPopularMovieBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.item_popular_movie, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.bind(getItem(position));
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ItemPopularMovieBinding binding;

        ViewHolder(ItemPopularMovieBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Movie movie) {
            binding.setMovie(movie);
            binding.executePendingBindings();
        }
    }
}
