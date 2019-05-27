package com.yudhapn.moviecatalogue.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.yudhapn.moviecatalogue.model.Favorite;

import java.util.ArrayList;
import java.util.List;

public class FavoriteHelper {
    private static final String DATABASE_TABLE = DatabaseContract.TABLE_FAVORITE;
    private static final String _ID = DatabaseContract.FavoriteColumns._ID;
    private static final String ID_FAVORITE = DatabaseContract.FavoriteColumns.ID_FAVORITE;
    private static final String CATEGORY = DatabaseContract.FavoriteColumns.CATEGORY;
    private static final String TITLE = DatabaseContract.FavoriteColumns.TITLE;
    private static final String POSTER_PATH = DatabaseContract.FavoriteColumns.POSTER_PATH;
    private static final String RELEASE_DATE = DatabaseContract.FavoriteColumns.RELEASE_DATE;
    private static final String RUNTIME = DatabaseContract.FavoriteColumns.RUNTIME;
    private static final String VOTE_AVERAGE = DatabaseContract.FavoriteColumns.VOTE_AVERAGE;
    private static DatabaseHelper databaseHelper;
    private static FavoriteHelper INSTANCE;

    private static SQLiteDatabase database;

    private FavoriteHelper(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public static FavoriteHelper getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SQLiteOpenHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new FavoriteHelper(context);
                }
            }
        }
        return INSTANCE;
    }

    public void open() throws SQLException {
        database = databaseHelper.getWritableDatabase();
    }

    public void close() {
        databaseHelper.close();
        if (database.isOpen()) {
            database.close();
        }
    }

    public List<Favorite> getAllFavorites() {
        List<Favorite> listFav = new ArrayList<>();
        Cursor cursor = database.query(
                DATABASE_TABLE, null,
                null,
                null,
                null,
                null,
                _ID + " ASC",
                null);
        cursor.moveToFirst();
        Favorite favorite;
        if (cursor.getCount() > 0) {
            do {
                favorite = new Favorite();
                favorite.setId(cursor.getString(cursor.getColumnIndexOrThrow(ID_FAVORITE)));
                favorite.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(CATEGORY)));
                favorite.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(TITLE)));
                favorite.setPosterPath(cursor.getString(cursor.getColumnIndexOrThrow(POSTER_PATH)));
                favorite.setReleaseDate(cursor.getString(cursor.getColumnIndexOrThrow(RELEASE_DATE)));
                favorite.setRuntime(cursor.getString(cursor.getColumnIndexOrThrow(RUNTIME)));
                favorite.setVoteAverage(cursor.getString(cursor.getColumnIndexOrThrow(VOTE_AVERAGE)));

                listFav.add(favorite);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return listFav;
    }

    public boolean isFavorite(String id) {
        if (!database.isOpen()) {
            open();
        }
        Cursor cursor = database.rawQuery("SELECT * FROM " + DATABASE_TABLE + " WHERE " + ID_FAVORITE + " = ?", new String[]{id});
        boolean result = cursor.getCount() > 0;
        cursor.close();
        return result;
    }

    public long insertFavorite(Favorite favorite) {
        if (!database.isOpen()) {
            open();
        }
        ContentValues args = new ContentValues();
        args.put(ID_FAVORITE, favorite.getId());
        args.put(CATEGORY, favorite.getCategory());
        args.put(TITLE, favorite.getTitle());
        args.put(POSTER_PATH, favorite.getPosterPath());
        args.put(RELEASE_DATE, favorite.getReleaseDate());
        args.put(RUNTIME, favorite.getRuntime());
        args.put(VOTE_AVERAGE, favorite.getVoteAverage());
        return database.insert(DATABASE_TABLE, null, args);
    }

    public int deleteFavorite(String id) {
        if (!database.isOpen()) {
            open();
        }
        return database.delete(DATABASE_TABLE, ID_FAVORITE + "= '" + id + "'", null);
    }


}
