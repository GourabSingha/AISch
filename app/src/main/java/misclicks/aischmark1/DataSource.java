package misclicks.aischmark1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import static misclicks.aischmark1.FeedReaderDbHelper.TABLE_NAME;
import static misclicks.aischmark1.FeedReaderDbHelper.COLUMN_DATE;
import static misclicks.aischmark1.FeedReaderDbHelper.COLUMN_TIME;
import static misclicks.aischmark1.FeedReaderDbHelper.COLUMN_ID;
import static misclicks.aischmark1.FeedReaderDbHelper.COLUMN_NAME;
import static misclicks.aischmark1.FeedReaderDbHelper.COLUMN_LOCATION;

/**
 * Created by Gourab Singha on 25-03-2017.
 */

public class DataSource
{
    private SQLiteDatabase database;
    private FeedReaderDbHelper dbHelper;

    public DataSource(Context context)
    {
        dbHelper = new FeedReaderDbHelper(context);
    }

    public void open() throws SQLException
    {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {

        dbHelper.close();
    }
    public boolean insert_data(String name, String location,String date,String time)
    {
        ContentValues values = new ContentValues();
        values.put(FeedReaderDbHelper.COLUMN_NAME, name);
        values.put(FeedReaderDbHelper.COLUMN_LOCATION, location);
        values.put(FeedReaderDbHelper.COLUMN_DATE, date);
        values.put(FeedReaderDbHelper.COLUMN_TIME, time);
        long insertId = database.insert(TABLE_NAME, null,
                values);
        if (insertId == -1)
            return false;
        else
            return true;
    }
   public Cursor getAllData()
    {
        Cursor res = database.rawQuery("select * from "+TABLE_NAME,null);
        return res;
    }

    public Cursor get_event_name(String date)
    {
        Cursor re = database.rawQuery("select * from "+TABLE_NAME+" where "+
                COLUMN_DATE+"= ?", new String[]{date});
        return re;
    }

    public Cursor event_delete()
    {
        Cursor re1 = database.rawQuery("select * from "+TABLE_NAME,null);
        return re1;
    }

    public Cursor event_notify(String date)
    {
        Cursor rel = database.rawQuery("select * from "+TABLE_NAME+" where "+COLUMN_DATE+"= ?",
                new String[]{date});
        return rel;
    }

    public boolean updateData( String name, String location, String date ,String  time)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, name);
        contentValues.put(COLUMN_LOCATION, location);
        contentValues.put(COLUMN_DATE, date);
        contentValues.put(COLUMN_TIME, time);
        long updateID = database.update(TABLE_NAME, contentValues, "name=?", new String[] { name });
        if (updateID == -1)
            return false;
        else
            return true;
    }

    public void delete(String id)
    {
        database.execSQL("delete from "+TABLE_NAME+" where "+COLUMN_NAME +"='"+id+"'");
    }
}
