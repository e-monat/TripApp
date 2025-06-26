package com.example.inventory.ui

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.inventory.R
import com.example.inventory.databinding.FragmentSettingsBinding
import com.example.inventory.tools.SharedPrefHelper
import com.google.firebase.auth.FirebaseAuth

class SetingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Mode sombre
        val isNightMode = SharedPrefHelper.isNightMode()
        binding.switchMode.isChecked = isNightMode

        binding.switchMode.setOnCheckedChangeListener { _, isChecked ->
            val mode = if (isChecked) {
                SharedPrefHelper.setNightMode(true)
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                SharedPrefHelper.setNightMode(false)
                AppCompatDelegate.MODE_NIGHT_NO
            }
            AppCompatDelegate.setDefaultNightMode(mode)
        }

        // Déconnexion
        binding.btnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            Toast.makeText(requireContext(), getString(R.string.logout), Toast.LENGTH_SHORT).show()
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            requireActivity().finish()
        }
    }
}


