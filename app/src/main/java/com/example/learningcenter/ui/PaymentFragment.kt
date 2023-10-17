package com.example.learningcenter.ui

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.learningcenter.appconfig.AppConfig
import com.example.learningcenter.data.ServicesData
import com.example.learningcenter.databinding.FragmentPaymentBinding
import com.example.learningcenter.ui.viewmodel.ServicesViewModel



class PaymentFragment : Fragment() {
    private var _binding: FragmentPaymentBinding? = null
    private val binding get() = _binding!!
    private val servicesViewModel: ServicesViewModel by activityViewModels()
    private lateinit var appConfig: AppConfig


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPaymentBinding.inflate(inflater, container, false)
        return binding.root
    }

    //https://my.click.uz/services/pay/?service_id=1&merchant_id=1&amount=1000&transaction_param=order_id_in_your_server


    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        appConfig = AppConfig(this.requireContext())

        servicesViewModel._serviceData = ServicesData.getServicesData(appConfig.getNameOfUser().toString())
        servicesViewModel._currentService.value = servicesViewModel._serviceData[0]

        val adapter = ServicesAdapter {
            servicesViewModel.updateCurrentService(it)
            val action = PaymentFragmentDirections.actionPaymentFragmentToProcessPaymentFragment()
            this.findNavController().navigate(action)
        }
        binding.recyclerView.adapter = adapter
        adapter.submitList(servicesViewModel.serviceData)

    }

}