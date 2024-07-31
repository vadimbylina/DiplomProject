package com.example.esedu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class menumenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menumenu)
        var gsoup: Button = findViewById(R.id.soupmenu)
        var gfits: Button = findViewById(R.id.fitburgersmenu)
        var qdish: Button = findViewById(R.id.dishesmenu)
        val qdesert: Button = findViewById(R.id.desertMenu)
        val qdrinks: Button = findViewById(R.id.drinksButton)
        gsoup.setOnClickListener {
            val intent = Intent(this,SoupComp::class.java)
            startActivity(intent)
        }
        gfits.setOnClickListener {
            val intent = Intent(this,FitComp::class.java)
            startActivity(intent)
        }
        qdish.setOnClickListener {
            val intent = Intent(this,DishComp::class.java)
            startActivity(intent)
        }
        qdesert.setOnClickListener {
            val intent = Intent(this, ColdsComp::class.java)
            startActivity(intent)
        }
        qdrinks.setOnClickListener {
            val intent = Intent(this, DrinksComp::class.java)
            startActivity(intent)
        }
    }
}