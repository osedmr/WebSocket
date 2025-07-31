package com.galerkinrobotics.test.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.galerkinrobotics.test.R
import com.galerkinrobotics.test.data.model.Lighting
import com.galerkinrobotics.test.databinding.LightingCardViewBinding

class LightingAdapter : RecyclerView.Adapter<LightingAdapter.ViewHolder>() {

    private var onItemClickListener: ((Lighting) -> Unit)? = null

    fun setOnItemClickListener(listener: (Lighting) -> Unit) {
        onItemClickListener = listener
    }

    private val DifferUtilCallBAck = object : DiffUtil.ItemCallback<Lighting>() {
        override fun areItemsTheSame(oldItem: Lighting, newItem: Lighting): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Lighting, newItem: Lighting): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, DifferUtilCallBAck)

    inner class ViewHolder(var binding: LightingCardViewBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LightingCardViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val lighting = differ.currentList[position]
        holder.binding.apply {
            // LED ikonu
            imageView.setImageResource(R.drawable.led)
            
            // İsim
            textView.text = lighting.name
            
            // Switch durumu
            lightSwitch.isChecked = lighting.currentValue == 1
            
            // Switch aktiflik durumu
            lightSwitch.isEnabled = lighting.isActive
            
            // Click listener
            root.setOnClickListener {
                onItemClickListener?.invoke(lighting)
            }
            
            // Switch listener
            lightSwitch.setOnCheckedChangeListener { _, isChecked ->
                onItemClickListener?.invoke(lighting.copy(currentValue = if (isChecked) 1 else 0))
            }
        }
    }
}