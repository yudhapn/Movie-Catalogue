package com.yudhapn.moviecatalogue.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.yudhapn.moviecatalogue.R;
import com.yudhapn.moviecatalogue.databinding.ItemCastBinding;
import com.yudhapn.moviecatalogue.model.Cast;

public class CastAdapter extends PagedListAdapter<Cast, CastAdapter.ViewHolder> {
    public CastAdapter() {
        super(Cast.DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        ItemCastBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.item_cast, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ItemCastBinding binding;

        ViewHolder(ItemCastBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Cast cast) {
            binding.setCast(cast);
            binding.executePendingBindings();
        }
    }
}
