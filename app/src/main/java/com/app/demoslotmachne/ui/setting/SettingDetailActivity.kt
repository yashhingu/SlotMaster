package com.app.demoslotmachne.ui.setting

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.app.demoslotmachne.MainActivity
import com.app.demoslotmachne.R
import com.app.demoslotmachne.databinding.ActivitySettingDetailBinding

class SettingDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingDetailBinding
    private var type = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        type = intent.getIntExtra("type", 1)
        when (type) {
            1 -> {}
            2 -> {}
            3 -> {
                aboutUs()
            }

            4 -> {}
        }
        binding.ivHome.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
    }

    private fun aboutUs() {
        binding.tvTitle.text = getString(R.string.about_us)
        binding.tvDesc.text = getString(R.string.about_us_desc)
    }
}