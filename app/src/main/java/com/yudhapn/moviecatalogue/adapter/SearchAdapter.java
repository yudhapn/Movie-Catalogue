package com.yudhapn.moviecatalogue.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.yudhapn.moviecatalogue.R;
import com.yudhapn.moviecatalogue.databinding.ItemSearchBinding;
import com.yudhapn.moviecatalogue.model.Search;
import com.yudhapn.moviecatalogue.util.CustomHandler;

public class SearchAdapter extends PagedListAdapter<Search, SearchAdapter.ViewHolder> {
    public SearchAdapter() {
        super(Search.DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        ItemSearchBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.item_search, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ItemSearchBinding binding;

        ViewHolder(ItemSearchBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Search search) {
            String posterPath = "";
            String title = "";
            switch (search.getMediaType()) {
                case "movie":
                    posterPath = search.getPosterPath();
                    title = search.getTitle();
                    break;
                case "tv":
                    posterPath = search.getPosterPath();
                    title = search.getName() + " (TV Series)";
                    break;
                case "person":
                    posterPath = search.getProfilePath();
                    title = search.getName();
                    break;
            }
            binding.setHandler(new CustomHandler());
            binding.setPosterPath(posterPath);
            binding.setTitle(title);
            binding.setMediaType(search.getMediaType());
            binding.setId(search.getId().toString());
            binding.executePendingBindings();
        }
    }
}
