package com.app.demoslotmachne.dialog

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.app.demoslotmachne.R
import com.app.demoslotmachne.databinding.DialogInfoBinding
import com.app.demoslotmachne.databinding.DialogWinBinding

class WinDialogFragment(val selectedBet:Int):DialogFragment() {

    lateinit var binding: DialogWinBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DialogWinBinding.inflate(inflater,container,false)
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

        binding.tvAmount.text = "+$selectedBet"

        Handler(Looper.getMainLooper()).postDelayed({
            dismiss()
        },3000)
    }
}