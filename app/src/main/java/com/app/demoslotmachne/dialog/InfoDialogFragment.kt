package com.app.demoslotmachne.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.app.demoslotmachne.R
import com.app.demoslotmachne.databinding.DialogInfoBinding

class InfoDialogFragment(val isSlotMaster:Boolean = false):DialogFragment() {

    lateinit var binding: DialogInfoBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DialogInfoBinding.inflate(inflater,container,false)
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog?.window?.setDimAmount(.7f)
        return binding.root

    }

    override fun getTheme(): Int {
        return R.style.Theme_Dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(isSlotMaster) {
            binding.ivFirst.setImageResource(R.drawable.iv_dice)
            binding.ivSecond.setImageResource(R.drawable.iv_bell)
            binding.ivThird.setImageResource(R.drawable.iv_7)
        }else{
            binding.ivFirst.setImageResource(R.drawable.iv_limon)
            binding.ivSecond.setImageResource(R.drawable.iv_cherry)
            binding.ivThird.setImageResource(R.drawable.iv_apple)
        }

        binding.btnClose.setOnClickListener {
            dismiss()
        }
    }
}