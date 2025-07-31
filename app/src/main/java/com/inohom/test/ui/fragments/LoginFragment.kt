package com.inohom.test.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.inohom.test.R
import com.inohom.test.databinding.FragmentLoginBinding
import com.inohom.test.ui.viewmodels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
            Toast.makeText(requireContext(), "Giriş Yapılıyor...", Toast.LENGTH_SHORT).show()
            lifecycleScope.launch {
                delay(2000)
                findNavController().navigate(R.id.action_loginFragment_to_inohomFragment)
            }
        }

        viewModel.authResult.observe(viewLifecycleOwner) { response ->
            Log.d("AuthResult", "Gelen yanıt: $response")
            Toast.makeText(requireContext(), "Hoşgeldin ${response.params.first()}", Toast.LENGTH_SHORT).show()
        }
    }

}
