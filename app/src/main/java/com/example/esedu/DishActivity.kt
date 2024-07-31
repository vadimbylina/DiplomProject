package com.example.esedu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import org.w3c.dom.Text

class DishActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dish)
        var name: TextView = findViewById(R.id.dishlisttitleone)
        var comp: TextView = findViewById(R.id.dishlisttext)
        var price: TextView = findViewById(R.id.dishrice)
        var image: ImageView = findViewById(R.id.dishlistimages)

        var itemName = intent.getStringExtra("itemTitle")
        var itemComp = intent.getStringExtra("itemText")
        var itemPrice = intent.getStringExtra("itemPrice")
        var itemImage = intent.getStringExtra("itemImage")
        name.text = itemName
        comp.text = itemComp
        price.text = itemPrice
        val imageId = resources.getIdentifier(itemImage, "drawable", packageName)
        image.setImageResource(imageId)

    }
}