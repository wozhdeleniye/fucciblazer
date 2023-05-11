package com.example.compicprogtamming

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.GravityCompat
import com.example.compicprogtamming.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
            naviView.setNavigationItemSelectedListener {
                when(it.itemId){
                    R.id.crVar ->{
                        //делать что-то или запустить функцию
                    }
                }
                drawer.closeDrawer(GravityCompat.START)
                true
            }
            open.setOnClickListener{
                drawer.openDrawer(GravityCompat.START)
            }
        }
    }
}