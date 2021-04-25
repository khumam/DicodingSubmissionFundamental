package com.khumam.githubuserconsumer.activity

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.database.ContentObserver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.khumam.githubuserconsumer.viewModel.MyViewModel
import com.khumam.githubuserconsumer.fragment.HomeFragment
import com.khumam.githubuserconsumer.R
import com.khumam.githubuserconsumer.adapter.SectionHomeAdapter
import com.khumam.githubuserconsumer.adapter.userAdapter
import com.khumam.githubuserconsumer.contract.DatabaseContract
import com.khumam.githubuserconsumer.data.User
import com.khumam.githubuserconsumer.databinding.ActivityMainBinding
import com.khumam.githubuserconsumer.databinding.FragmentHomeBinding

class MainActivity : AppCompatActivity() {
    private lateinit var rvUser: RecyclerView
    private var list: ArrayList<User> = arrayListOf()
    private lateinit var binding: ActivityMainBinding
    private lateinit var fragmentHomeBinding: FragmentHomeBinding
    private lateinit var homeFragment: HomeFragment
    private val viewModel: MyViewModel by viewModels()

    companion object {
        private val TAG = MainActivity::class.java.simpleName
        private val TAB_TITLES = intArrayOf(
                R.string.home,
                R.string.favorite,
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        fragmentHomeBinding = FragmentHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getUserLists()

        val sectionHomeAdapter  = SectionHomeAdapter(this@MainActivity)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager_home)
        viewPager.adapter         = sectionHomeAdapter
        val tabs: TabLayout = findViewById(R.id.tabs_home)

        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.elevation = 0f
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        setMode(item.itemId)
        return super.onOptionsItemSelected(item)
    }

    private fun setMode(selectedMode: Int) {
        when (selectedMode) {
//            R.id.action_about -> {
//                val moveToAbout = Intent(this@MainActivity, AboutActivity::class.java)
//                startActivity(moveToAbout)
//            }
            R.id.action_locale -> {
                val moveToLocale = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(moveToLocale)
            }
//            R.id.action_setting -> {
//                val moveToSetting = Intent(this@MainActivity, SettingActivity::class.java)
//                startActivity(moveToSetting)
//            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.clearFocus()
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (!query.isEmpty()) {
                      viewModel.searchUser(query)
                }
                return true
            }
            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        searchView.setOnCloseListener (object : SearchView.OnCloseListener {
            override fun onClose(): Boolean {
                viewModel.getUserLists()
                Log.d("CLOSE", "CLOSED")
                return true
            }
        })
        return super.onCreateOptionsMenu(menu)
    }
}