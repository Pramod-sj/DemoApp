package com.demoapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.demoapp.databinding.ActivityMainBinding
import com.google.android.material.snackbar.BaseTransientBottomBar.ANIMATION_MODE_SLIDE
import com.google.android.material.snackbar.BaseTransientBottomBar.BaseCallback
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), UserAdapterClickListener {

    companion object {
        private const val TAG = "MainActivity"
    }

    private var _binding: ActivityMainBinding? = null
    val binding: ActivityMainBinding
        get() = _binding!!

    private val viewModel by viewModels<MainViewModel>()

    private var adapter: UserAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupUserAdapter()
        setupSwipeToRefresh()
        bindUiState()
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiEvent.collect { uiEvent ->
                    when (uiEvent) {
                        is UiEvent.ShowToastMessage -> {
                            Toast.makeText(this@MainActivity, uiEvent.message, Toast.LENGTH_SHORT)
                                .show()
                        }

                        is UiEvent.SnackBarMessage -> {
                            Snackbar.make(binding.root, uiEvent.message, Snackbar.LENGTH_SHORT)
                                .setAnimationMode(ANIMATION_MODE_SLIDE)
                                .addCallback(object : Snackbar.Callback() {
                                    override fun onDismissed(
                                        transientBottomBar: Snackbar?,
                                        event: Int
                                    ) {
                                        super.onDismissed(transientBottomBar, event)
                                        if (event == DISMISS_EVENT_SWIPE
                                            || event == DISMISS_EVENT_TIMEOUT
                                        ) {
                                            uiEvent.noActionCallback?.invoke()
                                        }

                                    }
                                }
                                ).apply {
                                    uiEvent.action?.let { action -> setAction(action) { uiEvent.actionCallback?.invoke() } }
                                }.show()
                        }

                    }
                }
            }
        }
        viewModel.fetchUser(false)
    }

    private fun setupSwipeToRefresh() {
        binding.srlFetchNewUsers.setOnRefreshListener {
            viewModel.fetchUser(true)
        }
    }

    private fun bindUiState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    if (uiState.showContentLoader) {
                        binding.indicatorLoader.show()
                    } else {
                        binding.indicatorLoader.hide()
                    }
                    binding.srlFetchNewUsers.isRefreshing = uiState.showSwipeToRefreshLoader
                    adapter?.submitList(uiState.users)
                }
            }
        }
    }

    private fun setupUserAdapter() {
        adapter = UserAdapter(this)
        binding.rvUsers.adapter = adapter
    }

    override fun onUserAccept(userId: String) {
        viewModel.acceptUser(userId)
    }

    override fun onUserDecline(userId: String) {
        viewModel.declineUser(userId)
    }

}