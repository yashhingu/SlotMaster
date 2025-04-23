package com.app.demoslotmachne.ui.setting

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.app.demoslotmachne.R
import com.app.demoslotmachne.databinding.ActivitySettingBinding
import com.app.demoslotmachne.ui.setting.adapter.SettingAdapter
import com.app.demoslotmachne.utils.ListUtils
import com.app.demoslotmachne.utils.applyClickShrink
import com.app.demoslotmachne.utils.openChromeTab

class SettingActivity : AppCompatActivity() {

    companion object{
        const val TERM = "https://www.freeprivacypolicy.com/live/8b6e95cd-1cfe-40f1-9626-b2735d5d3883"
        const val PRIVACY = "https://www.freeprivacypolicy.com/live/be89fae7-1fcc-4a8d-9b5a-0b15f2956431"
    }

    private lateinit var binding: ActivitySettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivHome.applyClickShrink()
        binding.ivHome.setOnClickListener {
            finish()
        }
        initAdapter()
    }

    private fun initAdapter() {
        val adapter = SettingAdapter()
        binding.rcvSetting.adapter = adapter
        adapter.setList(ListUtils.settingList())

        adapter.onItemClick = {
            when (it) {
                1 -> {
                    openChromeTab(this, PRIVACY)
                }

                2 -> {
                    openChromeTab(this,TERM)
                }

                3 -> {
                    startActivity(Intent(this, SettingDetailActivity::class.java).putExtra("type",3))
                }
            }
        }
    }
}