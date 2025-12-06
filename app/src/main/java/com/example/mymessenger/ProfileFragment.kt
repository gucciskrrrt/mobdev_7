package com.example.mymessenger

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.mymessenger.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("ProfileFragment", "onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("ProfileFragment", "onCreateView")
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("ProfileFragment", "onViewCreated")

        setupObservers()
        setupListeners()
    }

    private fun setupObservers() {
        // Наблюдаем за изменениями имени пользователя
        viewModel.userName.observe(viewLifecycleOwner) { name ->
            Log.d("ProfileFragment", "userName changed: $name")
            binding.profileName.text = name
            binding.editName.setText(name)
        }

        // Наблюдаем за изменениями статуса
        viewModel.userStatus.observe(viewLifecycleOwner) { status ->
            Log.d("ProfileFragment", "userStatus changed: $status")
            binding.editStatus.setText(status)
        }
    }

    private fun setupListeners() {
        // Кнопка сохранения
        binding.saveButton.setOnClickListener {
            val name = binding.editName.text.toString()
            val status = binding.editStatus.text.toString()

            Log.d("ProfileFragment", "Save clicked - name: $name, status: $status")

            viewModel.updateUserName(name)
            viewModel.updateUserStatus(status)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("ProfileFragment", "onDestroyView")
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("ProfileFragment", "onDestroy")
    }
}

