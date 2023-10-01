package com.example.ecommerce.ui.main.notifcation

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.ecommerce.R
import com.example.ecommerce.databinding.FragmentNotificationBinding
import com.example.ecommerce.room.entity.NotificationEntity
import com.example.ecommerce.ui.main.cart.WishlistAdapter
import com.example.ecommerce.ui.main.wishlist.WishlistViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NotificationFragment : Fragment() {

    private var _bindig: FragmentNotificationBinding? = null
    val binding get() = _bindig!!

    private lateinit var notificationAdapter: NotificationAdapter
    private val viewModel by viewModels<NotificationViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _bindig = FragmentNotificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        notificationAdapter = NotificationAdapter(
            NotificationAdapter.NotificaitionEntityDiffCallback
        ) { it -> updateNotification(it) }

        binding.topAppBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        binding.recyclerView.adapter = notificationAdapter
        binding.recyclerView.itemAnimator?.changeDuration = 0

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.notificationItem.collect {
                if (it.isNotEmpty()) {
                    binding.recyclerView.isVisible = true
                    binding.errorData.isVisible = false
                    notificationAdapter.submitList(it)
                } else {
                    binding.errorData.isVisible = true
                    binding.recyclerView.isVisible = false
                }
            }
        }

    }

    private fun updateNotification(it: NotificationEntity) {

        viewModel.updateRead(it.idNotification, true)

    }

    override fun onDestroy() {
        super.onDestroy()
        _bindig = null
    }

}