package com.app.demoslotmachne.ui.intro

import android.content.Intent
import android.os.Bundle
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import com.app.demoslotmachne.MainActivity
import com.app.demoslotmachne.R
import com.app.demoslotmachne.databinding.ActivityIntroBinding
import com.app.demoslotmachne.utils.applyClickShrink
import com.app.demoslotmachne.utils.fadeText

class IntroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIntroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnNext.applyClickShrink()
        binding.materialTextView.fadeText(getString(R.string.intro_1))

        binding.btnNext.setOnClickListener {
            if (binding.materialTextView.text == getString(R.string.intro_2)) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
                return@setOnClickListener
            }
            binding.materialTextView.fadeText(getString(R.string.intro_2))
            binding.ivDummy.setImageResource(R.drawable.intro_2)
        }

        onBackPressedDispatcher.addCallback(this) {
            if (binding.materialTextView.text == getString(R.string.intro_2)) {
                binding.materialTextView.fadeText(getString(R.string.intro_1))
                binding.ivDummy.setImageResource(R.drawable.intro_1)
            } else {
                finish()
            }
        }

        binding.motionLayout.setTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionStarted(motionLayout: MotionLayout, startId: Int, endId: Int) {
                // Optional: Do something when the transition starts
            }

            override fun onTransitionChange(motionLayout: MotionLayout, startId: Int, endId: Int, progress: Float) {
                // Optional: Do something during the transition
            }

            override fun onTransitionCompleted(motionLayout: MotionLayout, currentId: Int) {
                // When the animation completes (reaches the end state),
                // trigger it to start again from the start state.
                motionLayout.transitionToState(R.id.end)
                motionLayout.transitionToStart() // Or motionLayout.progress = 0f;
            }

            override fun onTransitionTrigger(motionLayout: MotionLayout, triggerId: Int, positive: Boolean, progress: Float) {
                // Optional: Do something when a trigger is activated
            }
        })

        // Start the animation initially
        binding.motionLayout.transitionToStart()
    }
}