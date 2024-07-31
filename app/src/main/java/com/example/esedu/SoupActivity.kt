package com.example.esedu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import org.w3c.dom.Text

class SoupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_soup)

        var name: TextView = findViewById(R.id.itemlisttitleone)
        val comp: TextView = findViewById(R.id.itemlisttext)
        val price: TextView = findViewById(R.id.souprice)
        val image: ImageView = findViewById(R.id.itemlistimages)
        val legend: TextView = findViewById(R.id.soupLegend)

        val itemName = intent.getStringExtra("itemTitle")
        val itemComp = intent.getStringExtra("itemText")
        val itemPrice = intent.getStringExtra("itemPrice")
        val itemImageName = intent.getStringExtra("itemImage")
        val itemLegend = intent.getStringExtra("itemLegend")
        name.text = itemName
        comp.text = itemComp
        price.text =itemPrice
        if (itemLegend!=null)
        {
            legend.text = "Легенда блюда $itemLegend"
        }
            legend.text = ""

        val imageId = resources.getIdentifier(itemImageName, "drawable", packageName)
        image.setImageResource(imageId)
    }
}