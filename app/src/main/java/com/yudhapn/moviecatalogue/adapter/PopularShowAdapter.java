package com.yudhapn.moviecatalogue.adapter;

import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.yudhapn.moviecatalogue.R;
import com.yudhapn.moviecatalogue.databinding.ItemPopularTvshowBinding;
import com.yudhapn.moviecatalogue.model.TvShow;

public class PopularShowAdapter extends PagedListAdapter<TvShow, PopularShowAdapter.ViewHolder> {

    public PopularShowAdapter() {
        super(TvShow.DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        ItemPopularTvshowBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.item_popular_tvshow, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position));
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        private ItemPopularTvshowBinding binding;

        ViewHolder(ItemPopularTvshowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(TvShow tvShow) {
            binding.setTvshow(tvShow);
            binding.executePendingBindings();
        }
    }
}
