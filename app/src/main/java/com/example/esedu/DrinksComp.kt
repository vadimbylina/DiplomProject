package com.example.esedu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class DrinksComp : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drinks_comp)
        val drinksList: RecyclerView = findViewById(R.id.DrinksList)
        val dbHelper = DBHelper(this,null)
        val allDrinks = dbHelper.getAllDrinks()
        drinksList.adapter = DrinksAdapter(allDrinks,this)
        drinksList.layoutManager = LinearLayoutManager(this)
    }
}