package com.app.demoslotmachne.ui.slotmaster.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.demoslotmachne.databinding.ItemSlotWheelBinding

class ReelAdapter(private val fruits: List<Int>,val index:Int) : RecyclerView.Adapter<ReelAdapter.ReelViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReelViewHolder {

        return ReelViewHolder(ItemSlotWheelBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ReelViewHolder, position: Int) {
        val actualPosition = if (fruits.size == 1) 0 else holder.adapterPosition % fruits.size
        holder.binding.ivSlot.setImageResource(fruits[actualPosition])
    }

    override fun getItemCount(): Int = Int.MAX_VALUE // Infinite scrolling illusion

   inner class ReelViewHolder(val binding: ItemSlotWheelBinding) : RecyclerView.ViewHolder(binding.root){
       var imageResId: Int = -1
   }
}