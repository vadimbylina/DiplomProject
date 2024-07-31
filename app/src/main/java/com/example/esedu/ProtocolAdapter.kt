package com.example.esedu

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.threeten.bp.format.DateTimeFormatter
import java.util.Locale

class ProtocolAdapter(var results: List<Result>, var context: Context) : RecyclerView.Adapter<ProtocolAdapter.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val userName: TextView = view.findViewById(R.id.userName)
        val endingSoup: TextView = view.findViewById(R.id.endingSoup)
        val endingFit: TextView = view.findViewById(R.id.endingFit)
        val endingDish: TextView = view.findViewById(R.id.endingDish)
        val endingDrink: TextView = view.findViewById(R.id.endingDrink)
        val endingDesert: TextView = view.findViewById(R.id.endingDesert)
        val total:TextView = view.findViewById(R.id.totalMarkText)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_protocol_in_list, parent, false)
        return MyViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val db = DBHelper(context, null)
        val result = results[position]
        val query = "SELECT result.*, users.login FROM result INNER JOIN users ON result.userID = users.id WHERE result.userID = ?"

        val cursor = db.readableDatabase.rawQuery(query, arrayOf(result.userID.toString()))
        if (cursor.moveToFirst()) {
            val userNameIndex = cursor.getColumnIndex("login")
            if (userNameIndex != -1) {
                val userName = cursor.getString(userNameIndex)
                holder.userName.text = userName
            }
        }
        cursor.close()
        var endingSoup = "Тест супы: "
        var endingFit = "Тест Fitбургеры: "
        var endingDish = "Тест вторые блюда: "
        var endingDrinks = "Тест напитки: "
        var endingColds = "Тест холодные блюда: "

        val dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm", Locale.getDefault())
        if(result.endingSoupTest?.format(dateFormatter) != null) { endingSoup += "пройден" } else { endingSoup += "не пройден"}
        if(result.endingFitTest?.format(dateFormatter) != null) { endingFit += "пройден" } else { endingFit += "не пройден"}
        if(result.endingDishTest?.format(dateFormatter) != null) { endingDish += "пройден" } else { endingDish += "не пройден"}
        if(result.endingDrinksTest?.format(dateFormatter) != null) { endingDrinks += "пройден" } else { endingDrinks += "не пройден"}
        if(result.endingDesertsTest?.format(dateFormatter) != null) { endingColds += "пройден" } else { endingColds += "не пройден"}
        if (result.endingSoupTest != null && result.endingFitTest != null && result.endingDishTest != null &&
            result.endingDesertsTest != null && result.endingDrinksTest != null) {
            holder.total.text = "Сотрудник допущен к работе"
        } else {
            holder.total.text = "Сотрудник не допущен к работе"
        }

        holder.endingSoup.text = endingSoup
            holder.endingFit.text = endingFit
            holder.endingDish.text = endingDish
            holder.endingDesert.text = endingDrinks
            holder.endingDrink.text = endingColds

        }
    override fun getItemCount(): Int {
        return results.size
    }

}
