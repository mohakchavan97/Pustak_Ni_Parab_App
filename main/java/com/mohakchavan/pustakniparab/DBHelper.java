package com.mohakchavan.pustakniparab;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mohakchavan.pustakniparab.Models.Names;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Names.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "Names";
    public static final String SER_NO = "SERIAL_NO";
    public static final String FNAME = "FIRST_NAME";
    public static final String LNAME = "LAST_NAME";
    public static final String BLK = "BLK_OR_FLT_NO";
    public static final String STRT = "STREET_NAME";
    public static final String AREA = "LOCALITY_OR_AREA";
    public static final String CALL = "CONTACT";

    private final String CREATE_TABLE = new StringBuilder().append("CREATE TABLE ").append(TABLE_NAME).append(" (").append(SER_NO).append(" INTEGER PRIMARY KEY AUTOINCREMENT, ").append(FNAME).append(" TEXT(50), ").append(LNAME).append(" TEXT(50), ").append(BLK).append(" VARCHAR(10), ").append(STRT).append(" TEXT(50), ").append(AREA).append(" TEXT(50), ").append(CALL).append(" VARCHAR(10) ").append(" ); ").toString();


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

    }

    public boolean addName(String fname, String lname, String blk, String strt, String area, String call) {
//        final String sql = "INSERT INTO " + TABLE_NAME + " (\"" + FNAME + "\", " + "\"" + LNAME + "\", " + "\"" +BLK+"\", "+"\""+STRT+"\", "+"\""+AREA+"\", "+"\""+CALL+"\" "+") VALUES ('"+fname.toUpperCase().trim()+"', "+"'"+lname.toUpperCase().trim()+"', "+"'"+blk.toUpperCase().trim()+"', "+"'"+strt.toUpperCase().trim()+"', "+"'"+area.toUpperCase().trim()+"', "+"'"+call.toUpperCase().trim()+"');";
        final SQLiteDatabase db = getWritableDatabase();
        final ContentValues contentValues = new ContentValues();
        contentValues.put(FNAME, fname.toUpperCase());
        contentValues.put(LNAME, lname.toUpperCase());
        contentValues.put(BLK, blk.toUpperCase());
        contentValues.put(STRT, strt.toUpperCase());
        contentValues.put(AREA, area.toUpperCase());
        contentValues.put(CALL, call);
        int i = (int) db.insert(TABLE_NAME, null, contentValues);
//        db.execSQL(sql);
        db.close();
        return i > 0;
    }

    public boolean addName(String ser, String fname, String lname, String blk, String strt, String area, String call) {
        final SQLiteDatabase db = getWritableDatabase();
        final ContentValues contentValues = new ContentValues();
        contentValues.put(SER_NO, ser);
        contentValues.put(FNAME, fname);
        contentValues.put(LNAME, lname);
        contentValues.put(BLK, blk);
        contentValues.put(STRT, strt);
        contentValues.put(AREA, area);
        contentValues.put(CALL, call);
        int i = (int) db.insert(TABLE_NAME, null, contentValues);
        db.close();
        return i > 0;
    }

    public int getinsertedser() {
        final SQLiteDatabase db = getReadableDatabase();
        int insertedser = 1;
        final Cursor cursor = db.query(TABLE_NAME, new String[]{SER_NO}, null, null, null, null, SER_NO);
        if (cursor.getCount() > 0) {
            cursor.moveToLast();
            insertedser = cursor.getInt(cursor.getColumnIndex(SER_NO));
//            nextser++;
        }
        db.close();
        return insertedser;
    }

    public String getinsertedname() {
        final SQLiteDatabase db = getReadableDatabase();
        String name = "";
        final Cursor cursor = db.query(TABLE_NAME, new String[]{FNAME, LNAME}, null, null, null, null, SER_NO);
        if (cursor.getCount() > 0) {
            cursor.moveToLast();
            name += cursor.getString(cursor.getColumnIndex(FNAME));
            name += " ";
            if (cursor.getString(cursor.getColumnIndex(LNAME)).contentEquals("NULL"))
                name += "";
            else
                name += cursor.getString(cursor.getColumnIndex(LNAME));
        }
        db.close();
        return name;
    }

    public List<Names> getAllNames() {
        final SQLiteDatabase db = getReadableDatabase();
        Names names;
        List<Names> namesList = new ArrayList<>();
        final Cursor cursor = db.query(TABLE_NAME, new String[]{SER_NO, FNAME, LNAME, BLK, STRT, AREA, CALL}, null, null, null, null, SER_NO);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                names = new Names(cursor.getInt(cursor.getColumnIndex(SER_NO)), cursor.getString(cursor.getColumnIndex(FNAME)), cursor.getString(cursor.getColumnIndex(LNAME)).contentEquals("NULL") ? "" : cursor.getString(cursor.getColumnIndex(LNAME)), cursor.getString(cursor.getColumnIndex(BLK)).contentEquals("NULL") ? "" : cursor.getString(cursor.getColumnIndex(BLK)), cursor.getString(cursor.getColumnIndex(STRT)), cursor.getString(cursor.getColumnIndex(AREA)), cursor.getString(cursor.getColumnIndex(CALL)));
                namesList.add(names);
            } while (cursor.moveToNext());

        }
        db.close();
        return namesList;
    }

    public List<String> getAllSerials() {
        final SQLiteDatabase db = getReadableDatabase();
        List<String> list = new ArrayList<>();
        final Cursor cursor = db.query(TABLE_NAME, new String[]{SER_NO}, null, null, null, null, SER_NO);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                list.add(cursor.getString(cursor.getColumnIndex(SER_NO)));
            } while (cursor.moveToNext());
        }
        db.close();
        return list;
    }

    public List<Names> getSerialDetails(String ser) {
//        Names names = new Names();
        final SQLiteDatabase db = getReadableDatabase();

        /*
        final Cursor cursor = db.query(TABLE_NAME, new String[]{SER_NO, FNAME, LNAME, BLK, STRT, AREA, CALL}, SER_NO + "=?", new String[]{ser}, null, null, SER_NO);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                names.setSer_no(cursor.getInt(cursor.getColumnIndex(SER_NO)));
                names.setFname(cursor.getString(cursor.getColumnIndex(FNAME)));
                names.setLname(cursor.getString(cursor.getColumnIndex(LNAME)));
                names.setBlk(cursor.getString(cursor.getColumnIndex(BLK)));
                names.setStrt(cursor.getString(cursor.getColumnIndex(STRT)));
                names.setArea(cursor.getString(cursor.getColumnIndex(AREA)));
                names.setCall(cursor.getLong(cursor.getColumnIndex(CALL)));
            } while (cursor.moveToNext());
        }
        */

        List<Names> namesList = new ArrayList<>();
        final Cursor cursor = db.query(TABLE_NAME, new String[]{SER_NO, FNAME, LNAME, BLK, STRT, AREA, CALL}, SER_NO + " =? ", new String[]{ser}, null, null, SER_NO);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                namesList.add(new Names(cursor.getInt(cursor.getColumnIndex(SER_NO)), cursor.getString(cursor.getColumnIndex(FNAME)), cursor.getString(cursor.getColumnIndex(LNAME)).contentEquals("NULL") ? "" : cursor.getString(cursor.getColumnIndex(LNAME)), cursor.getString(cursor.getColumnIndex(BLK)).contentEquals("NULL") ? "" : cursor.getString(cursor.getColumnIndex(BLK)), cursor.getString(cursor.getColumnIndex(STRT)), cursor.getString(cursor.getColumnIndex(AREA)), cursor.getString(cursor.getColumnIndex(CALL))));
//                namesList.add(names);
            } while (cursor.moveToNext());

        }
        db.close();

        return namesList;

//        return names;
    }

    public List<Names> getNameDetails(String ser) {
//        Names names = new Names();
        List<Names> namesList = new ArrayList<>();
        final SQLiteDatabase db = getReadableDatabase();
        final Cursor cursor = db.query(TABLE_NAME, new String[]{SER_NO, FNAME, LNAME, BLK, STRT, AREA, CALL}, FNAME + " LIKE ? OR " + LNAME + " LIKE ? ", new String[]{"%" + ser.toUpperCase() + "%", "%" + ser.toUpperCase() + "%"}, null, null, SER_NO);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {

//                names.setSer_no(cursor.getInt(cursor.getColumnIndex(SER_NO)));
//                names.setFname(cursor.getString(cursor.getColumnIndex(FNAME)));
//                names.setLname(cursor.getString(cursor.getColumnIndex(LNAME)));
//                names.setBlk(cursor.getString(cursor.getColumnIndex(BLK)));
//                names.setStrt(cursor.getString(cursor.getColumnIndex(STRT)));
//                names.setArea(cursor.getString(cursor.getColumnIndex(AREA)));
//                names.setCall(cursor.getLong(cursor.getColumnIndex(CALL)));

                namesList.add(new Names(cursor.getInt(cursor.getColumnIndex(SER_NO)), cursor.getString(cursor.getColumnIndex(FNAME)), cursor.getString(cursor.getColumnIndex(LNAME)).contentEquals("NULL") ? "" : cursor.getString(cursor.getColumnIndex(LNAME)), cursor.getString(cursor.getColumnIndex(BLK)).contentEquals("NULL") ? "" : cursor.getString(cursor.getColumnIndex(BLK)), cursor.getString(cursor.getColumnIndex(STRT)), cursor.getString(cursor.getColumnIndex(AREA)), cursor.getString(cursor.getColumnIndex(CALL))));

            } while (cursor.moveToNext());
        }
        db.close();
//        return names;
        return namesList;
    }

    public boolean deleteName(String serial_name) {
        final SQLiteDatabase db = getWritableDatabase();
        int i = db.delete(TABLE_NAME, SER_NO + " = ? ", new String[]{serial_name});
        db.close();
        return i > 0;
    }
}

