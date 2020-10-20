package br.com.junio.castgroup.common.datasource.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import br.com.junio.castgroup.common.model.Category;

public class CategorySQL {

    public static final String TAB_NAME = "category";

    public static final String COL_CODE = "code";
    private static final String COL_DESC = "desc";

    private static final String[] fields = {
            COL_CODE, COL_DESC
    };

    public static String CREATE_TABLE_SQL =
            "CREATE TABLE " + TAB_NAME +
                    "(" +
                    COL_CODE + " INTEGER PRIMARY KEY ," +
                    COL_DESC + " TEXT NOT NULL" +
                    ")";

    private final SQLHelper sqlHelper;
    private SQLiteDatabase writable;
    private SQLiteDatabase readable;

    public CategorySQL(Context context) {
        this.sqlHelper = SQLHelper.getInstance(context);
    }

    public synchronized SQLiteDatabase getWritable() {
        if (writable == null)
            writable = sqlHelper.getWritableDatabase();
        return writable;
    }

    public synchronized SQLiteDatabase getReadable() {
        if (readable == null)
            readable = sqlHelper.getReadableDatabase();
        return readable;
    }

    @NotNull
    private ContentValues getContentValues(@NotNull Category category) {
        ContentValues values = new ContentValues();
        values.put(COL_CODE, category.getCode());
        values.put(COL_DESC, category.getDesc());

        return values;
    }

    public boolean insert(List<Category> categories) {
        getWritable().delete(TAB_NAME, null, null);
        getWritable().beginTransaction();

        boolean inserted;
        try {
            for (Category category : categories)
                insert(category, getWritable());

            getWritable().setTransactionSuccessful();
            inserted = true;
        } catch (Exception e) {
            inserted = false;
        } finally {
            getWritable().endTransaction();
        }

        return inserted;
    }

    private void insert(@NotNull Category category, @NotNull SQLiteDatabase db) {
        db.insert(TAB_NAME, null, getContentValues(category));
    }

    @NotNull
    private Category convertToModel(@NotNull Cursor cursor) {

        Category category = new Category();

        category.setCode(cursor.getLong(cursor.getColumnIndex(COL_CODE)));
        category.setDesc(cursor.getString(cursor.getColumnIndex(COL_DESC)));

        return category;
    }

    public List<Category> getList() {
        Cursor cursor = getReadable().query(TAB_NAME, fields, null, null, null, null, null, null);

        List<Category> list = new ArrayList<>();
        if (cursor != null) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                list.add(convertToModel(cursor));
            }
            cursor.close();
        }

        return list;
    }

    public Category getBy(long code) {

        String where = COL_CODE + " = " + code;
        Cursor cursor = getReadable().query(TAB_NAME, fields, where, null, null, null, null, null);

        Category payType = null;
        if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
            payType = convertToModel(cursor);
            cursor.close();
        }

        return payType;
    }
}
