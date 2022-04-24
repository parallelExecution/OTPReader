package com.example.otpreader

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.otpreader.databinding.ListItemViewBinding

class SmsAdapter(val onClickListener: OnClickListener) :
    ListAdapter<String, SmsAdapter.SmsViewHolder>(DiffCallback) {

    class SmsViewHolder(private var binding: ListItemViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(message: String) {
            val otp = otpMatcher(message)
            if (otp != null) {
                binding.message = otp.substring(1)
            } else {
                binding.message = message
            }
            binding.executePendingBindings()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SmsViewHolder {
        return SmsViewHolder(
            ListItemViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SmsViewHolder, position: Int) {
        val message = getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(message)
        }
        holder.bind(message)
    }

    class OnClickListener(val clickListener: (message: String) -> Unit) {
        fun onClick(message: String) = clickListener(message)
    }
}