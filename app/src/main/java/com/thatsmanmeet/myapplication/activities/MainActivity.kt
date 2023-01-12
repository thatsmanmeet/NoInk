package com.thatsmanmeet.myapplication.activities

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.text.Html
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.Fragment
import com.thatsmanmeet.myapplication.R
import com.thatsmanmeet.myapplication.databinding.ActivityMainBinding
import com.thatsmanmeet.myapplication.fragments.NotesFragment
import com.thatsmanmeet.myapplication.fragments.SettingsFragment
import com.thatsmanmeet.myapplication.fragments.TodoFragment
import com.thatsmanmeet.myapplication.fragments.TrashFragment


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val notesFragment = NotesFragment()
    private val todoFragment = TodoFragment()
    private val trashFragment = TrashFragment()
    private val settingsFragment = SettingsFragment()
    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setActionBar()
        window.navigationBarColor = getColor(R.color.black)
        setCurrentFragment(todoFragment)
        binding.bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.miNote -> {
                    setCurrentFragment(notesFragment)
                }
                R.id.miTodo -> {
                    setCurrentFragment(todoFragment)
                }
                R.id.miTrash -> {
                    setCurrentFragment(trashFragment)
                }
                R.id.miSettings -> {
                    setCurrentFragment(settingsFragment)
                }
            }
            true
        }
    }

    private fun setActionBar() {
        supportActionBar!!.title =
            Html.fromHtml("<font color=\"#FFFFFF\">" + "No Ink" + "</font>", 0)
        if (isDarkTheme()) {
            supportActionBar!!.setBackgroundDrawable(
                ResourcesCompat.getDrawable(
                    resources,
                    R.color.grey,
                    null
                )
            )
        } else {
            supportActionBar!!.setBackgroundDrawable(
                ResourcesCompat.getDrawable(
                    resources,
                    R.color.blue,
                    null
                )
            )
        }
    }

    private fun setCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            this.replace(R.id.frameContainer, fragment)
            if (supportFragmentManager.fragments.isNotEmpty()) {
                this.addToBackStack(null)
            }
            this.commit()
        }
        supportFragmentManager.addOnBackStackChangedListener {
            // find the current fragment and check the string
            val currentFragment = supportFragmentManager.findFragmentById(R.id.frameContainer).toString()
            if (currentFragment.contains("TodoFragment")) {
                binding.bottomNav.menu.getItem(0).isChecked = true
            } else if (currentFragment.contains("NotesFragment")) {
                binding.bottomNav.menu.getItem(1).isChecked = true
            } else if (currentFragment.contains("TrashFragment")) {
                binding.bottomNav.menu.getItem(2).isChecked = true
            } else if(currentFragment.contains("SettingsFragment")){
                binding.bottomNav.menu.getItem(3).isChecked = true
            }
        }
    }

    private fun isDarkTheme(): Boolean {
        return when (this.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_YES -> true
            Configuration.UI_MODE_NIGHT_NO -> false
            else -> false
        }
    }

    @SuppressLint("ResourceType")
    override fun onConfigurationChanged(newConfig: Configuration) {
        if (isDarkTheme()) {
            window.statusBarColor = getColor(R.color.grey)
            window.navigationBarColor = getColor(R.color.black)
            supportActionBar!!.setBackgroundDrawable(
                ResourcesCompat.getDrawable(
                    resources,
                    R.color.grey,
                    null
                )
            )
            binding.parent.setBackgroundColor(
                ResourcesCompat.getColor(
                    resources,
                    R.color.black_bg,
                    null
                )
            )
        } else {
            window.statusBarColor = getColor(R.color.blue)
            window.navigationBarColor = getColor(R.color.black)
            supportActionBar!!.setBackgroundDrawable(
                ResourcesCompat.getDrawable(
                    resources,
                    R.color.blue,
                    null
                )
            )
            binding.parent.setBackgroundColor(
                ResourcesCompat.getColor(
                    resources,
                    R.color.white,
                    null
                )
            )
        }
        super.onConfigurationChanged(newConfig)
    }
}