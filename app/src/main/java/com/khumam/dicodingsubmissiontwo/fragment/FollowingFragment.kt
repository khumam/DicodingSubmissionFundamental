package com.khumam.dicodingsubmissiontwo.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.khumam.dicodingsubmissiontwo.ViewBindingHolder
import com.khumam.dicodingsubmissiontwo.ViewBindingHolderImpl
import com.khumam.dicodingsubmissiontwo.activity.DetailUserActivity
import com.khumam.dicodingsubmissiontwo.adapter.FollowingAdapter
import com.khumam.dicodingsubmissiontwo.data.User
import com.khumam.dicodingsubmissiontwo.databinding.FragmentFollowingBinding
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject

class FollowingFragment : Fragment(), ViewBindingHolder<FragmentFollowingBinding> by ViewBindingHolderImpl() {

    private var listFollowing: ArrayList<User> = ArrayList()
    private lateinit var adapter: FollowingAdapter
    private var token: String = "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"

    companion object {
        const val USERNAME = "username"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View = initBinding(FragmentFollowingBinding.inflate(inflater), this) {}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = FollowingAdapter(listFollowing)
        binding?.rvFollowing?.setHasFixedSize(true)
        listFollowing.clear()
        val dataUser = activity?.intent?.getStringExtra(USERNAME)
        getFollowers(dataUser.toString())
    }

    private fun showRecyclerList() {
        binding?.rvFollowing?.layoutManager = LinearLayoutManager(activity)
        val followingAdapter = FollowingAdapter(listFollowing)
        binding?.rvFollowing?.adapter = followingAdapter

        followingAdapter.setOnItemClickCallback(object : FollowingAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                showSelectedUser(data)
            }
        })
    }

    private fun getFollowers(id: String) {
        binding?.progressBarFollowing?.visibility = View.VISIBLE
        val client = AsyncHttpClient()
        client.addHeader("User-Agent", "request")
        client.addHeader("Authorization", "token $token")
        val source = "https://api.github.com/users/$id/following"
        client.get(source, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                binding?.progressBarFollowing?.visibility = View.INVISIBLE
                val result = String(responseBody)
                try {
                    val resultArray = JSONArray(result)
                    for (index in 0 until resultArray.length()) {
                        val resultObject = resultArray.getJSONObject(index)
                        val userName: String = resultObject.getString("login")
                        getDetailUser(userName)
                    }
                } catch (e: Exception) {
                    Toast.makeText(activity, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                binding?.progressBarFollowing?.visibility = View.INVISIBLE
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getDetailUser(id: String) {
        binding?.progressBarFollowing?.visibility = View.VISIBLE
        val client = AsyncHttpClient()
        client.addHeader("User-Agent", "request")
        client.addHeader("Authorization", "token $token")
        val source = "https://api.github.com/users/$id"

        client.get(source, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                binding?.progressBarFollowing?.visibility = View.INVISIBLE
                val result = String(responseBody)

                try {
                    val resultObject = JSONObject(result)
                    val username: String? = resultObject.getString("login").toString()
                    val name: String? = resultObject.getString("name").toString()
                    val avatar: String? = resultObject.getString("avatar_url").toString()
                    val company: String? = resultObject.getString("company").toString()
                    val location: String? = resultObject.getString("location").toString()
                    val repository: String? = resultObject.getString("public_repos")
                    val followers: String? = resultObject.getString("followers")
                    val following: String? = resultObject.getString("following")

                    listFollowing.add(
                            User(
                                    username,
                                    name,
                                    location,
                                    repository,
                                    company,
                                    followers,
                                    following,
                                    avatar,
                            )
                    )

                    showRecyclerList()
                } catch (e: Exception) {
                    Toast.makeText(activity, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                binding?.progressBarFollowing?.visibility = View.INVISIBLE
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show()
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
}