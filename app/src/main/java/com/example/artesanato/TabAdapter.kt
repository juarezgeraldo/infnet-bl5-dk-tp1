package com.example.artesanato

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class TabAdapter (fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity){
    val fragments = arrayOf(
        Produtos(),
        ItensProdutos()
    )

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}