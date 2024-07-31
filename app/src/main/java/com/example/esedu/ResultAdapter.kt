package com.example.esedu

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.threeten.bp.format.DateTimeFormatter
import java.util.Locale

class ResultAdapter(var results: List<Result>, var context: Context) : RecyclerView.Adapter<ResultAdapter.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val userName: TextView = view.findViewById(R.id.userName)
        val endingSoup: TextView = view.findViewById(R.id.endingSoup)
        val endingFit: TextView = view.findViewById(R.id.endingFit)
        val endingDish: TextView = view.findViewById(R.id.endingDish)
        val endingDrink: TextView = view.findViewById(R.id.endingDrink)
        val endingDesert: TextView = view.findViewById(R.id.endingDesert)
        val totalMark: TextView = view.findViewById(R.id.totalMarkText)
        val mark: Spinner = view.findViewById(R.id.markSpinner0)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_result_in_list, parent, false)
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

        val dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm", Locale.getDefault())

        holder.endingSoup.text = "Дата прохождения теста на знание супов: " + (result.endingSoupTest?.format(dateFormatter) ?: "ещё не пройден")
        holder.endingFit.text = "Дата прохождения теста на знание Fitбургеров: " + (result.endingFitTest?.format(dateFormatter) ?: "ещё не пройден")
        holder.endingDish.text = "Дата прохождения теста на знание вторых блюд: " + (result.endingDishTest?.format(dateFormatter) ?: "ещё не пройден")
        holder.endingDesert.text = "Дата прохождения теста на знание десертов: " + (result.endingDesertsTest?.format(dateFormatter) ?: "ещё не пройден")
        holder.endingDrink.text = "Дата прохождения теста на знание напитков: " + (result.endingDrinksTest?.format(dateFormatter) ?: "ещё не пройден")

        // Проверка, все ли тесты пройдены
        if (result.endingSoupTest != null && result.endingFitTest != null && result.endingDishTest != null &&
            result.endingDesertsTest != null && result.endingDrinksTest != null) {
            holder.totalMark.text = "Вы допущены к работе"
        } else {
            holder.totalMark.text = "Вы не допущены к работе"
        }

        populateMarkSpinner(holder, result.userID)

        holder.mark.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedMark = parent.getItemAtPosition(position).toString()
                db.updateMark(result.userID, selectedMark)
                holder.totalMark.text = "Оценка администратора: $selectedMark"
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }
    }

    override fun getItemCount(): Int {
        return results.size
    }

    private fun populateMarkSpinner(holder: MyViewHolder, userId: Int?) {
        if (userId != null) {
            val exams = listOf("1", "2", "3", "4", "5")
            val adapter = ArrayAdapter(holder.itemView.context, R.layout.custom_spinner_item, exams)
            adapter.setDropDownViewResource(R.layout.custom_spinner_item)
            holder.mark.adapter = adapter

            val db = DBHelper(holder.itemView.context, null)
            val savedMark = db.getMarkForUser(userId)
            if (savedMark != null) {
                val position = exams.indexOf(savedMark)
                if (position != -1) {
                    holder.mark.setSelection(position)
                }
            }
        }
    }
}
