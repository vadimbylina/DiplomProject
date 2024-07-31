package com.example.esedu

import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.jakewharton.threetenabp.AndroidThreeTen
import org.threeten.bp.LocalDateTime
import kotlin.math.log

class FitExam : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper
    private lateinit var db: SQLiteDatabase
    private lateinit var cursor: Cursor

    private lateinit var textViewTest: TextView
    private lateinit var radioGroup: RadioGroup
    private lateinit var radioButton1: RadioButton
    private lateinit var radioButton2: RadioButton
    private lateinit var radioButton3: RadioButton
    private lateinit var radioButton4: RadioButton
    private lateinit var nextButton: Button
    private val usedSoupNames = mutableSetOf<String>()

    private var currentQuestionIndex = 0
    private var correctAnswersCount = 0
    private var isAnswerChecked = false
    private var login = ExamMenu.login


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        AndroidThreeTen.init(this)
        // Initialize UI elements
        textViewTest = findViewById(R.id.textViewTest)
        radioGroup = findViewById(R.id.radioGroup)
        radioButton1 = findViewById(R.id.radioButton)
        radioButton2 = findViewById(R.id.radioButton2)
        radioButton3 = findViewById(R.id.radioButton3)
        radioButton4 = findViewById(R.id.radioButton4)
        nextButton = findViewById(R.id.nextButton)

        // Initially disable the next button
        nextButton.isEnabled = false

        // Initialize the database helper
        dbHelper = DBHelper(this, null)

        // Get a readable database
        db = dbHelper.readableDatabase

        // Query the database and get a cursor with the data
        cursor = db.rawQuery("SELECT composition FROM fits", null)

        // Display the first question
        displayQuestion()

        // Set a listener for the RadioGroup to enable the next button when an option is selected
        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            nextButton.isEnabled = checkedId != -1
        }

        // Set a click listener for the "Next" button
        nextButton.setOnClickListener {
            if (isAnswerChecked) {
                currentQuestionIndex++
                if (currentQuestionIndex < cursor.count) {
                    displayQuestion()
                    resetRadioButtons()
                    isAnswerChecked = false
                    nextButton.isEnabled = false
                } else {
                    showResult()
                }
            } else {
                checkAnswer()
                isAnswerChecked = true
            }
        }
    }

    private fun displayQuestion() {
        // Clear the selection in RadioGroup
        radioGroup.clearCheck()

        // Enable all RadioButtons
        setRadioButtonsEnabled(true)

        // Query the database to get a random soup, excluding those already used
        val randomSoupQuery = "SELECT name, composition FROM fits WHERE name NOT IN (${usedSoupNames.joinToString(",") { "'$it'" }}) ORDER BY RANDOM() LIMIT 1"
        val randomSoupCursor = db.rawQuery(randomSoupQuery, null)

        if (randomSoupCursor.moveToFirst()) {
            // Get the soup name and its composition
            val soupNameIndex = randomSoupCursor.getColumnIndex("name")
            val soupCompositionIndex = randomSoupCursor.getColumnIndex("composition")
            var soupName = ""
            var soupComposition = ""
            if (soupNameIndex != -1 && soupCompositionIndex != -1) {
                soupName = randomSoupCursor.getString(soupNameIndex)
                soupComposition = randomSoupCursor.getString(soupCompositionIndex)
            }

            // Add the used soup name to the set
            usedSoupNames.add(soupName)

            // Close the cursor after use
            randomSoupCursor.close()

            // Set the soup name in TextView
            textViewTest.text = soupName

            // Create a list to store answer options (soup compositions)
            val answerOptions = mutableListOf(soupComposition.trim())

            // Check if "composition" is present in the query results
            if (soupCompositionIndex != -1) {
                // Populate the list with incorrect answer options
                val wrongAnswersQuery =
                    "SELECT composition FROM fits WHERE name != ? ORDER BY RANDOM() LIMIT 3"
                val wrongAnswersCursor = db.rawQuery(wrongAnswersQuery, arrayOf(soupName))
                while (wrongAnswersCursor.moveToNext()) {
                    val compositionIndex = wrongAnswersCursor.getColumnIndex("composition")
                    if (compositionIndex != -1) {
                        answerOptions.add(wrongAnswersCursor.getString(compositionIndex).trim())
                    }
                }
                wrongAnswersCursor.close()

                // Shuffle the list of answer options
                answerOptions.shuffle()

                // Set the text in each RadioButton
                radioButton1.text = answerOptions[0]
                radioButton2.text = answerOptions[1]
                radioButton3.text = answerOptions[2]
                radioButton4.text = answerOptions[3]
            } else {
                // Log an error or handle the missing column case
                Log.e("TestActivity", "Column 'composition' not found in the cursor.")
            }
        } else {
            // Log an error or handle the case when there are no soups in the database
            Log.e("TestActivity", "No soups found in the database.")
        }
    }

    private fun checkAnswer() {
        val selectedRadioButtonId = radioGroup.checkedRadioButtonId
        if (selectedRadioButtonId != -1) {
            val selectedRadioButton = findViewById<RadioButton>(selectedRadioButtonId)
            val selectedAnswer = selectedRadioButton.text.toString()

            // Disable all RadioButtons
            setRadioButtonsEnabled(false)

            // Get the name from TextView
            val questionName = textViewTest.text.toString()

            // Query the database to get the correct answer based on the question name
            val query = "SELECT composition FROM fits WHERE name = ?"
            val cursor = db.rawQuery(query, arrayOf(questionName))

            if (cursor.moveToFirst()) {
                val correctAnswerIndex = cursor.getColumnIndex("composition")
                var correctAnswer = ""
                // If the query returns a result, get the value from the "composition" column
                if (correctAnswerIndex != -1) {
                    correctAnswer = cursor.getString(correctAnswerIndex)
                }

                // Compare the selected answer with the correct answer
                if (selectedAnswer == correctAnswer) {
                    correctAnswersCount++
                    selectedRadioButton.setBackgroundColor(Color.rgb(0,160,0)) // Set green background
                    selectedRadioButton.setTextColor(Color.WHITE)

                } else {
                    selectedRadioButton.setBackgroundColor(Color.rgb(160,0,0)) // Set red background
                    selectedRadioButton.setTextColor(Color.WHITE)
                }
            }

            // Close the cursor after use
            cursor.close()
        }
    }

    private fun setRadioButtonsEnabled(enabled: Boolean) {
        radioButton1.isEnabled = enabled
        radioButton2.isEnabled = enabled
        radioButton3.isEnabled = enabled
        radioButton4.isEnabled = enabled
    }

    private fun resetRadioButtons() {
        radioButton1.setBackgroundColor(Color.TRANSPARENT)
        radioButton2.setBackgroundColor(Color.TRANSPARENT)
        radioButton3.setBackgroundColor(Color.TRANSPARENT)
        radioButton4.setBackgroundColor(Color.TRANSPARENT)
        val defaultTextColor = ContextCompat.getColor(this, R.color.textColor)
        radioButton1.setTextColor(defaultTextColor)
        radioButton2.setTextColor(defaultTextColor)
        radioButton3.setTextColor(defaultTextColor)
        radioButton4.setTextColor(defaultTextColor)
    }

    private fun showResult() {
        Toast.makeText(this, "Тест завершен. Правильных ответов: $correctAnswersCount", Toast.LENGTH_SHORT).show()
        cursor.close()


        if (correctAnswersCount == cursor.count) {
            val currentDate = LocalDateTime.now().toString()
            val userId = getUserIdByLogin(login)

            if (userId != null) {
                val db = DBHelper(this, null)
                val values = ContentValues().apply {
                    put("userID", userId)
                    put("endingFitTest", currentDate)
                }

                val userExists = db.checkUserExistence(userId)
                if (userExists) {
                    db.writableDatabase.update("result", values, "userID = ?", arrayOf(userId.toString()))
                    Log.e("E", "Updating Table userId - $userId, ${currentDate}")
                } else {
                    db.writableDatabase.insert("result", null, values)
                    Log.e("E", "Creating new Table userId - $userId, ${currentDate}")
                }
                db.close()
            } else {
                Log.e("showResult", "Failed to retrieve user ID for login: ${MainActivity.login}")
            }
        } else {
            return
        }
        val intent = Intent(this, MainMenu::class.java)
        startActivity(intent)
        db.close()
        finish()
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
