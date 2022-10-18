package com.bintangpoetra.sumbanginaja.presentation.auth.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bintangpoetra.sumbanginaja.R
import com.bintangpoetra.sumbanginaja.data.lib.ApiResponse
import com.bintangpoetra.sumbanginaja.databinding.FragmentLoginBinding
import com.bintangpoetra.sumbanginaja.utils.ConstVal.KEY_ADDRESS
import com.bintangpoetra.sumbanginaja.utils.ConstVal.KEY_EMAIL
import com.bintangpoetra.sumbanginaja.utils.ConstVal.KEY_NAME
import com.bintangpoetra.sumbanginaja.utils.ConstVal.KEY_TOKEN
import com.bintangpoetra.sumbanginaja.utils.ConstVal.KEY_USER_NAME
import com.bintangpoetra.sumbanginaja.utils.PreferenceManager
import com.bintangpoetra.sumbanginaja.utils.ext.*
import org.koin.android.ext.android.inject

class LoginFragment : Fragment() {

    private var _fragmentLoginBinding: FragmentLoginBinding? = null
    private val binding get() = _fragmentLoginBinding!!

    private val loginViewModel: LoginViewModel by inject()

    private lateinit var pref: PreferenceManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragmentLoginBinding = FragmentLoginBinding.inflate(inflater, container, false)
        return _fragmentLoginBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pref = PreferenceManager(requireContext())

        initUI()
        initObservers()
        initAction()
    }

    private fun initUI() {
        binding.pbLogin.setAnimation(R.raw.loading_lottie)
    }

    private fun initAction() {
        binding.apply {
            btnLogin.click {
                val email = binding.edtEmail.text.toString()
                val password = binding.edtPassword.text.toString()

                when {
                    email.isEmpty() -> {
                        binding.edtEmail.showError(getString(R.string.error_email_must_not_empty))
                    }
                    password.isEmpty() -> {
                        binding.edtPassword.showError(getString(R.string.error_must_not_empty))
                    }
                    else -> {
                        loginViewModel.loginUser(email, password)
                    }
                }
            }
            btnHaventAccount.click {
                findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            }
        }
    }

    private fun initObservers() {
        loginViewModel.loginUserResult.observe(viewLifecycleOwner) { response ->
            when (response) {
                is ApiResponse.Loading -> {
                    binding.let {
                        showLoading(it.pbLogin, it.viewBgDimmer)
                    }
                }
                is ApiResponse.Success -> {
                    pref.apply {
                        setStringPreference(KEY_NAME, response.data.name)
                        setStringPreference(KEY_USER_NAME, response.data.profileUsers)
                        setStringPreference(KEY_EMAIL, response.data.email)
                        setStringPreference(KEY_ADDRESS, response.data.address)
                        setStringPreference(KEY_TOKEN, response.data.token)
                    }
                    findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                }
                is ApiResponse.Error -> {
                    binding.let {
                        hideLoading(it.pbLogin, it.viewBgDimmer)
                    }
                }
                else -> {
                    binding.let {
                        hideLoading(it.pbLogin, it.viewBgDimmer)
                    }
                }
            }
        }
    }

}