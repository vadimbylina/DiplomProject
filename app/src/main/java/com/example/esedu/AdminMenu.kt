package com.example.esedu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class AdminMenu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_menu)
        val userListBtn: Button = findViewById(R.id.userListButton)
        val addingButton: Button = findViewById(R.id.AddingButton)
        val changeButton: Button = findViewById(R.id.changeButton)
        val resultButton: Button = findViewById(R.id.resultButton)
        val examButton:Button = findViewById(R.id.examButton)
        val regButton:Button = findViewById(R.id.registrationButton)
        val protButton:Button =findViewById(R.id.protocolButton)
        val editUser:Button = findViewById(R.id.editUserButton)

        userListBtn.setOnClickListener {
            val intent = Intent(this, UserComp::class.java)
            startActivity(intent)
        }
        addingButton.setOnClickListener {
            val intent = Intent(this, AddingMenuActivity::class.java)
            startActivity(intent)
        }
        changeButton.setOnClickListener {
            val intent = Intent(this, ChangeActivity::class.java)
            startActivity(intent)
        }
        resultButton.setOnClickListener {
            val intent = Intent(this,ResultComp::class.java)
            startActivity(intent)
        }
        examButton.setOnClickListener {
            val intent = Intent(this, ExamMenu::class.java)
            startActivity(intent)
        }
        regButton.setOnClickListener {
            val intent = Intent(this, Registration::class.java)
            startActivity(intent)
        }
        protButton.setOnClickListener {
            val intent = Intent(this,ProtocolComp::class.java)
            startActivity(intent)
        }
        editUser.setOnClickListener {
            val intent = Intent(this, UserChange::class.java)
            startActivity(intent)
        }

    }
}