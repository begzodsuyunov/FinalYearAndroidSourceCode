package com.example.learningcenter.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.learningcenter.BaseApplication
import com.example.learningcenter.R
import com.example.learningcenter.appconfig.AppConfig
import com.example.learningcenter.databinding.FragmentHomePageBinding
import com.example.learningcenter.ui.viewmodel.WordViewModel
import com.example.learningcenter.ui.viewmodel.WordViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class HomePageFragment : Fragment() {

    private var _binding: FragmentHomePageBinding? = null

    private lateinit var appConfig: AppConfig
    private lateinit var btnlogout: ImageButton

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val viewModel: WordViewModel by activityViewModels {
        WordViewModelFactory(
            (activity?.application as BaseApplication).database.wordDao()
        )
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomePageBinding.inflate(inflater, container, false)
        val view = binding.root
        // Inflate the layout for this fragment
        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.dictionary.setOnClickListener {
            val action = HomePageFragmentDirections.actionHomePageFragmentToWordListFragment()
            findNavController().navigate(action)
        }

        binding.unscrambledApp.setOnClickListener {
            if (viewModel.isEnoughWords()) {
                showConfirmationDialog()
            } else {
                    val action =
                        HomePageFragmentDirections.actionHomePageFragmentToUnscrambledAppFragment()
                    findNavController().navigate(action)
            }
        }

        binding.fileSharing.setOnClickListener {
            val action =
                HomePageFragmentDirections.actionHomePageFragmentToFileSharingFragment()
            findNavController().navigate(action)
        }
        binding.payment.setOnClickListener {
            val action =
                HomePageFragmentDirections.actionHomePageFragmentToPaymentFragment()
            findNavController().navigate(action)
        }
        binding.videochat.setOnClickListener {
            val action =
                HomePageFragmentDirections.actionHomePageFragmentToVideoCallFragment()
            findNavController().navigate(action)
        }

        appConfig =AppConfig(this.requireContext())
        btnlogout = binding.btnLogout
        btnlogout.setOnClickListener {
            appConfig.updateUserLoginStatus(false)
            val action = HomePageFragmentDirections.actionHomePageFragmentToLoginFragment()
            findNavController().navigate(action)
        }

    }
    private fun showConfirmationDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(android.R.string.dialog_alert_title))
            .setMessage("Please make sure that you have at least 20 words in Dictionary")
            .setCancelable(false)
            .setNegativeButton(getString(R.string.no)) { _, _ -> }
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                    val action =
                        HomePageFragmentDirections.actionHomePageFragmentToUnscrambledAppFragment()
                    findNavController().navigate(action)
            }
            .show()
    }


}