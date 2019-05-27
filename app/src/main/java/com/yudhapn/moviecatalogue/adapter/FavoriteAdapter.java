package com.yudhapn.moviecatalogue.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.yudhapn.moviecatalogue.R;
import com.yudhapn.moviecatalogue.databinding.ItemFavoriteBinding;
import com.yudhapn.moviecatalogue.model.Favorite;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {
    private List<Favorite> listFavorites = new ArrayList<>();

    public void setListNotes(List<Favorite> listFavorites) {
        if (listFavorites.size() > 0) {
            this.listFavorites.clear();
        }
        this.listFavorites.addAll(listFavorites);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        ItemFavoriteBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.item_favorite, parent, false);
        return new ViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(listFavorites.get(position));
    }

    @Override
    public int getItemCount() {
        return listFavorites.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ItemFavoriteBinding binding;

        ViewHolder(ItemFavoriteBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Favorite favorite) {
            binding.setFavorite(favorite);
            binding.executePendingBindings();
        }
    }
}
