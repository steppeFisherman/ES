package com.example.es.ui.adapters

import androidx.recyclerview.widget.DiffUtil
import com.example.es.ui.model.DataUi

class HistoryFragmentDiffUtil(
    private val oldList: List<DataUi>,
    private val newList: List<DataUi>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size
    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition].time == newList[newItemPosition].time

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        when {
            oldList[oldItemPosition] != newList[newItemPosition] -> false
            else -> true
        }
}
