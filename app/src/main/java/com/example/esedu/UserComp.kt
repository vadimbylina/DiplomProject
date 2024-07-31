package com.example.esedu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class UserComp : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_comp)
        val usersList: RecyclerView = findViewById(R.id.UsersList)
        val dbHelper = DBHelper(this, null)
        val allUsers = dbHelper.getAllUsers()

        usersList.adapter = UserAdapter(allUsers,this)
        usersList.layoutManager = LinearLayoutManager(this)
    }
}