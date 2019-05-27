package com.yudhapn.moviecatalogue.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.yudhapn.moviecatalogue.R;
import com.yudhapn.moviecatalogue.databinding.ItemRelatedBinding;
import com.yudhapn.moviecatalogue.model.Movie;

public class SimilarMovieAdapter extends PagedListAdapter<Movie, SimilarMovieAdapter.ViewHolder> {
    public SimilarMovieAdapter() {
        super(Movie.DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        ItemRelatedBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.item_related, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ItemRelatedBinding binding;

        ViewHolder(ItemRelatedBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Movie movie) {
            binding.setMovie(movie);
            binding.executePendingBindings();
        }
    }
}