package com.example.esedu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class Сomp : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comp)
        val soupButton: Button = findViewById(R.id.soupTestButton)
        val fitButton: Button = findViewById(R.id.fitTestButton)
        val dishButton: Button = findViewById(R.id.dishTestButton)
        val drinksButton: Button = findViewById(R.id.drinksTestButton)
        val coldsButton: Button = findViewById(R.id.coldsTestButton)
        val resultUserButton: Button = findViewById(R.id.resultUserButton)

        soupButton.setOnClickListener {
            val intent = Intent(this,TestActivity::class.java)
            startActivity(intent)
        }
        fitButton.setOnClickListener {
            val intent = Intent(this,FitTestActivity::class.java)
            startActivity(intent)
        }
        dishButton.setOnClickListener {
            val intent = Intent(this,DishTestActivity::class.java)
            startActivity(intent)
        }
        drinksButton.setOnClickListener {
            val intent = Intent(this, DrinksTestActivity::class.java)
            startActivity(intent)
        }
        coldsButton.setOnClickListener {
            val intent = Intent(this, ColdsTestActivity::class.java)
            startActivity(intent)
        }
        resultUserButton.setOnClickListener {
            val intent = Intent(this, UserResult::class.java)
            startActivity(intent)
        }


    }
}