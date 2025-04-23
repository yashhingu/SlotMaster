package com.app.demoslotmachne

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.app.demoslotmachne.ui.bonus.BonusActivity
import com.app.demoslotmachne.databinding.ActivityMainBinding
import com.app.demoslotmachne.ui.fortunereel.FortuneReelActivity
import com.app.demoslotmachne.ui.setting.SettingActivity
import com.app.demoslotmachne.ui.slotmaster.SlotMasterActivity
import com.app.demoslotmachne.utils.PrefHelper
import com.app.demoslotmachne.utils.applyClickShrink

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBonus.applyClickShrink()
        binding.btnFortune.applyClickShrink()
        binding.btnSlotPlay.applyClickShrink()
        binding.ivSetting.applyClickShrink()

        binding.btnBonus.setOnClickListener {
            val intent = Intent(this, BonusActivity::class.java)
            startActivity(intent)
        }

        binding.btnFortune.setOnClickListener {
            val intent = Intent(this, FortuneReelActivity::class.java)
            startActivity(intent)
        }

        binding.btnSlotPlay.setOnClickListener {
            val intent = Intent(this, SlotMasterActivity::class.java)
            startActivity(intent)
        }

        binding.ivSetting.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
        }


    }

    override fun onResume() {
        super.onResume()
        binding.tvCoins.text = getString(R.string.coins_x, PrefHelper.getCoins(this).toString())
    }
}