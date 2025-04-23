package com.app.demoslotmachne.ui.setting.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.demoslotmachne.data.model.Setting
import com.app.demoslotmachne.databinding.RcvSettingBinding
import com.app.demoslotmachne.utils.applyClickShrink

class SettingAdapter : RecyclerView.Adapter<SettingAdapter.ViewHolder>() {

    var itemList: ArrayList<Setting> = ArrayList()
    var onItemClick : ((Int)->Unit)? = null
    inner class ViewHolder(val binding: RcvSettingBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(RcvSettingBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount() = itemList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = itemList[holder.adapterPosition]
        holder.binding.apply {
            btnNext.text = data.name

            btnNext.scaleX = 1f
            btnNext.scaleY = 1f
            btnNext.applyClickShrink()

            btnNext.setOnClickListener {
                onItemClick?.invoke(data.id)
                notifyDataSetChanged()
            }
        }
    }

    fun setList(list: ArrayList<Setting>) {
        this.itemList = list
        notifyDataSetChanged()
    }

}