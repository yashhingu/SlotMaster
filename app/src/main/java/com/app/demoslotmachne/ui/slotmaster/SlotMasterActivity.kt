package com.app.demoslotmachne.ui.slotmaster

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.demoslotmachne.R
import com.app.demoslotmachne.databinding.ActivitySlotMasterBinding
import com.app.demoslotmachne.dialog.InfoDialogFragment
import com.app.demoslotmachne.dialog.WinDialogFragment
import com.app.demoslotmachne.ui.slotmaster.adapter.BetAdapter
import com.app.demoslotmachne.ui.slotmaster.adapter.ReelAdapter
import com.app.demoslotmachne.utils.ListUtils
import com.app.demoslotmachne.utils.PrefHelper
import com.app.demoslotmachne.utils.ReelItemDecoration
import com.app.demoslotmachne.utils.applyClickShrink
import kotlin.math.abs

class SlotMasterActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySlotMasterBinding
    private lateinit var betAdapter: BetAdapter

    val fruits = listOf(R.drawable.iv_7, R.drawable.iv_bell, R.drawable.iv_dice)
    private var isSpinning = false
    private var reelsStopped = 0
    private val handler = Handler(Looper.getMainLooper())
    private val vibrator by lazy {
        ContextCompat.getSystemService(this, Vibrator::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySlotMasterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initAdapter()
        binding.ivHome.applyClickShrink()
        binding.ivInfo.applyClickShrink()
        binding.btnPlay.applyClickShrink()

        binding.ivHome.setOnClickListener {
            finish()
        }

        binding.ivInfo.setOnClickListener {
            val dialog = InfoDialogFragment(true)
            dialog.show(supportFragmentManager, dialog.tag)
        }
        setupReel(binding.wheel1, 0)
        setupReel(binding.wheel2, 1)
        setupReel(binding.wheel3, 2)

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
                startSpin()
            }
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

    private fun setupReel(reel: RecyclerView, reelIndex: Int) {
        reel.setOnTouchListener { view, motionEvent -> true }
        reel.adapter = ReelAdapter(fruits,reelIndex)

        val middlePosition = Int.MAX_VALUE / 2
        reel.scrollToPosition(middlePosition)

        // Add decoration after layout is complete
        reel.post {
            reel.addItemDecoration(ReelItemDecoration())
        }
        reel.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
                val centerY = rv.height / 2f
                val d1 = centerY * 0.75f // distance threshold for min scale/alpha
                val scaleMin = 0.7f
                val scaleMax = 1f
                val alphaMin = 0.4f
                val alphaMax = 1f

                for (i in 0 until rv.childCount) {
                    val child = rv.getChildAt(i)
                    val childCenterY = (child.top + child.bottom) / 2f
                    val distance = abs(centerY - childCenterY)

                    // Scale based on distance
                    val scale = ((scaleMax - scaleMin) * (1 - (distance / d1).coerceIn(0f, 1f))) + scaleMin
                    child.scaleX = scale
                    child.scaleY = scale

                    // Alpha fades as it moves away from center
                    val alpha = ((alphaMax - alphaMin) * (1 - (distance / d1).coerceIn(0f, 1f))) + alphaMin
                    child.alpha = alpha
                }
            }
        })
    }

    private fun startSpin() {
        isSpinning = true
        reelsStopped = 0
        disableBtn()
        spinWheel(binding.wheel1,  0L)
        spinWheel(binding.wheel2,  200L)
        spinWheel(binding.wheel3,  400L)
    }

    private fun spinWheel(recyclerView: RecyclerView, delay: Long) {
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager

        Handler(Looper.getMainLooper()).postDelayed({
            val middle = Int.MAX_VALUE / 2
            recyclerView.scrollToPosition(middle) // Reset position

            Handler(Looper.getMainLooper()).postDelayed({
                val randomOffset = (3000..5000).random()
                recyclerView.smoothScrollBy(0, randomOffset, LinearInterpolator())

                Handler(Looper.getMainLooper()).postDelayed({
                    alignWheel(recyclerView)
                }, 1500 + 200)

            }, 50) // Small delay to allow position reset

        }, delay)
    }
    private fun alignWheel(recyclerView: RecyclerView) {
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        val centerY = recyclerView.height / 2
        var closestChild: View? = null
        var minDistance = Int.MAX_VALUE

        for (i in 0 until layoutManager.childCount) {
            val child = layoutManager.getChildAt(i) ?: continue
            val childCenterY = (child.top + child.bottom) / 2
            val distance = abs(centerY - childCenterY)
            if (distance < minDistance) {
                minDistance = distance
                closestChild = child
            }
        }

        closestChild?.let {
            val scrollAdjustment = (it.top + it.bottom) / 2 - centerY
            recyclerView.smoothScrollBy(0, scrollAdjustment)

            Handler(Looper.getMainLooper()).postDelayed({
                reelsStopped++
                vibrate()
               if(reelsStopped == 3){
                   if (!isFinishing) {
                       checkWin()
                       isSpinning = false
                   }
               }
            }, 300)
        }
    }

    fun getCenteredItem(reel:RecyclerView,layoutManager: LinearLayoutManager): Int {
        val centerY = reel.height / 2
        var closestDistance = Int.MAX_VALUE
        var centeredPosition = -1

        for (i in 0 until layoutManager.childCount) {
            val child = layoutManager.getChildAt(i) ?: continue
            val childCenterY = (child.top + child.bottom) / 2
            val distance = abs(childCenterY - centerY)

            if (distance < closestDistance) {
                closestDistance = distance
                centeredPosition = reel.getChildAdapterPosition(child)
            }
        }
        return centeredPosition
    }
    fun getSelectedFruit(recyclerView: RecyclerView): Int {
        val lm = recyclerView.layoutManager as LinearLayoutManager
        val pos = getCenteredItem(recyclerView, lm)
        return if (fruits.size == 1) fruits[0] else fruits[pos % fruits.size]
    }

    private fun vibrate() {
        vibrator?.vibrate(VibrationEffect.createOneShot(30, VibrationEffect.DEFAULT_AMPLITUDE))
    }

    private fun checkWin() {
        val selectedBet = betAdapter.itemList.find { it.isSelected }
        if (selectedBet == null) return
        val f1 = getSelectedFruit(binding.wheel1)
        val f2 = getSelectedFruit(binding.wheel2)
        val f3 = getSelectedFruit(binding.wheel3)
        Log.e("ACE", "checkWin: $f1 $f2 $f3")
        Log.e("ACE", "fruits:"+fruits.map { it })

        if (f1 == f2 && f2 == f3) {
            val times = when (f1) {
                R.drawable.iv_7 -> 7  // Seven 7x of current bet
                R.drawable.iv_bell -> 5   // Bell 5x of current bet
                R.drawable.iv_dice -> 2   // Dice 2x of current bet
                else -> 1
            }
            val winAmount = selectedBet.name.toInt() * times
            val finalAmount = PrefHelper.getCoins(this) + winAmount
            binding.tvCoins.text = getString(R.string.coins_x, finalAmount.toString())
            PrefHelper.addCoins(this, winAmount)

            val dialog = WinDialogFragment(winAmount)
            dialog.show(supportFragmentManager, dialog.tag)

            if (f1 == R.drawable.iv_7) {
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