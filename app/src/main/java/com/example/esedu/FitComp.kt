package com.example.esedu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FitComp : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fit_comp)
        val fitsList: RecyclerView = findViewById(R.id.FitsList)
        val dbHelper = DBHelper(this,null)
        val allFits = dbHelper.getAllFits()
        fitsList.adapter = FitAdapter(allFits,this)
        fitsList.layoutManager = LinearLayoutManager(this)

    }
}
