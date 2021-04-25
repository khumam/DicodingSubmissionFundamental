package com.khumam.githubuserconsumer.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.khumam.githubuserconsumer.data.User
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class MyViewModel: ViewModel() {
    private var _listUser = MutableLiveData<List<User>>()
    var listUser: LiveData<List<User>> = _listUser
    var list = arrayListOf<User>()
    private var token: String = "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"

    fun searchUser(username: String) {
        val client = AsyncHttpClient()
        client.addHeader("User-Agent", "request")
        client.addHeader("Authorization", "token $token")
        val source = "https://api.github.com/search/users?q=$username"
        client.get(source, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                val result = String(responseBody)
                list.clear()
                _listUser.postValue(list)
                try {
                    val resultArray = JSONObject(result)
                    val item = resultArray.getJSONArray("items")
                    for (index in 0 until item.length()) {
                        val resultObject = item.getJSONObject(index)
                        val userName: String = resultObject.getString("login")
                        getDetailUser(userName)
                    }
                } catch (e: Exception) {
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
            }
        })
    }

    fun getUserLists() {
        val client = AsyncHttpClient()
        client.addHeader("User-Agent", "request")
        client.addHeader("Authorization", "token $token")
        val source = "https://api.github.com/search/users?q=khumam"
        client.get(source, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                val result = String(responseBody)
                list.clear()
                _listUser.postValue(list)
                try {
                    val resultArray = JSONObject(result)
                    val item = resultArray.getJSONArray("items")
                    for (index in 0 until item.length()) {
                        val resultObject = item.getJSONObject(index)
                        val userName: String = resultObject.getString("login")
                        if (userName != null) {
                            getDetailUser(userName)
                        }
                    }

                } catch (e: Exception) {
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
            }
        })
    }

    private fun getDetailUser(id: String) {
        val client = AsyncHttpClient()
        client.addHeader("User-Agent", "request")
        client.addHeader("Authorization", "token $token")
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

                    if (name != "null" && name != null) {
                        list.add(
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

                        _listUser.postValue(list)
                    }
                } catch (e: Exception) {
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
            }
        })
    }
}