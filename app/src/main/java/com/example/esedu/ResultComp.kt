package com.example.esedu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.esedu.Result

class ResultComp : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result_comp)
        val resultList : RecyclerView = findViewById(R.id.ResultsList)
        val db = DBHelper(this,null)

        val allResult = db.getAllResults()

        resultList.adapter = ResultAdapter(allResult, this)
        resultList.layoutManager = LinearLayoutManager(this)
    }
}