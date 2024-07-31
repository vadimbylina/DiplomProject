package com.example.esedu

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView

class ResultActivity : AppCompatActivity() {
    private lateinit var dbHelper :DBHelper
    private lateinit var mark:Spinner
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        dbHelper = DBHelper(this,null)
        val userName: TextView = findViewById(R.id.userName)
        val endingSoup: TextView = findViewById(R.id.endingSoup)
        val endingFit: TextView = findViewById(R.id.endingFit)
        val endingDish: TextView = findViewById(R.id.endingDish)
        val endingDrink: TextView = findViewById(R.id.endingDrink)
        val endingDesert: TextView = findViewById(R.id.endingDesert)
        val mark: Spinner = findViewById(R.id.markSpinner)

        val itemName = intent.getStringExtra("userName")
        val itemSoup = intent.getStringExtra("endingSoup")
        val itemFit = intent.getStringExtra("endingFit")
        val itemDish = intent.getStringExtra("endingDish")
        val itemDrink = intent.getStringExtra("endingDrink")
        val itemDesert = intent.getStringExtra("endingDesert")
        userName.text = itemName
        endingDish.text = itemDish ?: ""
        endingDesert.text = itemDesert ?: ""
        endingDrink.text = itemDrink ?: ""
        endingSoup.text = itemSoup ?: ""
        endingFit.text = itemFit ?: ""
        mark.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            @SuppressLint("SuspiciousIndentation")
            override fun onItemSelected(parent: AdapterView<*>, view: android.view.View, position: Int, id: Long) {
                val selectedMark = parent.getItemAtPosition(position).toString()

                    dbHelper.updateMark(getUserIdByLogin(itemName), selectedMark)

            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }
        populateMarkSpinner()
    }
    private fun populateMarkSpinner() {
        val exams = listOf("1", "2", "3", "4", "5")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, exams)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mark.adapter = adapter
    }
    private fun getUserIdByLogin(login: String?): Int? {
        if (login == null) return null

        val db = DBHelper(this, null)
        val query = "SELECT id FROM users WHERE login = ?"
        val cursor = db.readableDatabase.rawQuery(query, arrayOf(login))
        val userId = if (cursor.moveToFirst()) {
            var id = 0
            val idIndex = cursor.getColumnIndex("id")
            if (idIndex != -1) {
                id = cursor.getInt(idIndex)
            }

            cursor.close()
            id
        } else {
            null
        }
        db.close()
        return userId
    }

    }



