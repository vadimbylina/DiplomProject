package com.example.esedu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SoupComp : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_soup_comp)
        val soupsList: RecyclerView = findViewById(R.id.ItemsList)
        val dbHelper = DBHelper(this,null)

        val allSoups = dbHelper.getAllSoups()

        soupsList.adapter = SoupAdapter(allSoups,this)
        soupsList.layoutManager = LinearLayoutManager(this)
    }
}
