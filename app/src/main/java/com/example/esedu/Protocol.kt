package com.example.esedu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class Protocol : AppCompatActivity() {
    private lateinit var dbHelper: DBHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_protocol)
        dbHelper = DBHelper(this,null)
        val userName: TextView = findViewById(R.id.userName)
        val endingSoup: TextView = findViewById(R.id.endingSoup)
        val endingFit: TextView = findViewById(R.id.endingFit)
        val endingDish: TextView = findViewById(R.id.endingDish)
        val endingDrink: TextView = findViewById(R.id.endingDrink)
        val endingDesert: TextView = findViewById(R.id.endingDesert)

        val itemName = intent.getStringExtra("userName")
        val itemSoup = intent.getStringExtra("endingSoup")
        val itemFit = intent.getStringExtra("endingFit")
        val itemDish = intent.getStringExtra("endingDish")
        val itemDrink = intent.getStringExtra("endingDrink")
        val itemDesert = intent.getStringExtra("endingDesert")
    }
}