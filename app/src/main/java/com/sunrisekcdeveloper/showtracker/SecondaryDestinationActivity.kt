package com.sunrisekcdeveloper.showtracker

import android.os.Bundle
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.sunrisekcdeveloper.showtracker.databinding.ActivitySecondaryDestinationBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SecondaryDestinationActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySecondaryDestinationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySecondaryDestinationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        setCurrentFragment(SecondaryShareFragment())
        binding.toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_ios_new_24)
        binding.toolbar.setNavigationOnClickListener {
            toaster(33)
        }
        supportActionBar?.setDisplayShowTitleEnabled(false)
        GlobalScope.launch {
            delay(2000)
            setCurrentFragment(SecondaryTitleFragment())
            delay(2000)
            setCurrentFragment(SecondaryExtraComplxFragment())
        }
    }

    private fun setCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.nav_host_fragment_content_secondary_destination, fragment)
            commit()
        }
    }

    override fun setTitle(title: CharSequence?) {
        binding.toolbarTitle.text = title
    }

    fun toaster(v: Int) {
        Toast.makeText(this, "text $v", Toast.LENGTH_SHORT).show()
    }
}