package com.example.esedu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.esedu.DBHelper

class Registration : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        val userLogin: EditText = findViewById(R.id.userLogin)
        val userPass: EditText = findViewById(R.id.userPass)
        val button: Button = findViewById(R.id.buttonReg)

        button.setOnClickListener {
            val login = userLogin.text.toString().trim()
            val pass = userPass.text.toString().trim()
            val db = DBHelper(this,null)
            if(login == "admin" && pass == "root")
            {
                Toast.makeText(this, "Неверные данные", Toast.LENGTH_SHORT).show()
            }
            else {
                if (login == "" || pass == "") {
                    Toast.makeText(this, "Не все строки заполнены", Toast.LENGTH_SHORT).show()
                } else {
                    if(db.isUsernameAvailable(login))
                    {
                        val user = User(login, pass)

                        db.addUser(user)
                        Toast.makeText(this, "Работник $login добавлен", Toast.LENGTH_SHORT).show()
                        userLogin.text.clear()
                        userPass.text.clear()
                    }
                    else
                    {
                        Toast.makeText(this,"Имя пользователя уже занято", Toast.LENGTH_SHORT).show()
                    }


                }
            }
        }
    }
}