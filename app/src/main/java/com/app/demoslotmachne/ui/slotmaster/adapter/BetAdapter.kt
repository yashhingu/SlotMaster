package com.app.demoslotmachne.ui.slotmaster.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.app.demoslotmachne.R
import com.app.demoslotmachne.data.model.Setting
import com.app.demoslotmachne.databinding.RcvBetsBinding

class BetAdapter(val context:Context) : RecyclerView.Adapter<BetAdapter.ViewHolder>() {

    var itemList: ArrayList<Setting> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            RcvBetsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = itemList[holder.adapterPosition]
        holder.binding.apply {
            btnBet.text = data.name
            if (data.isSelected) {
                btnBet.alpha = 1f
                btnBet.strokeColor = ColorStateList.valueOf(context.getColor(android.R.color.white))
            } else {
                btnBet.alpha = .8f
                btnBet.strokeColor = ColorStateList.valueOf(context.getColor(android.R.color.transparent))
            }
            btnBet.setOnClickListener {
                itemList.forEachIndexed { index, setting ->
                    setting.isSelected = index == holder.adapterPosition
                }
                notifyDataSetChanged()
            }
        }
    }

    override fun getItemCount(): Int = itemList.size

    class ViewHolder(val binding: RcvBetsBinding) : RecyclerView.ViewHolder(binding.root)

    fun setList(list: ArrayList<Setting>) {
        this.itemList = list
        notifyDataSetChanged()
    }
}