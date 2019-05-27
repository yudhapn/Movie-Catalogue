package com.yudhapn.moviecatalogue.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.yudhapn.moviecatalogue.R;
import com.yudhapn.moviecatalogue.databinding.ItemRelatedBinding;
import com.yudhapn.moviecatalogue.model.TvShow;

public class SimilarTvShowAdapter extends PagedListAdapter<TvShow, SimilarTvShowAdapter.ViewHolder> {
    public SimilarTvShowAdapter() {
        super(TvShow.DIFF_CALLBACK);
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

        void bind(TvShow tvShow) {
            binding.setTvShow(tvShow);
            binding.executePendingBindings();
        }
    }
}