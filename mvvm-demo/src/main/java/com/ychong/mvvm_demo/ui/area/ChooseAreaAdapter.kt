package com.ychong.mvvm_demo.ui.area

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ychong.mvvm_demo.CoolWeatherApplication
import com.ychong.mvvm_demo.R
import com.ychong.mvvm_demo.databinding.ItemSimpleBinding

class ChooseAreaAdapter(private var dataList:List<String>) : RecyclerView.Adapter<ChooseAreaAdapter.ChooseAreaViewHolder>() {
      public var  itemClickListener: ItemClickListener? = null
     fun setListener(itemClickListener: ItemClickListener){
        this.itemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChooseAreaViewHolder {
       val binding = DataBindingUtil.inflate<ItemSimpleBinding>(
               LayoutInflater.from(parent.context),
               R.layout.item_simple,
               parent,
               false
       )

        return ChooseAreaViewHolder(binding,itemClickListener)
    }

    override fun getItemCount(): Int= dataList.size

    override fun onBindViewHolder(holder: ChooseAreaViewHolder, position: Int) {
        holder.bind(dataList[position],position)
    }
    class ChooseAreaViewHolder(private val binding:ItemSimpleBinding,private val itemClickListener: ItemClickListener?): RecyclerView.ViewHolder(binding.root){
        fun bind(data:String,position: Int){
            binding.data = data
            binding.simpleTextView.setOnClickListener{
                itemClickListener?.onClick(binding.simpleTextView,position)
            }
            binding.executePendingBindings()
        }
    }
    interface ItemClickListener {
        fun onClick(view:View,position: Int)
    }
}

