package com.example.es.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.es.databinding.HistoryItemRawBinding
import com.example.es.ui.model.DataUi
import com.example.es.utils.LoadImage

class HistoryFragmentAdapter(private val loadImage: LoadImage) :
    RecyclerView.Adapter<HistoryFragmentAdapter.MainHolder>() {

    private var mList = emptyList<DataUi>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val view = HistoryItemRawBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return MainHolder(view)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {

        holder.binding.apply {
            loadImage.load(holder.binding.profileImage, "")
            holder.binding.txtName.text = mList[position].full_name
            holder.binding.txtLatitude.text = mList[position].time_location
            holder.binding.txtPhone.text = mList[position].phone_user
        }
    }

    override fun getItemCount() = mList.size

    class MainHolder(val binding: HistoryItemRawBinding) : RecyclerView.ViewHolder(binding.root)

    fun setData(newList: List<DataUi>) {
        val diffUtil = HistoryFragmentDiffUtil(mList, newList)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        mList = newList
        diffResult.dispatchUpdatesTo(this)
    }
}