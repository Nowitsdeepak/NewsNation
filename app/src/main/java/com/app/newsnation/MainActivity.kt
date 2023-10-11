package com.app.newsnation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.app.newsnation.ui.BookmarkFragment
import com.app.newsnation.ui.MainFragment
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<MaterialToolbar>(R.id.materialToolbar)
        val bottomNavView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        setSupportActionBar(toolbar)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment

        navController = navHostFragment.navController

        setupActionBarWithNavController(navController)

        bottomNavView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menuHome -> {
                    toolbar.title = "News Nation"
                    loadFragment(MainFragment())
                    item.icon = ResourcesCompat.getDrawable(resources, R.drawable.homedark, null)
                    return@setOnItemSelectedListener true
                }
                R.id.menuBookmark -> {
                    toolbar.title = "Bookmarks"
                    loadFragment(BookmarkFragment())
                    return@setOnItemSelectedListener true
                }
            }
            false
        }

    }

    private fun loadFragment(fragment: Fragment) {
        // load fragment
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainerView, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
                || super.onSupportNavigateUp()
    }
}
