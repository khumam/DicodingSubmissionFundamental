package com.khumam.githubuserconsumer.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
//import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.khumam.githubuserconsumer.viewModel.MyViewModel
import com.khumam.githubuserconsumer.helper.ViewBindingHolder
import com.khumam.githubuserconsumer.helper.ViewBindingHolderImpl
import com.khumam.githubuserconsumer.activity.DetailUserActivity
import com.khumam.githubuserconsumer.adapter.userAdapter
import com.khumam.githubuserconsumer.data.User
import com.khumam.githubuserconsumer.databinding.FragmentHomeBinding

class HomeFragment : Fragment(), ViewBindingHolder<FragmentHomeBinding> by ViewBindingHolderImpl() {

    private var list = listOf<User>()
    private val viewModel: MyViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? = initBinding(FragmentHomeBinding.inflate(inflater), this) {
        binding?.progressBarHome?.visibility = View.VISIBLE
        viewModel.listUser.observe(viewLifecycleOwner, {
            list = it
            showRecyclerList()
        })
    }

    private fun showRecyclerList() {
        binding?.rvHome?.layoutManager = LinearLayoutManager(activity)
        val userAdapter = userAdapter(list)
        binding?.rvHome?.adapter = userAdapter

        userAdapter.setOnItemClickCallback(object : userAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                showSelectedUser(data)
            }
        })
        binding?.progressBarHome?.visibility = View.INVISIBLE
    }

    private fun showSelectedUser(user: User) {
        val userdetail = Intent(activity, DetailUserActivity::class.java)
        userdetail.putExtra(DetailUserActivity.USERNAME, user.username)
        userdetail.putExtra(DetailUserActivity.NAME, user.name)
        userdetail.putExtra(DetailUserActivity.AVATAR, user.avatar)
        startActivity(userdetail)
    }
}