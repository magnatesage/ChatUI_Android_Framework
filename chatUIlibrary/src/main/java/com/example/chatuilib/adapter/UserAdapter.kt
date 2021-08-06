package com.example.chatuilib.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.chatuilib.R
import com.example.chatuilib.customviews.CustomShapeableImageView
import com.example.chatuilib.customviews.CustomTextView

class UserAdapter(val context: Context, private val userList: ArrayList<String>) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    class UserViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
            val ivUser = itemView.findViewById<CustomShapeableImageView>(R.id.iv_user) as CustomShapeableImageView
            val tvUserName = itemView.findViewById<CustomTextView>(R.id.tv_user_name) as CustomTextView
            val tvUserDesignation = itemView.findViewById<CustomTextView>(R.id.tv_user_designation) as CustomTextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.lib_item_user,parent,false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.tvUserName.text = userList[position]
        holder.tvUserDesignation.text = context.getString(R.string.lib_agent_1)
    }

    override fun getItemCount(): Int = userList.size
}