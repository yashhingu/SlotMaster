package com.app.demoslotmachne.ui.bonus

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.app.demoslotmachne.R
import com.app.demoslotmachne.data.model.Bonus
import com.app.demoslotmachne.databinding.ActivityBonusBinding
import com.app.demoslotmachne.ui.bonus.adapter.BonusAdapter
import com.app.demoslotmachne.utils.PrefHelper

class BonusActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBonusBinding
    private lateinit var adapter: BonusAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBonusBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initAdapter()
        binding.tvDesc.text = if (PrefHelper.hasClickedToday(this)) {
            "The bonus is currently unavailable. Please try again later!"
        } else {
            "Choose a gift to open your daily bonus!"
        }
        binding.ivHome.setOnClickListener {
            finish()
        }
    }
    override fun onResume() {
        super.onResume()
        binding.tvCoins.text = getString(R.string.coins_x, PrefHelper.getCoins(this).toString())
    }
    private fun initAdapter() {
        adapter = BonusAdapter(this)
        val boxList = arrayListOf(
            Bonus(id = 1),
            Bonus(id = 2),
            Bonus(id = 3)
        )
        adapter.setList(boxList)
        adapter.generateRandomBonusBox()
        binding.rcvBonus.adapter = adapter

        adapter.result = { isSuccess ->
            binding.tvDesc.text = if (isSuccess) {
                binding.tvCoins.text = getString(R.string.coins_x, PrefHelper.getCoins(this).toString())
                "You have received 500 bonus coins!"
            } else {
                "Oops!😢 No bonus this time!"
            }
        }
    }
}