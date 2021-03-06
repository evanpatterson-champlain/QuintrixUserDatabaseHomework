package com.example.createdb

import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.createdb.DBHelper.Companion.COLUMN_NAMES

class MainActivity : AppCompatActivity() {

    var database: DBHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        database = DBHelper(this, null)
        refreshDB()

        val butAddUser = findViewById<Button>(R.id.button_adduser)
        butAddUser.setOnClickListener {
            addUserFromEditTexts()
        }

        val tvShowInfo = findViewById<TextView>(R.id.tv_showInfo)
        val butSearchUser = findViewById<Button>(R.id.button_searchID)
        val searchID = findViewById<EditText>(R.id.searchID)
        butSearchUser.setOnClickListener {
            val idStr = searchID.text.toString()
            searchID.text.clear()
            if (isNumeric(idStr)) {
                val memberData = getMemberDB(idStr.toUInt())
                if (memberData != null) {
                    tvShowInfo.text = memberData!!.toString()
                }
                else {
                    tvShowInfo.text = "User does not exist."
                }
            }
            else {
                tvShowInfo.text = "Wrong input type."
            }
        }

        val butDelete = findViewById<Button>(R.id.button_delete)
        butDelete.setOnClickListener {
            val idStr = searchID.text.toString()
            searchID.text.clear()
            if (isNumeric(idStr)) {
                val id: UInt = idStr.toUInt()
                val deleted = deleteMemberDB(id)
                if (deleted) {
                    tvShowInfo.text = "Deleted successfully."
                }
                else {
                    tvShowInfo.text = "Does not exist."
                }
            }
        }



    }

    // check if a string is numeric
    private fun isNumeric(str: String): Boolean {
        val numbersOnly = str.filter { it.isDigit() }
        return numbersOnly == str
    }

    // remove the table and create an empty one
    private fun refreshDB() {
        if (database != null) {
            val db = database!!
            db.refreshDB()
        }
    }

    // add a new member to the database
    private fun newMemberDB(id: UInt, firstName: String, lastName: String, rewards: UInt) {
        if (database != null) {
            val db = database!!
            db.addRow(id.toInt(), firstName, lastName, rewards.toInt())
        }
    }

    // get a members data from the database
    private fun getMemberDB(id: UInt): MemberData? {
        if (database != null) {
            val db = database!!
            return db.getRow(id.toInt())
        }
        return null
    }

    // delete a member from the database
    private fun deleteMemberDB(id: UInt): Boolean {
        if (database != null) {
            val db = database!!
            return db.deleteRowIfExists(id.toInt())
        }
        return false
    }

    // get info from edittext layout objects and add it into the database
    private fun addUserFromEditTexts() {
        val etEnterID = findViewById<EditText>(R.id.enterID)
        val etEnterFname = findViewById<EditText>(R.id.enterFirstName)
        val etEnterLname = findViewById<EditText>(R.id.enterLastName)
        val etEnterRewards = findViewById<EditText>(R.id.enterRewards)

        val idStr: String = etEnterID.text.toString()
        val enterFname: String = etEnterFname.text.toString()
        val enterLname: String = etEnterLname.text.toString()
        val rewardsStr: String = etEnterRewards.text.toString()

        etEnterID.text.clear()
        etEnterFname.text.clear()
        etEnterLname.text.clear()
        etEnterRewards.text.clear()

        if (isNumeric(idStr) && isNumeric(rewardsStr)) {
            newMemberDB(idStr.toUInt(), enterFname, enterLname, rewardsStr.toUInt())
        }
    }






}