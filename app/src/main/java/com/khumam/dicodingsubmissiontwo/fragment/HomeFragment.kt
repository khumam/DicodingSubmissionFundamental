package com.khumam.dicodingsubmissiontwo.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
//import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.khumam.dicodingsubmissiontwo.viewModel.MyViewModel
import com.khumam.dicodingsubmissiontwo.ViewBindingHolder
import com.khumam.dicodingsubmissiontwo.ViewBindingHolderImpl
import com.khumam.dicodingsubmissiontwo.activity.DetailUserActivity
import com.khumam.dicodingsubmissiontwo.adapter.userAdapter
import com.khumam.dicodingsubmissiontwo.data.User
import com.khumam.dicodingsubmissiontwo.databinding.FragmentHomeBinding

class HomeFragment : Fragment(), ViewBindingHolder<FragmentHomeBinding> by ViewBindingHolderImpl() {

    private var list = listOf<User>()
//    private var listInit = arrayListOf<User>()
    private lateinit var adapter: userAdapter
    private var token: String = "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"
    private val viewModel: MyViewModel by activityViewModels()

    companion object {
        private val TAG = HomeFragment::class.java.simpleName
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = userAdapter(list)
        binding?.rvHome?.setHasFixedSize(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? = initBinding(FragmentHomeBinding.inflate(inflater), this) {
        viewModel.getUserLists()
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
    }

    private fun showSelectedUser(user: User) {
        val userdetail = Intent(activity, DetailUserActivity::class.java)
        userdetail.putExtra(DetailUserActivity.USERNAME, user.username)
        userdetail.putExtra(DetailUserActivity.NAME, user.name)
        userdetail.putExtra(DetailUserActivity.AVATAR, user.avatar)
        startActivity(userdetail)
    }

//    private fun initUserList() {
//        binding?.progressBarHome?.visibility = View.VISIBLE
//        val client = AsyncHttpClient()
//        client.addHeader("User-Agent", "request")
//        client.addHeader("Authorization", "token $token")
//        val source = "https://api.github.com/search/users?q=alex"
//        client.get(source, object : AsyncHttpResponseHandler() {
//            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
//                binding?.progressBarHome?.visibility = View.INVISIBLE
//                val result = String(responseBody)
//                Log.d(TAG, result)
//                try {
//                    val resultArray = JSONObject(result)
//                    val item = resultArray.getJSONArray("items")
//                    if (item.length() == 0) {
//                        Toast.makeText(activity, resources.getString(R.string.not_found) , Toast.LENGTH_SHORT).show()
//                    }
//                    for (index in 0 until item.length()) {
//                        val resultObject = item.getJSONObject(index)
//                        val userName: String = resultObject.getString("login")
//                        if (userName != null) {
//                            getDetailUser(userName)
//                        }
//                    }
//                } catch (e: Exception) {
//                    Toast.makeText(activity, e.message, Toast.LENGTH_SHORT).show()
//                    e.printStackTrace()
//                }
//            }
//            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
//                binding?.progressBarHome?.visibility = View.INVISIBLE
//                val errorMessage = when (statusCode) {
//                    401 -> "$statusCode : Bad Request"
//                    403 -> "$statusCode : Forbidden"
//                    404 -> "$statusCode : Not Found"
//                    else -> "$statusCode : ${error.message}"
//                }
//                Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show()
//            }
//        })
//    }
//
//    private fun getDetailUser(id: String) {
//        binding?.progressBarHome?.visibility = View.VISIBLE
//        val client = AsyncHttpClient()
//        client.addHeader("User-Agent", "request")
//        client.addHeader("Authorization", "token $token")
//        val source = "https://api.github.com/users/$id"
//
//        client.get(source, object : AsyncHttpResponseHandler() {
//            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
//                binding?.progressBarHome?.visibility = View.INVISIBLE
//                val result = String(responseBody)
//
//                try {
//                    val resultObject = JSONObject(result)
//                    val username: String? = resultObject.getString("login").toString()
//                    val name: String? = resultObject.getString("name").toString()
//                    val avatar: String? = resultObject.getString("avatar_url").toString()
//                    val company: String? = resultObject.getString("company").toString()
//                    val location: String? = resultObject.getString("location").toString()
//                    val repository: String? = resultObject.getString("public_repos")
//                    val followers: String? = resultObject.getString("followers")
//                    val following: String? = resultObject.getString("following")
//
//                    if (name != "null") {
//                        listInit.add(
//                                User(
//                                        username,
//                                        name,
//                                        location,
//                                        repository,
//                                        company,
//                                        followers,
//                                        following,
//                                        avatar,
//                                )
//                        )
//
//                        showRecyclerList()
//                    }
//                } catch (e: Exception) {
//                    Toast.makeText(activity, e.message, Toast.LENGTH_SHORT).show()
//                    e.printStackTrace()
//                }
//            }
//            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
//                binding?.progressBarHome?.visibility = View.INVISIBLE
//                val errorMessage = when (statusCode) {
//                    401 -> "$statusCode : Bad Request"
//                    403 -> "$statusCode : Forbidden"
//                    404 -> "$statusCode : Not Found"
//                    else -> "$statusCode : ${error.message}"
//                }
//                Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show()
//            }
//        })
//    }
}