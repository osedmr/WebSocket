package com.galerkinrobotics.test.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.galerkinrobotics.test.R
import com.galerkinrobotics.test.databinding.FragmentLoginBinding
import com.galerkinrobotics.test.ui.viewmodels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.button.setOnClickListener {
            viewModel.authenticate()
        }

        viewModel.authResult.observe(viewLifecycleOwner) { response ->
            Log.d("AuthResult", "Gelen yanıt: $response")
            Toast.makeText(requireContext(), "Hoşgeldin ${response.params.first()}", Toast.LENGTH_SHORT).show()
        }
    }

}