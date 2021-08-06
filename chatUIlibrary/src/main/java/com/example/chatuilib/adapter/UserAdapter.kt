package com.example.chatuilib.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.chatuilib.R
import com.example.chatuilib.customviews.CustomShapeableImageView
import com.example.chatuilib.customviews.CustomTextView
import com.example.chatuilib.model.UserModel
import com.example.chatuilib.utils.Utils

class UserAdapter(val context: Context, private val userList: ArrayList<UserModel>) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    class UserViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
            val ivUser = itemView.findViewById(R.id.iv_user) as CustomShapeableImageView
            val tvUserName = itemView.findViewById(R.id.tv_user_name) as CustomTextView
            val tvUserRole = itemView.findViewById(R.id.tv_user_role) as CustomTextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.lib_item_user,parent,false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val userModel = userList[position]
        holder.tvUserName.text = userModel.userName
        holder.tvUserRole.text = userModel.userRole
        Utils.loadImageWithExecutor(
            userModel.userUrl,
            holder.ivUser,
            R.drawable.lib_ic_user
        )
    }

    override fun getItemCount(): Int = userList.size
}