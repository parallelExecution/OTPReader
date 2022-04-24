package com.example.otpreader

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<String>?) {
    if (data != null) {
        val adapter = recyclerView.adapter as SmsAdapter
        adapter.submitList(data)
    }
}