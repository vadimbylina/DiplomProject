package com.example.esedu
import java.sql.Connection
import java.sql.DriverManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.esedu.DBHelper
import com.jakewharton.threetenabp.AndroidThreeTen
import java.sql.ResultSet
import java.sql.Statement


class MainActivity : AppCompatActivity() {
    companion object {
        var login: String? = null
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidThreeTen.init(this)


        setContentView(R.layout.activity_main)
        val userLogin: EditText = findViewById(R.id.userLoginAuth)
        val userPass:EditText = findViewById(R.id.userPassAuth)
        val button: Button = findViewById(R.id.buttonAuth)
        val linkToRegistration: TextView = findViewById(R.id.linkToRegistration)
        val brow: TextView = findViewById(R.id.Browser)
        val x = DBHelper(this,null)
        brow.setOnClickListener {
            x.dropTables()
            x.createTables()
            x.insertTables()
        }
        linkToRegistration.setOnClickListener {
            val intent = Intent(this, Registration::class.java)
            startActivity(intent)
        }


        button.setOnClickListener{
            val pass = userPass.text.toString().trim()
            login = userLogin.text.toString().trim()

            val db = DBHelper(this,null)
            val isAuth = db.getUser(login!!,pass)
            if(login == "" || pass == "")
            {
                Toast.makeText(this, "Строка не заполнена", Toast.LENGTH_SHORT).show()
            }
            else
            {
                if(isAuth)
                {
                val intent = Intent(this,MainMenu::class.java)
                startActivity(intent)
                }
                else if (login == "admin" && pass == "root")
                {
                    val intent = Intent(this,MainMenu::class.java)
                    intent.putExtra("Admin", "1")
                    startActivity(intent)
                }
                else
                {
                    Toast.makeText(this, "Вход не выполнен", Toast.LENGTH_SHORT).show()
                }
            }
        }

        }


}