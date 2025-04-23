package com.app.demoslotmachne.ui.fortunereel

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.HapticFeedbackConstants
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.TranslateAnimation
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.app.demoslotmachne.R
import com.app.demoslotmachne.data.model.Setting
import com.app.demoslotmachne.databinding.ActivityFortuneReellBinding
import com.app.demoslotmachne.dialog.InfoDialogFragment
import com.app.demoslotmachne.dialog.WinDialogFragment
import com.app.demoslotmachne.ui.slotmaster.adapter.BetAdapter
import com.app.demoslotmachne.utils.ListUtils
import com.app.demoslotmachne.utils.PrefHelper
import com.app.demoslotmachne.utils.applyClickShrink
import kotlin.random.Random

class FortuneReelActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFortuneReellBinding
    private lateinit var betAdapter: BetAdapter
    private var isSpinning = false
    private val symbols = listOf(
        R.drawable.iv_limon,
        R.drawable.iv_apple,
        R.drawable.iv_cherry,
    )
    private val reelResults = IntArray(3) { 0 }
    private val handler = Handler(Looper.getMainLooper())

    private val vibrator by lazy {
        ContextCompat.getSystemService(this, Vibrator::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFortuneReellBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initAdapter()
        binding.ivHome.applyClickShrink()
        binding.ivInfo.applyClickShrink()
        binding.btnPlay.applyClickShrink()

        binding.btnPlay.setOnClickListener {
            if (!isSpinning) {
                if (PrefHelper.getCoins(this) == 0) {
                    Toast.makeText(this, "Insufficient coin", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                val selectedBet = betAdapter.itemList.find { it.isSelected }
                if (selectedBet == null) {
                    Toast.makeText(this, "Please choose your bet", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                if (selectedBet.name.toInt() > PrefHelper.getCoins(this)) {
                    Toast.makeText(this, "Insufficient coin", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                spinReels(selectedBet)
            }
        }

        binding.ivHome.setOnClickListener {
            finish()
        }

        binding.ivInfo.setOnClickListener {
            val dialog = InfoDialogFragment()
            dialog.show(supportFragmentManager, dialog.tag)
        }
    }
    override fun onResume() {
        super.onResume()
        binding.tvCoins.text = getString(R.string.coins_x, PrefHelper.getCoins(this).toString())
    }
    private fun initAdapter() {
        betAdapter = BetAdapter(this)
        binding.rcvBets.adapter = betAdapter
        betAdapter.setList(ListUtils.betList())
    }

    private fun spinReels(selectedBet: Setting) {
        isSpinning = true
        handler.removeCallbacksAndMessages(null)
        disableBtn()

        // Play haptic feedback for spin start
        vibrator?.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))

        // Spin each reel with sequential stopping
        spinReel(binding.reel1, 1500) { reelResults[0] = it }
        spinReel(binding.reel2, 2300) { reelResults[1] = it }
        spinReel(binding.reel3, 3000) { reelResults[2] = it }

        // Check results after all reels have stopped
        handler.postDelayed({
            if (!isFinishing) {
                checkResults(selectedBet)
                isSpinning = false
            }
        }, 3300)
    }

    private fun spinReel(reel: ImageView, duration: Long, onComplete: (Int) -> Unit) {
        // Create vertical translate animation to simulate reel spinning
        val symbolCount = symbols.size

        // Create multiple animations to simulate spinning symbols
        var finalSymbolIndex = Random.nextInt(symbolCount)

        // Start rapid symbol changing to simulate spinning
        val symbolChangeInterval = 80L // milliseconds between symbol changes during spin
        var symbolChangeCount = 0
        val maxSymbolChanges = (duration / symbolChangeInterval).toInt()

        val symbolChangeRunnable = object : Runnable {
            override fun run() {
                if (symbolChangeCount < maxSymbolChanges) {
                    // Change to random symbol during spinning
                    val randomIndex = Random.nextInt(symbolCount)
                    reel.setImageResource(symbols[randomIndex])

                    // During the spin, provide gentle haptic feedback occasionally
                    if (symbolChangeCount % 5 == 0) {
                        reel.performHapticFeedback(HapticFeedbackConstants.CLOCK_TICK)
                    }

                    symbolChangeCount++
                    handler.postDelayed(this, symbolChangeInterval)
                } else {
                    // Final symbol
                    reel.setImageResource(symbols[finalSymbolIndex])

                    // Strong haptic feedback for reel stop
                    reel.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)

                    // Notify result
                    onComplete(finalSymbolIndex)
                }
            }
        }

        // Start vertical bouncing animation
        val bounceAnimation = TranslateAnimation(
            0f, 0f,  // X axis: no movement
            0f, 50f   // Y axis: small bounce
        ).apply {
            this.duration = 100
            this.interpolator = AccelerateDecelerateInterpolator()
            this.repeatMode = TranslateAnimation.REVERSE
            this.repeatCount = 1
        }

        // Start the symbol changing
        handler.post(symbolChangeRunnable)

        // Add stopping animation at the end
        handler.postDelayed({
            reel.startAnimation(bounceAnimation)
        }, duration + 50)
    }

    private fun checkResults(selectedBet: Setting) {
        // Get the vibrator service for result feedback

        // Check for winning combinations and assign payouts based on symbols
        if (reelResults[0] == reelResults[1] && reelResults[1] == reelResults[2]) {
            // All three match - win depends on symbol value
            val times = when (reelResults[0]) {
                0 -> 2  // Lemon 2x of current bet
                1 -> 7   // Apple 7x of current bet
                2 -> 5   // Cherry 5x of current bet
                else -> 1
            }
            val winAmount = selectedBet.name.toInt() * times
            val finalAmount = PrefHelper.getCoins(this) + winAmount
            binding.tvCoins.text = getString(R.string.coins_x, finalAmount.toString())
            PrefHelper.addCoins(this, winAmount)

            val dialog = WinDialogFragment(winAmount)
            dialog.show(supportFragmentManager, dialog.tag)

            if (reelResults[0] == 1) {
                vibrator?.vibrate(
                    VibrationEffect.createOneShot(
                        600,
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )
            } else {
                vibrator?.vibrate(
                    VibrationEffect.createOneShot(
                        300,
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )
            }
            enableBtn()
        } else {
            if (PrefHelper.getCoins(this) < 0) return
            PrefHelper.addCoins(this, (-selectedBet.name.toInt()))
            vibrator?.vibrate(VibrationEffect.createOneShot(30, VibrationEffect.DEFAULT_AMPLITUDE))
            handler.postDelayed({
                if (!isFinishing) {
                    enableBtn()
                    binding.tvCoins.text =
                        getString(R.string.coins_x, PrefHelper.getCoins(this).toString())
                }
            }, 200)
        }
    }

    private fun enableBtn() {
        binding.btnPlay.isEnabled = true
        binding.btnPlay.alpha = 1f
    }

    private fun disableBtn() {
        binding.btnPlay.isEnabled = true
        binding.btnPlay.alpha = .6f
    }
}