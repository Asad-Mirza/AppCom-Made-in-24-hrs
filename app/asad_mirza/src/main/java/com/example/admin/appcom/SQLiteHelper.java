package com.example.admin.appcom;

/**
 * Created by Asad Mirza on 22-02-2018.
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.util.SortedList;
import android.widget.Toast;

public class SQLiteHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MYCONTACTSDB";
    public static final String CONTACTS_TABLE_NAME = "contacts";
    public static final String CONTACTS_COLUMN_ID = "id";
    public static final String CONTACTS_COLUMN_NAME = "name";
    public static final String CONTACTS_COLUMN_EMAIL = "email";

    public static final String CONTACTS_COLUMN_PHONE = "phone";
    private HashMap hp;
   Context context;
    public SQLiteHelper(Context context,String email) {




        super(context, DATABASE_NAME+email , null, 1);
      this.context  =context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table contacts " +
                        "(id integer primary key, name text,email text,phone text,image BLOB)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS contacts");
        onCreate(db);
    }

    public boolean insertContact (String name, String phone, String email,byte[] image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("phone", phone);
        contentValues.put("email", email);
        contentValues.put("image", image);

        long l = db.insert("contacts", null, contentValues);
       if (l==-1) return false;
        return true;
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from contacts where id="+id+"", null );
        return res;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, CONTACTS_TABLE_NAME);
        return numRows;
    }

    public boolean updateContact (Integer id, String name, String phone, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("phone", phone);
        contentValues.put("email", email);

        db.update("contacts", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public Integer deleteContact (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("contacts",
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

    public SortedList<contactPOJO> getAllCotacts() {
       SortedList<contactPOJO> data = new SortedList<contactPOJO>(contactPOJO.class, new SortedList.Callback<contactPOJO>() {
            @Override
            public int compare(contactPOJO o1, contactPOJO o2) {


                return o1.getName().compareTo(o2.getName());
            }

            @Override
            public void onInserted(int position, int count) {

            }

            @Override
            public void onRemoved(int position, int count) {

            }

            @Override
            public void onMoved(int fromPosition, int toPosition) {

            }

            @Override
            public void onChanged(int position, int count) {

            }

            @Override
            public boolean areContentsTheSame(contactPOJO oldItem, contactPOJO newItem) {
                // return whether the items' visual representations are the same or not.
                return oldItem.getEmail().equals(newItem.getEmail());
            }

            @Override
            public boolean areItemsTheSame(contactPOJO item1, contactPOJO item2) {
                return item1.getEmail().equals(item2.getEmail());
            }
        });



        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from contacts", null );



        if (res.moveToFirst()) {
            do {
                contactPOJO pojo= new contactPOJO();


                pojo.setName(res.getString(res.getColumnIndex("name")));
                pojo.setEmail(res.getString(res.getColumnIndex("email")));
                pojo.setPhone(res.getString(res.getColumnIndex("phone")));
                pojo.setImage(res.getBlob(res.getColumnIndex("image")));
                data.add(pojo);
             //   Toast.makeText(context, pojo.getName(), Toast.LENGTH_SHORT).show();

            } while (res.moveToNext());
        }



        return data;
    }
}

