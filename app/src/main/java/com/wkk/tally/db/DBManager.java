package com.wkk.tally.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DBManager {

    private static SQLiteDatabase db;

    public static void initDB(Context context)
    {
        DBOpenHelper helper = new DBOpenHelper(context);
        db = helper.getWritableDatabase();
    }

    /*
    向数据库中查询各种支出收入类型的数据
     */
    public static List<TypeBean> getTypeList(int kind)
    {
        List<TypeBean> list = new ArrayList<>();
        String sql = "select * from typetb where kind =" + kind;
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()){
            @SuppressLint("Range") String typename = cursor.getString(cursor.getColumnIndex("typename"));
            @SuppressLint("Range") int imageid = cursor.getInt(cursor.getColumnIndex("imageId"));
            @SuppressLint("Range") int simageid = cursor.getInt(cursor.getColumnIndex("sImageId"));
            @SuppressLint("Range") int kind1 = cursor.getInt(cursor.getColumnIndex("kind"));
            @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id"));
            TypeBean typeBean = new TypeBean(id, typename, imageid, simageid, kind1);
            list.add(typeBean);
        }
        cursor.close();
        return list;
    }

    public static void insertAccountBeanToTB (AccountBean accountBean)
    {
        System.out.println(accountBean.toString());
        if (accountBean.getBeiZhu().length() == 0)
        {
            accountBean.setBeiZhu(" NULL");
        }

        if (accountBean.getTime().length() == 0)
        {
            accountBean.setTime("NULL");
        }

        if (accountBean.getDay() == 0)
        {
            accountBean.setDay(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        }
        if (accountBean.getMonth() == 0)
        {
            accountBean.setMonth(Calendar.getInstance().get(Calendar.MONTH) + 1);

        }
        if (accountBean.getYear() == 0)
        {
            accountBean.setYear(Calendar.getInstance().get(Calendar.YEAR));

        }
        System.out.println(accountBean.toString());
        ContentValues contentValues = new ContentValues();
        contentValues.put("typename", accountBean.getTypename());
        contentValues.put("sImageId", accountBean.getsImageId());
        contentValues.put("beizhu", accountBean.getBeiZhu());
        contentValues.put("money", accountBean.getMoney());
        contentValues.put("time", accountBean.getTime());
        contentValues.put("year", accountBean.getYear());
        contentValues.put("month", accountBean.getMonth());
        contentValues.put("day", accountBean.getDay());
        contentValues.put("kind", accountBean.getKind());
        long asd = db.insert("accounttb", null, contentValues);
        Log.e("WKK", "insertAccountBeanToTB: " + "数据插入成功 " + asd);
    }
    // 按照日期查询数据库中的记录并返回   根据年月日
    public static List<AccountBean> getAccountListOnDayFromAccountTb (int year, int month, int day)
    {
        List<AccountBean> list = new ArrayList<>();
        String sql = "select * from accounttb where year=? and month=? and day=? order by id desc";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", day + ""});
        while (cursor.moveToNext()) {
            @SuppressLint("Range")int id = cursor.getInt(cursor.getColumnIndex("id"));
            @SuppressLint("Range")String typename = cursor.getString(cursor.getColumnIndex("typename"));
            @SuppressLint("Range")String beizhu = cursor.getString(cursor.getColumnIndex("beizhu"));
            @SuppressLint("Range")String time = cursor.getString(cursor.getColumnIndex("time"));
            @SuppressLint("Range")int sImageId = cursor.getInt(cursor.getColumnIndex("sImageId"));
            @SuppressLint("Range")int kind = cursor.getInt(cursor.getColumnIndex("kind"));
            @SuppressLint("Range")float money = cursor.getFloat(cursor.getColumnIndex("money"));
            AccountBean accountBean = new AccountBean(id, typename, sImageId, beizhu, money, time, year, month, day, kind);
            list.add(accountBean);
        }
        return list;
    }


    // 按照日期查询数据库中的记录并返回   根据年月
    public static List<AccountBean> getAccountListOnMonthFromAccountTb (int year, int month)
    {
        List<AccountBean> list = new ArrayList<>();
        String sql = "select * from accounttb where year=? and month=?  order by id desc";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + ""});
        while (cursor.moveToNext()) {
            @SuppressLint("Range")int id = cursor.getInt(cursor.getColumnIndex("id"));
            @SuppressLint("Range")String typename = cursor.getString(cursor.getColumnIndex("typename"));
            @SuppressLint("Range")String beizhu = cursor.getString(cursor.getColumnIndex("beizhu"));
            @SuppressLint("Range")String time = cursor.getString(cursor.getColumnIndex("time"));
            @SuppressLint("Range")int sImageId = cursor.getInt(cursor.getColumnIndex("sImageId"));
            @SuppressLint("Range")int kind = cursor.getInt(cursor.getColumnIndex("kind"));
            @SuppressLint("Range")float money = cursor.getFloat(cursor.getColumnIndex("money"));
            @SuppressLint("Range")int day = cursor.getInt(cursor.getColumnIndex("day"));
            AccountBean accountBean = new AccountBean(id, typename, sImageId, beizhu, money, time, year, month, day, kind);
            list.add(accountBean);
        }
        return list;
    }


    public static float getTotalOneMonth (int year, int month, int kind)
    {
        float consume = 0;
        String sql = "select * from accounttb where year= ? and month=? and kind=? order by id desc";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", kind + ""});
        while (cursor.moveToNext()) {
            @SuppressLint("Range")float money = cursor.getFloat(cursor.getColumnIndex("money"));
            consume += money;
        }
        return consume;
    }

    public static float getTotalOnDay  (int year, int month, int day, int kind)
    {
        float consume = 0;
        String sql = "select * from accounttb where year=? and month=? and day=? and kind=?";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", day + "", kind + ""});
        while (cursor.moveToNext()) {
            @SuppressLint("Range")float money = cursor.getFloat(cursor.getColumnIndex("money"));
            consume += money;
        }
        return consume;
    }
        // 根据数据中的条目id号查询并删除记录
    public static int deleteItemFromAccountTbByID(int id)
    {
        int i = db.delete("accounttb", "id=?", new String[]{id + ""});
        return i;
    }
    // 根据用户输入的备注条件查询并返回
    public static List<AccountBean> queryFromAccountTbByString(String string)
    {
        List<AccountBean> list = new ArrayList<>();
        String sql = "select * from accounttb where beizhu like '%" + string + "%'";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            @SuppressLint("Range")int id = cursor.getInt(cursor.getColumnIndex("id"));
            @SuppressLint("Range")String typename = cursor.getString(cursor.getColumnIndex("typename"));
            @SuppressLint("Range")String beizhu = cursor.getString(cursor.getColumnIndex("beizhu"));
            @SuppressLint("Range")String time = cursor.getString(cursor.getColumnIndex("time"));
            @SuppressLint("Range")int sImageId = cursor.getInt(cursor.getColumnIndex("sImageId"));
            @SuppressLint("Range")int kind = cursor.getInt(cursor.getColumnIndex("kind"));
            @SuppressLint("Range")float money = cursor.getFloat(cursor.getColumnIndex("money"));
            @SuppressLint("Range")int year = cursor.getInt(cursor.getColumnIndex("year"));
            @SuppressLint("Range")int month = cursor.getInt(cursor.getColumnIndex("month"));
            @SuppressLint("Range")int day = cursor.getInt(cursor.getColumnIndex("day"));
            AccountBean accountBean = new AccountBean(id, typename, sImageId, beizhu, money, time, year, month, day, kind);
            list.add(accountBean);
        }
        return list;
    }

    public static List<Integer> getYearListFromDB()
    {
        List<Integer> yearList = new ArrayList<>();
        String sql = "select distinct(year) from accounttb order by year asc";
        Cursor cursor = db.rawQuery(sql, null);

        while (cursor.moveToNext()) {
            @SuppressLint("Range") int year = cursor.getInt(cursor.getColumnIndex("year"));
            yearList.add(year);
        }
        return yearList;
    }
    public static void deleteALLAccount()
    {
        String sql = "delete from accounttb";
        db.execSQL(sql);
    }
}
