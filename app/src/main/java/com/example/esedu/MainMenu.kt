package com.example.esedu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class MainMenu : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu_tmp)
        val x = DBHelper(this, null)

        val tests: Button = findViewById(R.id.teststart)
        val menu: Button = findViewById(R.id.menu)
        val adminMenu: Button = findViewById(R.id.adminmenu)
        adminMenu.visibility = View.INVISIBLE
        if(isAdmin())
        {
            adminMenu.visibility = View.VISIBLE
        }


        tests.setOnClickListener {
            val intent = Intent(this, Сomp::class.java)
            startActivity(intent)
        }

        menu.setOnClickListener {
            val intent = Intent(this, menumenuActivity::class.java)
            startActivity(intent)
        }
        adminMenu.setOnClickListener {

            val intent = Intent(this, AdminMenu::class.java)
            startActivity(intent)
        }
    }
    private fun isAdmin(): Boolean {
        val x = intent.getStringExtra("Admin")
        if (x == "1")
        {
            return true
        }
        return false
    }

}
