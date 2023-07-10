package com.demoapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import coil.load
import com.demoapp.business.model.ACCEPT_STATE_ACCEPT
import com.demoapp.business.model.ACCEPT_STATE_DECLINE
import com.demoapp.business.model.ACCEPT_STATE_UNKNOWN
import com.demoapp.business.model.AcceptState
import com.demoapp.business.model.User
import com.demoapp.databinding.ItemUserProfileLayoutBinding

private val UserItemComparator = object : DiffUtil.ItemCallback<User>() {
    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem == newItem
    }

    override fun getChangePayload(oldItem: User, newItem: User): Bundle {
        return bundleOf("acceptState" to newItem.acceptState)
    }
}

interface UserAdapterClickListener {

    fun onUserAccept(userId: String)

    fun onUserDecline(userId: String)

}

class UserAdapter(
    private val listener: UserAdapterClickListener
) : ListAdapter<User, UserViewHolder>(AsyncDifferConfig.Builder(UserItemComparator).build()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(
            binding = ItemUserProfileLayoutBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ), listener = listener
        )
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class UserViewHolder(
    private val binding: ItemUserProfileLayoutBinding,
    private val listener: UserAdapterClickListener
) : ViewHolder(binding.root) {

    fun bind(user: User) {
        binding.ivUserProfile.load(user.profilePictureUrl)
        binding.tvUserFirstLastName.text = "${user.firstName} ${user.lastName}"
        binding.tvAddress.text = "${user.city}, ${user.state}, ${user.city}"


        when (user.acceptState) {
            ACCEPT_STATE_ACCEPT -> {
                binding.tvAcceptOrDeclineMessage.isVisible = true
                binding.tvAcceptOrDeclineMessage.text = "Member accepted"
                binding.btnAccept.isInvisible = true
                binding.btnDecline.isInvisible = true
            }

            ACCEPT_STATE_DECLINE -> {
                binding.tvAcceptOrDeclineMessage.isVisible = true
                binding.tvAcceptOrDeclineMessage.text = "Member decline"
                binding.btnAccept.isInvisible = true
                binding.btnDecline.isInvisible = true
            }

            ACCEPT_STATE_UNKNOWN -> {
                binding.tvAcceptOrDeclineMessage.isVisible = false
                binding.btnAccept.isInvisible = false
                binding.btnDecline.isInvisible = false
            }
        }

        binding.btnAccept.setOnClickListener {
            listener.onUserAccept(user.id)
        }
        binding.btnDecline.setOnClickListener {
            listener.onUserDecline(user.id)
        }
    }

}