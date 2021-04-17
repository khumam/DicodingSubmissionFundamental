package com.khumam.dicodingsubmissiontwo.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.khumam.dicodingsubmissiontwo.R
import com.khumam.dicodingsubmissiontwo.ViewBindingHolder
import com.khumam.dicodingsubmissiontwo.ViewBindingHolderImpl
import com.khumam.dicodingsubmissiontwo.data.User
import com.khumam.dicodingsubmissiontwo.data.UserDetail
import com.khumam.dicodingsubmissiontwo.databinding.FragmentUserinfoBinding
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class UserinfoFragment : Fragment(), ViewBindingHolder<FragmentUserinfoBinding> by ViewBindingHolderImpl() {

    private var token: String = "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"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? = initBinding(FragmentUserinfoBinding.inflate(inflater), this) {}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dataFromIntent = activity?.intent?.getStringExtra(FollowerFragment.USERNAME)
        getDetailUser(dataFromIntent.toString())
    }

    private fun getDetailUser(id: String) {
        binding?.progressBarUserInfo?.visibility = View.VISIBLE
        val client = AsyncHttpClient()
        client.addHeader("User-Agent", "request")
        client.addHeader("Authorization", "token $token")
        val source = "https://api.github.com/users/$id"

        client.get(source, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                binding?.progressBarUserInfo?.visibility = View.INVISIBLE
                val result = String(responseBody)

                try {
                    val resultObject    = JSONObject(result)
                    val companyResult       = resultObject.getString("company")
                    val locationResult      = resultObject.getString("location")
                    val repositoryResult    = resultObject.getString("public_repos")
                    val followersResult     = resultObject.getString("followers")
                    val followingResult     = resultObject.getString("following")

                    binding?.idFollowers?.text = followersResult ?: resources.getString(R.string.not_found)
                    binding?.idFollowing?.text = followingResult ?: resources.getString(R.string.not_found)
                    binding?.idRepository?.text = repositoryResult ?: resources.getString(R.string.not_found)
                    binding?.idCompany?.text = companyResult ?: resources.getString(R.string.not_found)
                    binding?.idLocation?.text = locationResult ?: resources.getString(R.string.not_found)

                    // Tipe null dari company dan location menghindari kata null dalam bentuk string
                    if (companyResult != "null") binding?.idCompany?.text = companyResult else binding?.idCompany?.text = resources.getString(R.string.not_found)
                    if (locationResult != "null") binding?.idLocation?.text = locationResult else binding?.idLocation?.text = resources.getString(R.string.not_found)
                } catch (e: Exception) {
                    Toast.makeText(activity, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }
            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                binding?.progressBarUserInfo?.visibility = View.INVISIBLE
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

}