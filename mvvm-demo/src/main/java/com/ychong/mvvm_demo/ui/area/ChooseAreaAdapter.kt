package com.ychong.mvvm_demo.ui.area

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ychong.mvvm_demo.R
import com.ychong.mvvm_demo.databinding.ItemSimpleBinding

class ChooseAreaAdapter(private var dataList:List<String>)
    : RecyclerView.Adapter<ChooseAreaAdapter.ChooseAreaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChooseAreaViewHolder {
       val binding = DataBindingUtil.inflate<ItemSimpleBinding>(
               LayoutInflater.from(parent.context),
               R.layout.item_simple,
               parent,
               false
       )
        return ChooseAreaViewHolder(binding)
    }

    override fun getItemCount(): Int= dataList.size

    override fun onBindViewHolder(holder: ChooseAreaViewHolder, position: Int) {
        holder.bind(dataList[position])
    }
    class ChooseAreaViewHolder(private val binding:ItemSimpleBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(data:String){
            binding.data = data
            binding.executePendingBindings()
        }
    }

}