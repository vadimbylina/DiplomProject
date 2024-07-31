package com.example.esedu

import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

class UserChange : AppCompatActivity() {
    private lateinit var userSpinner: Spinner
    private lateinit var db: DBHelper
    private lateinit var loginText: EditText
    private lateinit var passwordText: EditText
    private lateinit var saveButton: Button
    private lateinit var deleteButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_change)
        db = DBHelper(this, null)
        userSpinner = findViewById(R.id.UserSpinner)
        loginText = findViewById(R.id.loginText)
        passwordText = findViewById(R.id.passwordText)
        saveButton = findViewById(R.id.saveButton)
        deleteButton = findViewById(R.id.deleteButton)
        populateUserSpinner()
        setUserSpinnerListener()
        setButtonListeners()
    }

    private fun populateUserSpinner() {
        val users: ArrayList<String> = ArrayList()
        val cursor: Cursor = db.readableDatabase.rawQuery("SELECT login FROM users", null)

        while (cursor.moveToNext()) {
            val loginIndex = cursor.getColumnIndex("login")
            if (loginIndex != -1) {
                val login = cursor.getString(loginIndex)
                users.add(login)
            }
        }
        cursor.close()

        val adapter = ArrayAdapter(this, R.layout.custom_spinner_item, users)
        adapter.setDropDownViewResource(R.layout.custom_spinner_item)
        userSpinner.adapter = adapter
    }

    private fun setUserSpinnerListener() {
        userSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: android.view.View?, position: Int, id: Long) {
                val selectedLogin = parent.getItemAtPosition(position) as String
                updateLoginAndPasswordFields(selectedLogin)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }
    }

    private fun updateLoginAndPasswordFields(login: String) {
        val cursor: Cursor = db.readableDatabase.rawQuery("SELECT login, pass FROM users WHERE login = ?", arrayOf(login))

        if (cursor.moveToFirst()) {
            val loginIndex = cursor.getColumnIndex("login")
            val passIndex = cursor.getColumnIndex("pass")
            if (loginIndex != -1 && passIndex != -1) {
                val userLogin = cursor.getString(loginIndex)
                val userPass = cursor.getString(passIndex)
                loginText.setText(userLogin)
                passwordText.setText(userPass)
            }
        }
        cursor.close()
    }

    private fun setButtonListeners() {
        saveButton.setOnClickListener {
            val selectedLogin = userSpinner.selectedItem as String
            val newLogin = loginText.text.toString()
            val newPassword = passwordText.text.toString()

            if (newLogin.isNotEmpty() && newPassword.isNotEmpty()) {
                val dbWritable = db.writableDatabase
                val query = "UPDATE users SET login = ?, pass = ? WHERE login = ?"
                val args = arrayOf(newLogin, newPassword, selectedLogin)
                dbWritable.execSQL(query, args)
                Toast.makeText(this, "User updated successfully", Toast.LENGTH_SHORT).show()
                populateUserSpinner()
            } else {
                Toast.makeText(this, "Login and password cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }

        deleteButton.setOnClickListener {
            val selectedLogin = userSpinner.selectedItem as String
            showDeleteConfirmationDialog(selectedLogin)
        }
    }

    private fun showDeleteConfirmationDialog(login: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Confirm Deletion")
        builder.setMessage("Вы точно хотите удалить пользователя $login?")
        builder.setPositiveButton("Да") { dialog, which ->
            deleteUser(login)
        }
        builder.setNegativeButton("Нет") { dialog, which ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun deleteUser(login: String) {
        val dbWritable = db.writableDatabase
        val query = "DELETE FROM users WHERE login = ?"
        val args = arrayOf(login)
        dbWritable.execSQL(query, args)
        Toast.makeText(this, "User deleted successfully", Toast.LENGTH_SHORT).show()
        loginText.text.clear()
        passwordText.text.clear()
        populateUserSpinner()
    }
}
