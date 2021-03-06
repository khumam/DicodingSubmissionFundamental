package com.khumam.githubuserconsumer.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.khumam.githubuserconsumer.R
import com.khumam.githubuserconsumer.data.User


class FollowerAdapter (private val listUserFollowers: ArrayList<User>) : RecyclerView.Adapter<FollowerAdapter.ListViewHolder>() {

    private var onItemClickCallback: OnItemClickCallback? = null

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context).inflate(R.layout.list_users, viewGroup, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val user = listUserFollowers[position]
        Glide.with(holder.itemView.context)
                .load(user.avatar)
                .apply(RequestOptions().override(55, 55))
                .into(holder.avatar)
        holder.name.text = user.name
        holder.username.text = user.username
        holder.itemView.setOnClickListener { onItemClickCallback?.onItemClicked(listUserFollowers[holder.adapterPosition]) }
    }

    override fun getItemCount(): Int {
        return listUserFollowers.size
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: User)
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView = itemView.findViewById(R.id.id_name_rv)
        var username: TextView = itemView.findViewById(R.id.id_username_rv)
        var avatar: ImageView = itemView.findViewById(R.id.id_avatar_rv)
    }
}