package br.com.junio.castgroup.common.datasource.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import br.com.junio.castgroup.common.model.Course;
import br.com.junio.castgroup.main.presenter.filter.Sort;

public class CourseSQL {

    public static final String TAB_NAME = "course";

    public static final String COL_ID = "id";
    public static final String COL_UID = "uid";
    private static final String COL_SUBJECT_DESC = "desc";
    private static final String COL_START_DATE = "start_date";
    private static final String COL_END_DATE = "end_date";
    private static final String COL_STUDENTS = "students";
    private static final String COL_FK_CATEGORY = "fk_category";

    private static final String[] fields = {
            COL_ID, COL_UID, COL_SUBJECT_DESC, COL_START_DATE, COL_END_DATE, COL_STUDENTS, COL_FK_CATEGORY
    };

    public static String CREATE_TABLE_SQL =
            "CREATE TABLE " + TAB_NAME +
                    "(" +
                    COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COL_UID + " TEXT NOT NULL," +
                    COL_SUBJECT_DESC + " TEXT NOT NULL," +
                    COL_START_DATE + " TEXT NOT NULL," +
                    COL_END_DATE + " TEXT NOT NULL," +
                    COL_STUDENTS + " INTEGER," +
                    COL_FK_CATEGORY + " INTEGER NOT NULL," +

                    "FOREIGN KEY (" + COL_FK_CATEGORY + ")" +
                    " REFERENCES " + CategorySQL.TAB_NAME + " (" + CategorySQL.COL_CODE + ")" +
                    ")";

    private final SQLHelper sqlHelper;
    private SQLiteDatabase writable;
    private SQLiteDatabase readable;

    public CourseSQL(Context context) {
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
    private ContentValues getContentValues(@NotNull Course course) {
        ContentValues values = new ContentValues();
        values.put(COL_UID, course.get_id());
        values.put(COL_SUBJECT_DESC, course.getSubjectDesc());
        values.put(COL_START_DATE, course.getStartDate());
        values.put(COL_END_DATE, course.getEndDate());
        values.put(COL_STUDENTS, course.getStudents());
        values.put(COL_FK_CATEGORY, course.getFkCategory());

        return values;
    }

    public boolean insert(List<Course> courses) {

        getWritable().delete(TAB_NAME, null, null);

        getWritable().beginTransaction();

        boolean inserted;
        try {
            for (Course course : courses)
                insert(course, getWritable());

            getWritable().setTransactionSuccessful();
            inserted = true;
        } catch (Exception e) {
            inserted = false;
        } finally {
            getWritable().endTransaction();
        }

        return inserted;
    }

    private void insert(@NotNull Course course, @NotNull SQLiteDatabase db) {
        db.insert(TAB_NAME, null, getContentValues(course));
    }

    @NotNull
    private Course convertToModel(@NotNull Cursor cursor) {

        Course course = new Course();

        course.setId(cursor.getLong(cursor.getColumnIndex(COL_ID)));
        course.set_id(cursor.getString(cursor.getColumnIndex(COL_UID)));
        course.setSubjectDesc(cursor.getString(cursor.getColumnIndex(COL_SUBJECT_DESC)));
        course.setStartDate(cursor.getLong(cursor.getColumnIndex(COL_START_DATE)));
        course.setEndDate(cursor.getLong(cursor.getColumnIndex(COL_END_DATE)));
        course.setStudents(cursor.getInt(cursor.getColumnIndex(COL_STUDENTS)));
        course.setFkCategory(cursor.getLong(cursor.getColumnIndex(COL_FK_CATEGORY)));

        return course;
    }

    public List<Course> getList() {
        Cursor cursor = getReadable().query(TAB_NAME, fields, null, null, null, null, null, null);

        List<Course> list = new ArrayList<>();
        if (cursor != null) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                list.add(convertToModel(cursor));
            }
            cursor.close();
        }

        return list;
    }

    public List<Course> getListWith(String desc, String dateStart, @NotNull Sort sort) {

        String where = COL_SUBJECT_DESC + " LIKE '%" + desc + "%' AND " +
                COL_START_DATE + " LIKE '%" + dateStart + "%'";

        String order = null;
        switch (sort) {
            case LAST:
                order = COL_START_DATE + " DESC";
                break;
            case OLDER:
                order = COL_START_DATE + " ASC";
                break;
            case NAME_AZ:
                order = COL_SUBJECT_DESC + " ASC";
                break;
            case NAME_ZA:
                order = COL_SUBJECT_DESC + " DESC";
                break;
        }

        Cursor cursor = getReadable().query(TAB_NAME, fields, where, null, null, null, order, null);

        List<Course> list = new ArrayList<>();
        if (cursor != null) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                list.add(convertToModel(cursor));
            }
            cursor.close();
        }

        return list;
    }

    public void delete() {
        getWritable().delete(TAB_NAME, null, null);
    }

}
