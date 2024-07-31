package com.example.esedu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ColdsComp : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_colds_comp)
        val fitsList: RecyclerView = findViewById(R.id.ColdsList)
        val dbHelper = DBHelper(this,null)
        val allFits = dbHelper.getAllColds()
        fitsList.adapter = ColdSoupAdapter(allFits,this)
        fitsList.layoutManager = LinearLayoutManager(this)

    }
}