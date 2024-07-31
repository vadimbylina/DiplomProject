package com.example.esedu

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.io.FileNotFoundException

class SoupAdapter(var soups: List<Soup>, var context: Context) : RecyclerView.Adapter<SoupAdapter.MyViewHolder>() {
    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var image: ImageView = view.findViewById(R.id.souplistimage)
        var name: TextView = view.findViewById(R.id.souplisttitle)
        var comp: TextView = view.findViewById(R.id.souplistcomp)
        var price: TextView = view.findViewById(R.id.souplistprice)
        var legend: TextView = view.findViewById(R.id.soupLegend)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_soup_in_list, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return soups.size
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.name.text = soups[position].name
        holder.comp.text = soups[position].composition
        holder.price.text = soups[position].price.toString() + " руб"
        holder.legend.text = soups[position].Legend

        val imageIdentifier = soups[position].image

        if (imageIdentifier.startsWith("file://")) {
            val imagePath = imageIdentifier.substring("file://".length)
            val imageFile = File(imagePath)

            if (imageFile.exists()) {
                val drawable = Drawable.createFromPath(imageFile.absolutePath)
                holder.image.setImageDrawable(drawable)
                return
            }
        }

        val imageId = context.resources.getIdentifier(imageIdentifier, "drawable", context.packageName)
        if (imageId != 0) {
            holder.image.setImageResource(imageId)
        } else {
              holder.image.setImageDrawable(null)
        }

    }

}
