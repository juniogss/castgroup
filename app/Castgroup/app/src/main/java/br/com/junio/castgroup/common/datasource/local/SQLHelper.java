package br.com.junio.castgroup.common.datasource.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

public class SQLHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "castgroup.db";
    private static final int DB_VERSION = 1;
    private static SQLHelper INSTANCE;

    public static synchronized SQLHelper getInstance(Context context) {
        if (INSTANCE == null){
            INSTANCE = new SQLHelper(context.getApplicationContext());
        }
        return INSTANCE;
    }

    private SQLHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    @Override
    public void onCreate(@NotNull SQLiteDatabase db) {
        db.execSQL(CategorySQL.CREATE_TABLE_SQL);
        db.execSQL(CourseSQL.CREATE_TABLE_SQL);
    }

    @Override
    public void onUpgrade(@NotNull SQLiteDatabase db, int oldVersion, int newVersion) {
        dropTables(db);
    }

    private void dropTables(@NotNull SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + CourseSQL.TAB_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CategorySQL.TAB_NAME);

        onCreate(db);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

}
