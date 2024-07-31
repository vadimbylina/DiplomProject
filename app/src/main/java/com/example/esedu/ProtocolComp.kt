package com.example.esedu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ProtocolComp : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_protocol_comp)
        val protList: RecyclerView = findViewById(R.id.ResultsList)
        val db = DBHelper(this,null)
        val allProt = db.getAllResults()

        protList.adapter = ProtocolAdapter(allProt,this)
        protList.layoutManager = LinearLayoutManager(this)
    }
}