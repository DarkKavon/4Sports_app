package com.example.am_lecture

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.text.SimpleDateFormat
import java.util.*

class DBHelper(context: Context): SQLiteOpenHelper(context,DATABASE_NAME,
    null, DATABASE_VER) {
    companion object {
        private val DATABASE_VER = 2
        private val DATABASE_NAME = "runnerapp.db"
        //Table
        private val TABLE_NAME = "results"
        private val COL_ID = "Id"
        private val COL_ROUTE_ID = "route_id"
        private val COL_RESULT = "result"
        private val COL_DATE = "date"
    }
    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TABLE_QUERY = ("CREATE TABLE $TABLE_NAME ($COL_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COL_ROUTE_ID INTEGER, $COL_RESULT INTEGER, $COL_DATE TEXT)")
        db!!.execSQL(CREATE_TABLE_QUERY)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun selectBestTime(key:Int): Int {
        val selectQuery =
            "SELECT * FROM $TABLE_NAME WHERE $COL_ROUTE_ID = $key ORDER BY $COL_RESULT ASC LIMIT 1"
        val db = this.writableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()) {
            return cursor.getInt(cursor.getColumnIndexOrThrow(COL_RESULT))
        }
        return  0
    }

    fun selectBestDate(key:Int): String {
        val selectQuery =
            "SELECT * FROM $TABLE_NAME WHERE $COL_ROUTE_ID = $key ORDER BY $COL_RESULT ASC LIMIT 1"
        val db = this.writableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()) {
            return cursor.getString(cursor.getColumnIndexOrThrow(COL_DATE))
        }
        return  ""
    }

    fun selectLastTime(key:Int): Int {
        val selectQuery =
            "SELECT * FROM $TABLE_NAME WHERE $COL_ROUTE_ID = $key ORDER BY $COL_ID DESC LIMIT 1"
        val db = this.writableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()) {
            return cursor.getInt(cursor.getColumnIndexOrThrow(COL_RESULT))
        }
        return  0
    }

    fun selectLastDate(key:Int): String {
        val selectQuery =
            "SELECT * FROM $TABLE_NAME WHERE $COL_ROUTE_ID = $key ORDER BY $COL_ID DESC LIMIT 1"
        val db = this.writableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()) {
            return cursor.getString(cursor.getColumnIndexOrThrow(COL_DATE))
        }
        return  ""
    }

    fun clear() {
        this.writableDatabase.execSQL("DELETE FROM $TABLE_NAME")
    }

    fun cleartable() {
        this.writableDatabase.execSQL("DROP TABLE $TABLE_NAME")
    }

    fun makeTable() {
        this.writableDatabase.execSQL("CREATE TABLE $TABLE_NAME ($COL_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COL_ROUTE_ID INTEGER, $COL_RESULT INTEGER, $COL_DATE TEXT)")
    }

    fun addResult(route_id : Int, result : Int){
        val db= this.writableDatabase
        val values = ContentValues()
        values.put(COL_ROUTE_ID, route_id)
        values.put(COL_RESULT, result)
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val currentDate = sdf.format(Date())
        values.put(COL_DATE, currentDate)
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

}