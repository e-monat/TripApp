package com.example.inventory.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.inventory.R
import com.example.inventory.databinding.ActivityForgotPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.btnResetPassword.setOnClickListener {
            val email = binding.inputEmail.text.toString().trim()

            if (email.isEmpty()) {
                Toast.makeText(
                    this,
                    getString(R.string.error_email_required),
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            auth.sendPasswordResetEmail(email)
                .addOnSuccessListener {
                    Toast.makeText(
                        this,
                        getString(R.string.reset_password),
                        Toast.LENGTH_LONG
                    ).show()
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(
                        this,
                        "${getString(R.string.error_auth_failed)}: ${it.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
        }
    }
}

