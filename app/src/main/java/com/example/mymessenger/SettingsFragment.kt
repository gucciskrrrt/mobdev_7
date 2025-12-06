package com.example.mymessenger

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.mymessenger.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("SettingsFragment", "onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("SettingsFragment", "onCreateView")
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("SettingsFragment", "onViewCreated")

        setupObservers()
        setupListeners()

        // Инициализируем состояние из текущей темы
        val currentMode = AppCompatDelegate.getDefaultNightMode()
        val isDark = currentMode == AppCompatDelegate.MODE_NIGHT_YES
        viewModel.setDarkTheme(isDark)
    }

    private fun setupObservers() {
        // Наблюдаем за изменениями темы
        viewModel.isDarkTheme.observe(viewLifecycleOwner) { isDark ->
            Log.d("SettingsFragment", "isDarkTheme changed: $isDark")

            // Обновляем UI без триггера слушателя
            binding.themeSwitch.setOnCheckedChangeListener(null)
            binding.themeSwitch.isChecked = isDark
            binding.themeSwitch.setOnCheckedChangeListener { _, isChecked ->
                onThemeChanged(isChecked)
            }
        }
    }

    private fun setupListeners() {
        binding.themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            onThemeChanged(isChecked)
        }
    }

    private fun onThemeChanged(isDark: Boolean) {
        Log.d("SettingsFragment", "Theme switch changed: $isDark")

        // Сохраняем в ViewModel
        viewModel.setDarkTheme(isDark)

        // Применяем тему
        if (isDark) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("SettingsFragment", "onDestroyView")
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("SettingsFragment", "onDestroy")
    }
}

