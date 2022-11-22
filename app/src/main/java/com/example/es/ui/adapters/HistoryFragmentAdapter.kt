package com.example.es.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.es.R
import com.example.es.databinding.HistoryItemRawBinding
import com.example.es.ui.model.DataUi

class HistoryFragmentAdapter(private val listener: Listener) :
    ListAdapter<DataUi, HistoryFragmentAdapter.MainHolder>(ItemCallback), View.OnClickListener {

    override fun onClick(v: View) {
        val user = v.tag as DataUi
        when (v.id) {
            R.id.btn_location -> listener.toLocation(user)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val view = HistoryItemRawBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        view.btnLocation.setOnClickListener(this)

        return MainHolder(view)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val user = getItem(position)

        holder.binding.apply {
            root.tag = user
            btnLocation.tag = user

            holder.binding.txtLocationAddress.text = user.locationAddress
            holder.binding.txtTime.text = user.time
        }
    }

    class MainHolder(val binding: HistoryItemRawBinding) : RecyclerView.ViewHolder(binding.root)

    interface Listener {
        fun toLocation(user: DataUi)
    }

    object ItemCallback : DiffUtil.ItemCallback<DataUi>() {
        override fun areItemsTheSame(oldItem: DataUi, newItem: DataUi): Boolean {
            return oldItem.time == newItem.time
        }

        override fun areContentsTheSame(oldItem: DataUi, newItem: DataUi): Boolean {
            return oldItem == newItem
        }
    }
}