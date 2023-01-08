package com.example.es.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.es.R
import com.example.es.databinding.HistoryCommentItemRawBinding
import com.example.es.databinding.HistoryItemRawBinding
import com.example.es.ui.model.DataUi

class HistoryFragmentAdapter(private val listener: Listener) :
    ListAdapter<DataUi, HistoryFragmentAdapter.RecyclerViewHolder>(ItemCallback),
    View.OnClickListener {

    override fun onClick(v: View) {
        val user = v.tag as DataUi
        when (v.id) {
            R.id.btn_location_history -> listener.toLocation(user)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {

        val simpleBinding = HistoryItemRawBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        simpleBinding.btnLocationHistory.setOnClickListener(this)

        val commentBinding = HistoryCommentItemRawBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        commentBinding.btnLocationComment.setOnClickListener(this)

        return when (viewType) {
            TYPE_SIMPLE -> RecyclerViewHolder.MainHolder(simpleBinding)
            TYPE_COMMENT -> RecyclerViewHolder.CommentHolder(commentBinding)
            else -> RecyclerViewHolder.MainHolder(simpleBinding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val user = getItem(position)

        when (holder) {
            is RecyclerViewHolder.MainHolder -> {
                holder.mBinding.apply {
                    root.tag = user
                    btnLocationHistory.tag = user

                    txtId.animation = AnimationUtils
                        .loadAnimation(
                            holder.mBinding.root.context,
                            R.anim.fade_transition_animation
                        )
                    txtId.text = user.id
                    txtTime.text = user.time
                    txtLocationAddress.text = user.locationAddress
                }
            }
            is RecyclerViewHolder.CommentHolder -> {
                holder.cBinding.apply {
                    root.tag = user
                    btnLocationComment.tag = user

                    txtIdComment.animation = AnimationUtils
                        .loadAnimation(
                            holder.cBinding.root.context,
                            R.anim.fade_transition_animation
                        )
                    txtIdComment.text = user.id
                    txtTimeComment.text = user.time
                    txtLocationAddressComment.text = user.locationAddress
                }
            }
        }
    }

    sealed class RecyclerViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {
        class MainHolder(val mBinding: HistoryItemRawBinding) : RecyclerViewHolder(mBinding)
        class CommentHolder(val cBinding: HistoryCommentItemRawBinding) : RecyclerViewHolder(cBinding)
    }

    override fun getItemViewType(position: Int): Int {
        val user = getItem(position)
        return when (user.locationFlagOnly) {
            true -> TYPE_SIMPLE
            else -> TYPE_COMMENT
        }
    }

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

    companion object {
        private const val TYPE_SIMPLE = 0
        private const val TYPE_COMMENT = 1
    }
}