package com.example.esedu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView

class desertActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_desert)
        var name: TextView = findViewById(R.id.desertlisttitleone)
        val comp: TextView = findViewById(R.id.desertlisttext)
        val price: TextView = findViewById(R.id.desertrice)
        val image: ImageView = findViewById(R.id.desertlistimages)

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