package com.example.esedu

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.io.File

class FitAdapter(var fits: List<FitBurger>, var context: Context) : RecyclerView.Adapter<FitAdapter.MyViewHolder>() {
    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
    {
        var image: ImageView = view.findViewById(R.id.fitlistimage)
        var name: TextView = view.findViewById(R.id.fitlisttitle)
        var comp: TextView = view.findViewById(R.id.fitlistcomp)
        var price: TextView = view.findViewById(R.id.fitlistprice)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_fit_in_list, parent,false)
        return FitAdapter.MyViewHolder(view)
    }

    override fun getItemCount(): Int {
return fits.count()
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.name.text = fits[position].name
        holder.comp.text = fits[position].composition
        holder.price.text = fits[position].price.toString()+ " руб"
        val imageIdentifier = fits[position].image

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