package com.galerkinrobotics.test.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.galerkinrobotics.test.data.model.Services
import com.galerkinrobotics.test.databinding.InohomCardViewBinding

class ServicesAdapter :RecyclerView.Adapter<ServicesAdapter.ViewHolder>() {

    private var onItemClickListener: ((Services) -> Unit)? = null

    fun setOnItemClickListener(listener: (Services) -> Unit) {
        onItemClickListener = listener
    }

    private val DifferUtilCallBAck = object : DiffUtil.ItemCallback<Services>() {
        override fun areItemsTheSame(oldItem: Services, newItem: Services): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Services, newItem: Services): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this,DifferUtilCallBAck)

    inner class ViewHolder(var binding : InohomCardViewBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = InohomCardViewBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       val servis = differ.currentList[position]
        holder.binding.apply {
            imageView.setImageResource(servis.icon)
            textView.text=servis.title
            
            // Click listener ekle
            root.setOnClickListener {
                onItemClickListener?.invoke(servis)
            }
        }
    }
}