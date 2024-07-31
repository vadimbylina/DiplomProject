package com.example.esedu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class DishComp : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dish_comp)
        val dishList: RecyclerView = findViewById(R.id.DishList)
        val dbHelper = DBHelper(this, null)
        val allDish = dbHelper.getAllDish()
        dishList.adapter = DishAdapter(allDish, this)
        dishList.layoutManager = LinearLayoutManager(this)
    }
}