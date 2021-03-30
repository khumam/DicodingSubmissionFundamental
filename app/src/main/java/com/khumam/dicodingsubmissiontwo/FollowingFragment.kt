package com.khumam.dicodingsubmissiontwo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.khumam.dicodingsubmissiontwo.databinding.FragmentFollowingBinding
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject

class FollowingFragment : Fragment() {

    private var listFollowing: ArrayList<User> = ArrayList()
    private var binding: FragmentFollowingBinding? = null
    private lateinit var rvFollowing: RecyclerView
    private lateinit var adapter: FollowingAdapter

    companion object {
        private val TAG = FollowingFragment::class.java.simpleName
        const val DATAUSER = "datauser"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_following, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFollowingBinding.inflate(layoutInflater)
        adapter = FollowingAdapter(listFollowing)
        rvFollowing = view.findViewById(R.id.rvFollowing)
        rvFollowing.setHasFixedSize(true)

        listFollowing.clear()
        val dataUser = activity?.intent?.getParcelableExtra<User>(DATAUSER)
        getFollowers(dataUser?.username.toString())
    }

    private fun showRecyclerList() {

//        binding?.rvFollowing?.setHasFixedSize(true)
//        binding?.rvFollowing?.layoutManager = LinearLayoutManager(activity)
        rvFollowing.layoutManager = LinearLayoutManager(activity)
        val followingAdapter = FollowingAdapter(listFollowing)
//        binding?.rvFollowing?.adapter = FollowingAdapter(listFollowing)
        rvFollowing.adapter = followingAdapter

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
        client.addHeader("Authorization", "token 11a578ce3fb594dd8c1a862036d972a53fcb0baf")
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
        val client = AsyncHttpClient()
        client.addHeader("User-Agent", "request")
        client.addHeader("Authorization", "token 11a578ce3fb594dd8c1a862036d972a53fcb0baf")
        val source = "https://api.github.com/users/$id"

        client.get(source, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
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
        val userData = User(
                user.username,
                user.name,
                user.location,
                user.repository,
                user.company,
                user.followers,
                user.following,
                user.avatar
        )
        val userdetail = Intent(activity, DetailUser::class.java)
        userdetail.putExtra(DetailUser.DATAUSER, userData)
        startActivity(userdetail)
    }
}