package com.example.artesanato

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        val viewPager2 = findViewById<ViewPager2>(R.id.viewPager)
        viewPager2.adapter = TabAdapter(this)
        TabLayoutMediator(tabLayout,viewPager2){
            tab, position ->
            viewPager2.setCurrentItem(tab.position, true)
            if(position==0) tab.setText("PRODUTOS")
            if(position==1) tab.setText("ITENS")
        }.attach()
    }
}