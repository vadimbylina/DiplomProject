package com.example.esedu

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UserAdapter (var users: List<User>, var context:Context) : RecyclerView.Adapter<UserAdapter.MyViewHolder>()
{
       class MyViewHolder(view:View) : RecyclerView.ViewHolder(view)
       {
           var login: TextView = view.findViewById(R.id.userlistlogin)
           var pass: TextView = view.findViewById(R.id.userlistpass)
       }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_user_in_list, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
    return users.count()
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.login.text = users[position].login
        holder.pass.text = users[position].pass

    }


}