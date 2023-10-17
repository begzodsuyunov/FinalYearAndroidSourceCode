package com.example.learningcenter.ui

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.learningcenter.appconfig.AppConfig
import com.example.learningcenter.databinding.FragmentLoginBinding
import com.google.android.material.textfield.TextInputEditText
import com.vishnusivadas.advanced_httpurlconnection.PutData


class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var textInputEditTextUserPhone: TextInputEditText
    private lateinit var textInputEditTextPassword: TextInputEditText
    private lateinit var buttonLogin: Button
    private lateinit var progressBar: ProgressBar
    private var isRememberLogin = false
    private lateinit var appConfig: AppConfig

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

        appConfig = AppConfig(this.requireContext())
        if (appConfig.isUserLogin()) {
            val action = LoginFragmentDirections.actionLoginFragmentToHomePageFragment()
            findNavController().navigate(action)
        }
        textInputEditTextUserPhone = binding.username
        textInputEditTextPassword = binding.password
        buttonLogin = binding.btnLogin
        progressBar = binding.progress

        binding.cbxRemember.setOnClickListener{
            isRememberLogin = binding.cbxRemember.isChecked
        }

        buttonLogin.setOnClickListener {
            val username: String
            val password: String
            username = textInputEditTextUserPhone.text.toString()
            password = textInputEditTextPassword.text.toString()
            if (username != "" && password != "") {
                progressBar.visibility = View.VISIBLE
                val handler = Handler()
                handler.post {
                    //Starting Write and Read data with URL
                    //Creating array for parameters
                    val field = arrayOfNulls<String>(2)
                    field[0] = "username"
                    field[1] = "password"
                    //Creating array for data
                    val data = arrayOfNulls<String>(2)
                    data[0] = username
                    data[1] = password
                    val putData = PutData(
                        "https://everestlearningcenter.000webhostapp.com/login_page/login.php",
                        "POST",
                        field,
                        data
                    )
                    if (putData.startPut()) {
                        if (putData.onComplete()) {
                            progressBar.visibility = View.GONE
                            val result = putData.result
                            //End ProgressBar (Set visibility to GONE)
                            if (result == "Login Success") {
                                if (isRememberLogin) {
                                    appConfig.updateUserLoginStatus(true)
                                    appConfig.saveNameofUser(username)
                                }
                                Toast.makeText(this.context, result, Toast.LENGTH_SHORT)
                                    .show()
                                val action =
                                    LoginFragmentDirections.actionLoginFragmentToHomePageFragment()
                                findNavController().navigate(action)
                            } else {
                                Toast.makeText(this.context, result, Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    }
                    //End Write and Read data with URL
                }
            }
        }

    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
    }
}