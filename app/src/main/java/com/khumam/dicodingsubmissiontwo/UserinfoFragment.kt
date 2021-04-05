package com.khumam.dicodingsubmissiontwo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class UserinfoFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_userinfo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dataUser = activity?.intent?.getParcelableExtra<User>(FollowerFragment.DATAUSER)

        val infoCompany: TextView    = view.findViewById(R.id.id_company)
        val infoLocation: TextView   = view.findViewById(R.id.id_location)
        val infoFollowers: TextView  = view.findViewById(R.id.id_followers)
        val infoFollowing: TextView  = view.findViewById(R.id.id_following)
        val infoRepository: TextView = view.findViewById(R.id.id_repository)

        infoCompany.text    = dataUser?.company.toString() ?: "Tidak ada data"
        infoLocation.text   = dataUser?.location.toString() ?: "Tidak ada data"
        infoFollowers.text  = dataUser?.followers.toString() ?: "Tidak ada data"
        infoFollowing.text  = dataUser?.following.toString() ?: "Tidak ada data"
        infoRepository.text = dataUser?.repository.toString() ?: "Tidak ada data"
    }

}