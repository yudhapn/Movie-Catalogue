package com.yudhapn.moviecatalogue.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Cast implements Parcelable {

    @SerializedName("cast_id")
    @Expose
    private Integer castId;
    @SerializedName("character")
    @Expose
    private String character;
    @SerializedName("credit_id")
    @Expose
    private String creditId;
    @SerializedName("gender")
    @Expose
    private Integer gender;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("order")
    @Expose
    private Integer order;
    @SerializedName("profile_path")
    @Expose
    private String profilePath;

    public Integer getCastId() {
        return castId;
    }

    public void setCastId(Integer castId) {
        this.castId = castId;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public String getCreditId() {
        return creditId;
    }

    public void setCreditId(String creditId) {
        this.creditId = creditId;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getProfilePath() {
        return profilePath;
    }

    public void setProfilePath(String profilePath) {
        this.profilePath = profilePath;
    }

    public static final DiffUtil.ItemCallback<Cast> DIFF_CALLBACK = new DiffUtil.ItemCallback<Cast>() {

        // Check if items represent the same thing.
        @Override
        public boolean areItemsTheSame(Cast oldItem, Cast newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        // Checks if the item contents have changed.
        @Override
        public boolean areContentsTheSame(@NonNull Cast oldItem, @NonNull Cast newItem) {
            return true; // Assume Repository details don't change
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.castId);
        dest.writeString(this.character);
        dest.writeString(this.creditId);
        dest.writeValue(this.gender);
        dest.writeValue(this.id);
        dest.writeString(this.name);
        dest.writeValue(this.order);
        dest.writeString(this.profilePath);
    }

    public Cast() {
    }

    protected Cast(Parcel in) {
        this.castId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.character = in.readString();
        this.creditId = in.readString();
        this.gender = (Integer) in.readValue(Integer.class.getClassLoader());
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.name = in.readString();
        this.order = (Integer) in.readValue(Integer.class.getClassLoader());
        this.profilePath = in.readString();
    }

    public static final Parcelable.Creator<Cast> CREATOR = new Parcelable.Creator<Cast>() {
        @Override
        public Cast createFromParcel(Parcel source) {
            return new Cast(source);
        }

        @Override
        public Cast[] newArray(int size) {
            return new Cast[size];
        }
    };
}