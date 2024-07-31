package com.example.esedu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class DesertComp : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_desert_comp)
        val desertList: RecyclerView = findViewById(R.id.DesertsList)
        val dbHelper = DBHelper(this,null)
        val allFits = dbHelper.getAllDeserts()
        desertList.adapter = desertAdapter(allFits,this)
        desertList.layoutManager = LinearLayoutManager(this)
    }
}