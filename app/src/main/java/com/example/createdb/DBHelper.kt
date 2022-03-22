package com.example.createdb

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    // below is the method for creating a database by a sqlite query
    override fun onCreate(db: SQLiteDatabase) {
        val defineColumns: String =
            "$ID_COL INTEGER PRIMARY KEY NOT NULL, " +
            "$FNAME_COL VARCHAR(255), " +
            "$LNAME_COL VARCHAR(255), " +
            "$REWARDS_COL INTEGER"

        db.execSQL("CREATE TABLE $TABLE_NAME($defineColumns)")
    }

    private fun replaceTable(db: SQLiteDatabase) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        // this method is to check if table already exists
        replaceTable(db)
    }

    fun refreshDB() {
        val db = this.writableDatabase
        replaceTable(db)
        db.close()
    }

    private fun rowExists(rowId: Int): Boolean {
        val db = this.readableDatabase

        var cursor: Cursor = db.rawQuery("SELECT id FROM $TABLE_NAME WHERE id = $rowId;", null)

        val exists: Boolean = (cursor.count > 0)

        cursor.close()
        db.close()

        return exists
    }

    fun deleteRowIfExists(rowId: Int): Boolean {
        return if (rowExists(rowId)) {
            val db = this.writableDatabase
            db.delete(TABLE_NAME, "$ID_COL == $rowId", null)
            db.close()
            true
        } else {
            false
        }
    }

    // Add a new row, if exists, replace it
    fun addRow(id: Int, firstName: String, lastName: String, rewards: Int) {
        deleteRowIfExists(id)

        // store the content before inserting
        val values = ContentValues()
        values.put(ID_COL, id)
        values.put(FNAME_COL, firstName)
        values.put(LNAME_COL, lastName)
        values.put(REWARDS_COL, rewards)

        val db = this.writableDatabase

        // all values are inserted into database
        db.insert(TABLE_NAME, null, values)

        // closing the database
        db.close()
    }

    // below method is to get
    // all data from our database
    fun getRow(rowId: Int): MemberData? {
        if (rowExists(rowId)) {
            val db = this.readableDatabase

            val colNames: String = COLUMN_NAMES.joinToString()
            val query = "SELECT $colNames FROM $TABLE_NAME WHERE id == $rowId;"
            val cursor: Cursor = db.rawQuery(query, null)

            cursor.moveToFirst()

            val data = MemberData(
                cursor.getInt(cursor.getColumnIndex(ID_COL).toInt()),
                cursor.getString(cursor.getColumnIndex(FNAME_COL).toInt()),
                cursor.getString(cursor.getColumnIndex(LNAME_COL).toInt()),
                cursor.getInt(cursor.getColumnIndex(REWARDS_COL).toInt())
            )

            cursor.close()
            db.close()
            return data
        }
        return null
    }



    companion object{
        // here we have defined variables for our database

        // below is variable for database name
        private const val DATABASE_NAME = "Database"

        // below is the variable for database version
        private const val DATABASE_VERSION = 1

        // below is the variable for table name
        const val TABLE_NAME = "NamesList"

        const val ID_COL = "id"
        const val FNAME_COL = "first_name"
        const val LNAME_COL = "last_name"
        const val REWARDS_COL = "rewards"
        val COLUMN_NAMES = listOf<String>(
            ID_COL, FNAME_COL, LNAME_COL, REWARDS_COL).toTypedArray()

    }
}