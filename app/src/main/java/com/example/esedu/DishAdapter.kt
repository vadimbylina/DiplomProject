package com.example.esedu

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.io.File

class DishAdapter (var dishes: List<Dish>,var context: Context) : RecyclerView.Adapter<DishAdapter.MyViewHolder>()
{
    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
    {
        var image: ImageView = view.findViewById(R.id.dishlistimage)
        var name: TextView = view.findViewById(R.id.dishlisttitle)
        var comp: TextView = view.findViewById(R.id.dishlistcomp)
        var price: TextView = view.findViewById(R.id.dishlistprice)
        val btn: Button = view.findViewById(R.id.dishlistbutton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_dish_in_list, parent, false)
        return DishAdapter.MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dishes.count()
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.name.text = dishes[position].name
        holder.comp.text = dishes[position].composition
        holder.price.text = dishes[position].price.toString()+ " руб"
        val imageIdentifier = dishes[position].image

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
        holder.image.setImageResource(imageId)
        holder.btn.setOnClickListener {
            val intent = Intent(context, DishActivity::class.java)
            intent.putExtra("itemTitle", dishes[position].name)
            intent.putExtra("itemText", dishes[position].composition)
            intent.putExtra("itemPrice", dishes[position].price)
            intent.putExtra("itemImage", dishes[position].image)
            context.startActivity(intent)
        }
    }

}