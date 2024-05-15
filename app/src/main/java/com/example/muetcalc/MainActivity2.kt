package com.example.muetcalc

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.models.SlideModel
import com.google.android.material.navigation.NavigationView

class MainActivity2 : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var imageSlider: ImageSlider
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        drawerLayout = findViewById(R.id.drawer_layout)
        imageSlider = findViewById(R.id.image_slider)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Enable Up button to open drawer
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(getDrawable(R.drawable.menu))  // Set custom Up button icon (optional)

        val imgList = ArrayList<SlideModel>()
        imgList.add(SlideModel(R.drawable.adminn))
        imgList.add(SlideModel(R.drawable.cover))
        imgList.add(SlideModel(R.drawable.masjid))
        imgList.add(SlideModel(R.drawable.uspcasw))
        imgList.add(SlideModel(R.drawable.clmuet))
        imageSlider.setImageList(imgList)

        findViewById<ImageView>(R.id.cpnopt).setOnClickListener()
        {
            val intent=Intent(this,CPNCal::class.java)
            startActivity(intent)
        }
        findViewById<ImageView>(R.id.gpaopt).setOnClickListener()
        {
            val intent=Intent(this,MainActivity::class.java)
            startActivity(intent)
        }


        val navigationView = findViewById<NavigationView>(R.id.nav_view)

        toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            R.string.navigation_open_drawer,
            R.string.navigation_close_drawer
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navigationView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.website ->run {
                    openWebsite("https://www.muet.edu.pk/")
                }
                R.id.contact_me -> run {
                    openWebsite("https://www.linkedin.com/in/asfand-ali-dal-b9587923b/")
                }
                R.id.rules_and_regulations -> run  {
                    openWebsite("https://exam.muet.edu.pk/website/guidelines/")
                }
            }
            drawerLayout.closeDrawers()
            true
        }
    }

    private fun openWebsite(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (toggle.onOptionsItemSelected(item)) {
            true
        } else super.onOptionsItemSelected(item)
    }
}
