package com.example.esedu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView

class FitBurgerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fit_burger)
        var name: TextView = findViewById(R.id.fitlisttitleone)
        val comp: TextView = findViewById(R.id.fitlisttext)
        val price: TextView = findViewById(R.id.fitrice)
        val image: ImageView = findViewById(R.id.fitlistimages)

        val itemName = intent.getStringExtra("itemTitle")
        val itemComp = intent.getStringExtra("itemText")
        val itemPrice = intent.getStringExtra("itemPrice")
        val itemImageName = intent.getStringExtra("itemImage")
        // Устанавливаем данные на активити
        name.text = itemName
        comp.text = itemComp
        price.text = itemPrice


        // Устанавливаем изображение, брат
        val imageId = resources.getIdentifier(itemImageName, "drawable", packageName)
        image.setImageResource(imageId)
    }
}