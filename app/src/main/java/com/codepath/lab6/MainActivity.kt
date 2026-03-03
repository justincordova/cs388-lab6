package com.codepath.lab6

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.codepath.lab6.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.serialization.json.Json

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var parksFragment: ParksFragment? = null
    private var campgroundFragment: CampgroundFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set up ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Initialize fragments
        parksFragment = ParksFragment.newInstance()
        campgroundFragment = CampgroundFragment.newInstance()

        // Load default fragment immediately
        replaceFragment(parksFragment!!)

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)

        // handle navigation selection
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_parks -> {
                    replaceFragment(parksFragment!!)
                    true
                }
                R.id.nav_campgrounds -> {
                    replaceFragment(campgroundFragment!!)
                    true
                }
                else -> false
            }
        }

        // Set default selection (triggers listener but fragment already loaded)
        bottomNavigationView.selectedItemId = R.id.nav_parks
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.main_frame_layout, fragment)
        fragmentTransaction.commit()
    }
}

fun createJson() = Json {
    isLenient = true
    ignoreUnknownKeys = true
    useAlternativeNames = false
}
