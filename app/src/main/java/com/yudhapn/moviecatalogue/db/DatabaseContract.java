package com.yudhapn.moviecatalogue.db;

import android.provider.BaseColumns;

class DatabaseContract {
    static String TABLE_FAVORITE = "favorite";

    static final class FavoriteColumns implements BaseColumns {
        static String ID_FAVORITE = "id_favorite";
        static String CATEGORY = "category";
        static String TITLE = "title";
        static String POSTER_PATH = "poster_path";
        static String RELEASE_DATE = "release_date";
        static String RUNTIME = "runtime";
        static String VOTE_AVERAGE = "vote_average";
    }
}
