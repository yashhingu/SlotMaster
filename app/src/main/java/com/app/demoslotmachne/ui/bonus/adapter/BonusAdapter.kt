package com.app.demoslotmachne.ui.bonus.adapter

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.app.demoslotmachne.R
import com.app.demoslotmachne.data.model.Bonus
import com.app.demoslotmachne.databinding.RcvBonusBoxBinding
import com.app.demoslotmachne.utils.PrefHelper

class BonusAdapter(val context:Context) : RecyclerView.Adapter<BonusAdapter.ViewHolder>() {

    var itemList: ArrayList<Bonus> = ArrayList()
    var hasClickedToday = PrefHelper.hasClickedToday(context)
    var result:((Boolean)->Unit)? = null
    val shake = AnimationUtils.loadAnimation(context, R.anim.shake)
    val pop = AnimationUtils.loadAnimation(context, R.anim.popup)
    class ViewHolder(val binding: RcvBonusBoxBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            RcvBonusBoxBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = itemList[holder.adapterPosition]
        holder.binding.apply {

            // Set default box image
            if(data.isBonusBox && data.isClicked){
                ivBonus.setImageResource(R.drawable.iv_bonus_500)
                ivBonus.startAnimation(pop)
            }else{
                ivBonus.setImageResource(R.drawable.iv_bonus)
            }

            // Disable clicks if already clicked
            ivBonus.isEnabled = !hasClickedToday && !data.isClicked

            ivBonus.setOnClickListener {
                if (!hasClickedToday) {
                    data.isClicked = true
                    hasClickedToday = true
                    PrefHelper.setClickedToday(context)

                    ivBonus.startAnimation(shake)

                    Handler(Looper.getMainLooper()).postDelayed({
                        if (data.isBonusBox) {
                            PrefHelper.addCoins(context,500)
                            result?.invoke(true)
                        } else {
                            result?.invoke(false)
                        }
                        notifyDataSetChanged()
                    }, 500) // wait for shake to complete
                }
            }
        }
    }

    override fun getItemCount(): Int = itemList.size



    fun setList(list: ArrayList<Bonus>) {
        this.itemList = list
        notifyDataSetChanged()
    }

    fun generateRandomBonusBox() {
        if (itemList.isNotEmpty()) {
            val randomIndex = (0 until itemList.size).random()
            itemList.forEachIndexed { index, bonus ->
                bonus.isBonusBox = index == randomIndex
            }
        }
    }
}