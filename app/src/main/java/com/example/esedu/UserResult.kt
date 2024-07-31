package com.example.esedu

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.jakewharton.threetenabp.AndroidThreeTen
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.DateTimeParseException
import java.util.*

class UserResult : AppCompatActivity() {
    private var login = MainActivity.login

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidThreeTen.init(this)
        setContentView(R.layout.activity_user_result)

        val db = DBHelper(this, null)
        val startDate: EditText = findViewById(R.id.startDateText)
        val endDate: EditText = findViewById(R.id.endDateText)
        val text: TextView = findViewById(R.id.resultText)
        val userResultButton: Button = findViewById(R.id.userResultButton)

        val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

        startDate.setOnClickListener {
            showDatePickerDialog(startDate, dateFormatter)
        }

        endDate.setOnClickListener {
            showDatePickerDialog(endDate, dateFormatter)
        }

        userResultButton.setOnClickListener {
            val startDateStr = startDate.text.toString()
            val endDateStr = endDate.text.toString()

            if (startDateStr.isNotEmpty() && endDateStr.isNotEmpty() && login != null) {
                val userId = getUserIdByLogin(login)
                if (userId != null) {
                    try {
                        val startDateLocal = LocalDate.parse(startDateStr, dateFormatter)
                        val endDateLocal = LocalDate.parse(endDateStr, dateFormatter)

                        val startDateFormatted = startDateLocal.atStartOfDay()
                        val endDateFormatted = endDateLocal.atTime(LocalTime.MAX)

                        val results = db.getResultsByUser(userId, startDateFormatted, endDateFormatted)
                        val resultStringBuilder = StringBuilder()
                        for (result in results) {
                            resultStringBuilder.append("Дата прохождения теста по супам: ${result.endingSoupTest?.format(dateFormatter) ?: "Тест не был пройден в выбранном диапазоне"}\n")
                            resultStringBuilder.append("Дата прохождения теста по Fiбургерам: ${result.endingFitTest?.format(dateFormatter) ?: "Тест не был пройден в выбранном диапазоне"}\n")
                            resultStringBuilder.append("Дата прохождения теста по вторым блюдам: ${result.endingDishTest?.format(dateFormatter) ?: "Тест не был пройден в выбранном диапазоне"}\n")
                            resultStringBuilder.append("Дата прохождения теста по напиткам: ${result.endingDrinksTest?.format(dateFormatter) ?: "Тест не был пройден в выбранном диапазоне"}\n")
                            resultStringBuilder.append("Дата прохождения теста по холодным блюдам: ${result.endingDesertsTest?.format(dateFormatter) ?: "Тест не был пройден в выбранном диапазоне"}\n\n")
                            resultStringBuilder.append("Оценка администратора: ${result.mark}\n\n")
                            if(result.endingSoupTest?.format(dateFormatter) != null && result.endingFitTest?.format(dateFormatter) != null && result.endingDishTest?.format(dateFormatter) != null && result.endingDrinksTest?.format(dateFormatter) != null && result.endingDesertsTest?.format(dateFormatter) != null )
                            {
                                resultStringBuilder.append("Вы допущены к работу")
                            }
                        }
                        text.text = resultStringBuilder.toString()
                    } catch (e: DateTimeParseException) {
                        text.text = "Ошибка при разборе даты"
                    } catch (e: Exception) {
                        text.text = "Ошибка при получении результатов из базы данных"
                        e.printStackTrace()
                    }
                } else {
                    text.text = "Ошибка при получении ID пользователя"
                }
            } else {
                text.text = "Пожалуйста, заполните все поля"
            }
        }
    }

    private fun showDatePickerDialog(editText: EditText, dateFormatter: DateTimeFormatter) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = LocalDate.of(selectedYear, selectedMonth + 1, selectedDay)
                editText.setText(selectedDate.format(dateFormatter))
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }

    private fun getUserIdByLogin(login: String?): Int? {
        if (login == null) return null

        val db = DBHelper(this, null)
        val query = "SELECT id FROM users WHERE login = ?"
        val cursor = db.readableDatabase.rawQuery(query, arrayOf(login))
        val userId = if (cursor.moveToFirst()) {
            var id: Int = 0
            val idIndex = cursor.getColumnIndex("id")
            if (idIndex != -1) {
                id = cursor.getInt(idIndex)
            }

            cursor.close()
            id
        } else {
            null
        }
        db.close()
        return userId
    }
}
