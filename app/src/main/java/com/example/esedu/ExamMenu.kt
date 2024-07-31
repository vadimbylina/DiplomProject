package com.example.esedu

import android.content.Intent
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast

class ExamMenu : AppCompatActivity() {
    companion object {
        var login: String? = null
    }

    private lateinit var dbHelper: DBHelper
    private lateinit var userSpinner: Spinner
    private lateinit var examSpinner: Spinner
    private lateinit var examButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exam_menu)
        dbHelper = DBHelper(this, null)
        userSpinner = findViewById(R.id.UserSpinner)
        examSpinner = findViewById(R.id.TestSpinner)
        examButton = findViewById(R.id.examButton)
        val textColor = resources.getColor(R.color.textColor)

        populateUserSpinner()
        populateExamSpinner()

        examButton.setOnClickListener {
            examWindow()
        }

        userSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View, position: Int, id: Long) {
                (selectedItemView as? TextView)?.setTextColor(textColor)
                val selectedCategory = parentView.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
                // Ничего не делаем
            }
        }

        examSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View, position: Int, id: Long) {
                (selectedItemView as? TextView)?.setTextColor(textColor)
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
                // Ничего не делаем
            }
        }
    }

    private fun examWindow() {
        val selectedCategory = examSpinner.selectedItem.toString()
        login = userSpinner.selectedItem.toString()
        if (userSpinner.selectedItem != 0) {
            when (selectedCategory) {
                "Супы" -> {
                    val intent = Intent(this, SoupExam::class.java)
                    startActivity(intent)
                }
                "Fitбургеры" -> {
                    val intent = Intent(this, FitExam::class.java)
                    startActivity(intent)
                }
                "Вторые блюда" -> {
                    val intent = Intent(this, DishExam::class.java)
                    startActivity(intent)
                }
                "Напитки" -> {
                    val intent = Intent(this, DrinksExam::class.java)
                    startActivity(intent)
                }
                "Холодные блюда" -> {
                    val intent = Intent(this, ColdsExam::class.java)
                    startActivity(intent)
                }
            }
        } else {
            Toast.makeText(this, "Выберите пользователя", Toast.LENGTH_SHORT).show()
        }
    }

    private fun populateUserSpinner() {
        val categories: ArrayList<String> = ArrayList()
        val cursor: Cursor = dbHelper.readableDatabase.rawQuery("SELECT login FROM users", null)

        while (cursor.moveToNext()) {
            val categoryNameIndex = cursor.getColumnIndex("login")
            if (categoryNameIndex != -1) {
                val categoryName = cursor.getString(categoryNameIndex)
                categories.add(categoryName)
            }
        }
        cursor.close()

        val adapter = ArrayAdapter(this, R.layout.custom_spinner_item, categories)
        adapter.setDropDownViewResource(R.layout.custom_spinner_item)
        userSpinner.adapter = adapter
    }

    private fun populateExamSpinner() {
        val exams = listOf("Супы", "Fitбургеры", "Вторые блюда", "Напитки", "Холодные блюда")

        val adapter = ArrayAdapter(this, R.layout.custom_spinner_item, exams)
        adapter.setDropDownViewResource(R.layout.custom_spinner_item)
        examSpinner.adapter = adapter
    }
}
