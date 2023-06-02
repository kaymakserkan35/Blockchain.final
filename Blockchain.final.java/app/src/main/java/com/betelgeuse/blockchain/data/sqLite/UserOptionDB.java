package com.betelgeuse.blockchain.data.sqLite;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.betelgeuse.blockchain.H;
import com.betelgeuse.blockchain.core.indicator.Period;
import com.betelgeuse.blockchain.data.IUserOptionOperations;
import com.betelgeuse.blockchain.data.dataListener.UserOptionDTOListListener;
import com.betelgeuse.blockchain.data.dataListener.UserOptionDTOListener;
import com.betelgeuse.blockchain.data.dto.UserOptionDTO;

import java.util.ArrayList;
import java.util.List;

public class UserOptionDB extends SQLiteOpenHelper implements IUserOptionOperations {
    private String tableName;

    public UserOptionDB(@Nullable Context context) {
        super(context, UserOptionDTO.class.getSimpleName(), null, 1);
        this.tableName = UserOptionDTO.class.getSimpleName();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS" + " " + tableName + "(\n" +
                UserOptionDTO.Id_String + " varchar(15) PRIMARY KEY,            \n" +
                UserOptionDTO.Symbol_String + " varchar(15) NOT NULL,            \n" +
                UserOptionDTO.Email_String + "  varchar(15) NOT NULL,               \n" +
                UserOptionDTO.FromCurrency_String + " varchar(15) NOT NULL,              \n" +
                UserOptionDTO.ToCurrency_String + " varchar(15) NOT NULL,              \n" +
                UserOptionDTO.Period_INT + " INTEGER NOT NULL,                         \n" +
                UserOptionDTO.History_INT + " INTEGER NOT NULL,                         \n" +
                UserOptionDTO.Notification_INT + " INTEGER NOT NULL,                         \n" +
                UserOptionDTO.Vibration_INT + " INTEGER NOT NULL                         \n" +
                ")";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS ";
        db.execSQL(sql + " " + tableName);
        onCreate(db);
    }

    @Override
    public boolean createOrUpdateUserOption(UserOptionDTO userOptionDTO) {
        boolean result = createUserOption(userOptionDTO);
        if (!result) {
            result = updateUserOption(userOptionDTO);
        }
        return result;
    }

    @Override
    public boolean createUserOption(UserOptionDTO userOptionDTO) {
        /*
            if (isAny(userOptionDTO.email,userOptionDTO.fromCurrency,userOptionDTO.toCurrency)) {
                H.debugLog(this.getClass().getSimpleName(),"createUserOption","already exist!!");
                return false;
            }
        */
        SQLiteDatabase writableDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(UserOptionDTO.Id_String, userOptionDTO.email + userOptionDTO.symbol);
        contentValues.put(UserOptionDTO.Symbol_String, userOptionDTO.symbol);
        contentValues.put(UserOptionDTO.Email_String, userOptionDTO.email);
        contentValues.put(UserOptionDTO.ToCurrency_String, userOptionDTO.toCurrency);
        contentValues.put(UserOptionDTO.FromCurrency_String, userOptionDTO.fromCurrency);
        contentValues.put(UserOptionDTO.History_INT, userOptionDTO.history);
        contentValues.put(UserOptionDTO.Period_INT, userOptionDTO.period.getCode());
        contentValues.put(UserOptionDTO.Notification_INT, userOptionDTO.notification ? 1 : 0);
        contentValues.put(UserOptionDTO.Vibration_INT, userOptionDTO.vibration ? 1 : 0);
        long result =
                writableDatabase.insert(UserOptionDTO.class.getSimpleName(), null, contentValues);
        writableDatabase.close();
        return result != -1;
    }

    @Override
    @SuppressLint("Range")
    public void readUserOption(String email, String fromCurrency, String toCurrency, UserOptionDTOListener listener) {
        /*------------------------------  WORKS --------------------------------------------------*/
        SQLiteDatabase readableDatabase = getReadableDatabase();
        String[] arg = {email + "", fromCurrency + "", toCurrency};
        String query = "select * from  " + tableName + " WHERE " + UserOptionDTO.Email_String + "=?" + " AND " + UserOptionDTO.FromCurrency_String + "=?" + " AND " + UserOptionDTO.ToCurrency_String + "=?";
        Cursor cursor = readableDatabase.rawQuery(query, arg);
        UserOptionDTO userOptionDTO = null;
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex(UserOptionDTO.Id_String));
            String symbol = cursor.getString(cursor.getColumnIndex(UserOptionDTO.Symbol_String));
            String _email = cursor.getString(cursor.getColumnIndex(UserOptionDTO.Email_String));
            String frC = cursor.getString(cursor.getColumnIndex(UserOptionDTO.FromCurrency_String));
            String tC = cursor.getString(cursor.getColumnIndex(UserOptionDTO.ToCurrency_String));
            int his = cursor.getInt((cursor.getColumnIndex(UserOptionDTO.History_INT)));
            int per = cursor.getInt((cursor.getColumnIndex(UserOptionDTO.Period_INT)));
            int vibration = cursor.getInt((cursor.getColumnIndex(UserOptionDTO.Vibration_INT)));
            boolean _vib = (vibration == 1) ? true : false;

            int notification = cursor.getInt((cursor.getColumnIndex(UserOptionDTO.Notification_INT)));
            boolean _notify = (notification == 1) ? true : false;
            userOptionDTO = new UserOptionDTO(_email, frC, tC, Period.get(per), his, _vib, _notify);
            H.debugLog(this.getClass().getSimpleName(), "readUserOption", id);
            H.debugLog(this.getClass().getSimpleName(), "readUserOption", userOptionDTO.email);
        }

        listener.onSuccess(userOptionDTO);
    }

    @Override
    @SuppressLint("Range")
    public List<UserOptionDTO> readUserOptionsAll(UserOptionDTOListListener listener) {
        List<UserOptionDTO> options = new ArrayList<>();
        SQLiteDatabase readableDatabase = getReadableDatabase();
        String query = " select * from " + tableName;
        Cursor cursor = readableDatabase.rawQuery(query, null);
        while (cursor.moveToNext()) {
            String symbol = cursor.getString(cursor.getColumnIndex(UserOptionDTO.Symbol_String));
            String _email = cursor.getString(cursor.getColumnIndex(UserOptionDTO.Email_String));
            String frC = cursor.getString(cursor.getColumnIndex(UserOptionDTO.FromCurrency_String));
            String tC = cursor.getString(cursor.getColumnIndex(UserOptionDTO.ToCurrency_String));
            int his = cursor.getInt((cursor.getColumnIndex(UserOptionDTO.History_INT)));
            int per = cursor.getInt((cursor.getColumnIndex(UserOptionDTO.Period_INT)));
            int vibration = cursor.getInt((cursor.getColumnIndex(UserOptionDTO.Vibration_INT)));
            boolean _vib = (vibration == 1) ? true : false;

            int notification = cursor.getInt((cursor.getColumnIndex(UserOptionDTO.Notification_INT)));
            boolean _notify = (notification == 1) ? true : false;
            UserOptionDTO optionDTO = new UserOptionDTO(_email, frC, tC, Period.get(per), his, _vib, _notify);
            options.add(optionDTO);
        }
        listener.onSuccess(options);
        return options;
    }

    @Override
    @SuppressLint("Range")
    public List<UserOptionDTO> readUserOptionsAllByEmail(String email, UserOptionDTOListListener listener) {
        List<UserOptionDTO> options = new ArrayList<>();
        SQLiteDatabase readableDatabase = getReadableDatabase();
        String[] arg = {email + ""};
        String query = "select * from  " + tableName + " WHERE " + UserOptionDTO.Email_String + "=?";
        Cursor cursor = readableDatabase.rawQuery(query, arg);
        while (cursor.moveToNext()) {
            String _email = cursor.getString(cursor.getColumnIndex(UserOptionDTO.Email_String));
            String frC = cursor.getString(cursor.getColumnIndex(UserOptionDTO.FromCurrency_String));
            String tC = cursor.getString(cursor.getColumnIndex(UserOptionDTO.ToCurrency_String));
            int his = cursor.getInt((cursor.getColumnIndex(UserOptionDTO.History_INT)));
            int per = cursor.getInt((cursor.getColumnIndex(UserOptionDTO.Period_INT)));
            int vibration = cursor.getInt((cursor.getColumnIndex(UserOptionDTO.Vibration_INT)));
            boolean _vib = (vibration == 1) ? true : false;
            int notification = cursor.getInt((cursor.getColumnIndex(UserOptionDTO.Notification_INT)));
            boolean _notify = (notification == 1) ? true : false;
            UserOptionDTO optionDTO = new UserOptionDTO(_email, frC, tC, Period.get(per), his, _vib, _notify);
            options.add(optionDTO);
        }
        cursor.close();
        listener.onSuccess(options);
        return options;
    }

    @Override // not working
    public boolean isAny(String email, String fromCurrency, String toCurrency) {
        SQLiteDatabase readableDatabase = getReadableDatabase();
        String[] arg = {email + " ", fromCurrency + " ", toCurrency};
        String query = "select * from  " + tableName + " WHERE " + UserOptionDTO.Email_String + "=?" + "AND" + UserOptionDTO.FromCurrency_String + "=?" + "AND" + UserOptionDTO.ToCurrency_String + "=?";
        Cursor cursor = readableDatabase.rawQuery(query, arg);
        UserOptionDTO userOptionDTO = null;
        boolean result = false;
        while (cursor.moveToNext()) {
            @SuppressLint("Range") String id = cursor.getString(cursor.getColumnIndex(UserOptionDTO.Id_String));
            result = ((email + fromCurrency + "/" + toCurrency) == (id));
        }
        return result;
    }


    @Override
    public boolean updateUserOption(UserOptionDTO userOptionDTO) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(UserOptionDTO.Vibration_INT, userOptionDTO.vibration ? 1 : 0);
        cv.put(UserOptionDTO.Notification_INT, userOptionDTO.notification ? 1 : 0);
        cv.put(UserOptionDTO.History_INT, userOptionDTO.history);
        cv.put(UserOptionDTO.Period_INT, userOptionDTO.period.getCode());
        String query = UserOptionDTO.Email_String + "=?" + " AND " + UserOptionDTO.FromCurrency_String + "=?" + " AND " + UserOptionDTO.ToCurrency_String + "=?";
        String[] arg = {userOptionDTO.email + "", userOptionDTO.fromCurrency + "", userOptionDTO.toCurrency + ""};
        int result = db.update(tableName, cv, query, arg);
        return result != 0;
    }

    @Override
    public boolean deleteUserOption(String email, String fromCurrency, String toCurrency) {
        String escape = "\'";
        SQLiteDatabase writableDatabase = getWritableDatabase();
        /*----------------------  METHOD  1 works!! -------------------------------*/
        // DELETE FROM UserOptionDTO WHERE email == "kaymak__serkan35@hotmail.com" AND fromCurrency == "BTC" AND toCurrency == "USD"; ise yaradı..

        String sql2 = " DELETE FROM " + tableName + " WHERE " + UserOptionDTO.Email_String + " == " +
                " " + escape + email + escape +
                " AND " + UserOptionDTO.FromCurrency_String + " == " + escape + fromCurrency + escape +
                " AND " + UserOptionDTO.ToCurrency_String + " == " + escape + toCurrency + escape;

        writableDatabase.execSQL(sql2);
        /*--------------------------  METHOD  2  does not works!! ------------------------------*/

        String[] arg = {escape + email + escape + " ", fromCurrency + " ", toCurrency + " "};
        String query = UserOptionDTO.Email_String + " =? " + " AND " + UserOptionDTO.FromCurrency_String + "=?" + " AND " + UserOptionDTO.ToCurrency_String + "=?";

        int result = writableDatabase.delete(tableName, query, arg);
        if (result > 0) {
            H.debugLog(this.getClass().getSimpleName(), "deleteUserOption", "is successfully deleted");
            return true;
        } else {
            H.errorLog(this.getClass().getSimpleName(), "deleteUserOption", "something goes wrong?!");
            return false;
        }
    }

}
