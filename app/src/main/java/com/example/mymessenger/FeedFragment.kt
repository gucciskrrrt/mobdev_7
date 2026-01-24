package com.example.mymessenger

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mymessenger.databinding.FragmentFeedBinding
import com.example.mymessenger.ui.feed.FeedViewModel
import com.example.mymessenger.ui.feed.MessageAdapter
import com.example.mymessenger.utils.NetworkStatus
import com.google.android.material.snackbar.Snackbar

class FeedFragment : Fragment() {
    private var _binding: FragmentFeedBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FeedViewModel by viewModels()
    private lateinit var messageAdapter: MessageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("FeedFragment", "onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("FeedFragment", "onCreateView")
        _binding = FragmentFeedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("FeedFragment", "onViewCreated")

        setupRecyclerView()
        setupSwipeRefresh()
        setupObservers()
        setupListeners()
    }

    private fun setupRecyclerView() {
        messageAdapter = MessageAdapter { message ->
            viewModel.toggleLike(message)
        }
        binding.recyclerMessages.apply {
            adapter = messageAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefresh.setColorSchemeColors(
            ContextCompat.getColor(requireContext(), R.color.primary)
        )
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refreshMessages()
        }
    }

    private fun setupObservers() {
        viewModel.messages.observe(viewLifecycleOwner) { messages ->
            Log.d("FeedFragment", "Messages updated: ${messages.size}")
            messageAdapter.submitList(messages)

            if (messages.isEmpty() && viewModel.isLoading.value != true) {
                binding.textEmptyState.visibility = View.VISIBLE
                binding.recyclerMessages.visibility = View.GONE
            } else {
                binding.textEmptyState.visibility = View.GONE
                binding.recyclerMessages.visibility = View.VISIBLE
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.swipeRefresh.isRefreshing = isLoading
            binding.progressBar.visibility = if (isLoading && messageAdapter.itemCount == 0) {
                View.VISIBLE
            } else {
                View.GONE
            }
            binding.fabRefresh.isEnabled = !isLoading
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            error?.let {
                showSnackbar(it, isError = true)
                viewModel.clearErrorMessage()
            }
        }

        viewModel.successMessage.observe(viewLifecycleOwner) { success ->
            success?.let {
                showSnackbar(it, isError = false)
                viewModel.clearSuccessMessage()
            }
        }

        viewModel.networkStatus.observe(viewLifecycleOwner) { status ->
            when (status) {
                NetworkStatus.Available -> {
                    binding.textNetworkStatus.visibility = View.GONE
                }
                NetworkStatus.Unavailable, NetworkStatus.Lost -> {
                    binding.textNetworkStatus.visibility = View.VISIBLE
                    binding.textNetworkStatus.text = getString(R.string.network_disconnected)
                    binding.textNetworkStatus.setTextColor(
                        ContextCompat.getColor(requireContext(), R.color.error)
                    )
                }
                NetworkStatus.Losing -> {
                    binding.textNetworkStatus.visibility = View.VISIBLE
                    binding.textNetworkStatus.text = "Соединение нестабильно"
                    binding.textNetworkStatus.setTextColor(
                        ContextCompat.getColor(requireContext(), R.color.warning)
                    )
                }
            }
        }
    }

    private fun setupListeners() {
        binding.fabRefresh.setOnClickListener {
            Log.d("FeedFragment", "FAB refresh clicked")
            viewModel.refreshMessages()
        }
    }

    private fun showSnackbar(message: String, isError: Boolean) {
        val snackbar = Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT)
        if (isError) {
            snackbar.setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.error))
        }
        snackbar.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("FeedFragment", "onDestroyView")
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("FeedFragment", "onDestroy")
    }
}
