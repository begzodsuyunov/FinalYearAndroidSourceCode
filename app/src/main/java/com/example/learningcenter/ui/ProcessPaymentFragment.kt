package com.example.learningcenter.ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import android.widget.RelativeLayout
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import com.example.learningcenter.R
import com.example.learningcenter.databinding.FragmentPaymentBinding
import com.example.learningcenter.databinding.FragmentProcessPaymentBinding
import com.example.learningcenter.ui.viewmodel.ServicesViewModel

private lateinit var internetLayout: WebView
private lateinit var noInternetLayout: RelativeLayout

class ProcessPaymentFragment : Fragment() {
    private var _binding: FragmentProcessPaymentBinding? = null
    private val binding get() = _binding!!

    private val servicesViewModel: ServicesViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProcessPaymentBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        servicesViewModel.currentService.observe(this.viewLifecycleOwner){
            binding.webviewPay.loadUrl(it.serviceLink)
            internetLayout = binding.webviewPay
            noInternetLayout = binding.noInternetLayoutPay

            drawLayout()
            val wv: WebView = view.findViewById(R.id.webviewPay)

            //https://my.click.uz/services/pay/?service_id=${it.merchantServiceId}&merchant_id=${it.merchantId}&amount=${it.merchantTransAmount}&transaction_param=${it.merchantTransId}
            //wv.loadUrl("https://my.click.uz/services/pay/?service_id=15&merchant_id=20&amount=1000&transaction_param=student_login_info")
            wv.webViewClient = Client()
            val ws: WebSettings = wv.settings

            ws.domStorageEnabled = true
            ws.javaScriptEnabled = true;
            wv.settings.javaScriptCanOpenWindowsAutomatically = true;
            wv.clearCache(true);
            wv.clearHistory();
        }


    }

    private class Client : WebViewClient() {

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
        }

        @Deprecated("Deprecated in Java")
        override fun shouldOverrideUrlLoading(
            view: WebView?, url: String?
        ): Boolean {
            view?.loadUrl(url!!)
            return true
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
        }

        override fun onReceivedError(
            view: WebView?,
            request: WebResourceRequest?,
            error: WebResourceError?
        ) {
            super.onReceivedError(view, request, error)

            try {
                view?.stopLoading()
                noInternetLayout.visibility = View.VISIBLE
            } catch (e: Exception) {

            }

            if (view?.canGoBack() == true) {
                view.goBack()
            }

            view?.loadUrl("about:blank")

            super.onReceivedError(view, request, error)
        }

    }


    private fun isNetworkAvailable(): Boolean {
        val cm = this.context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = cm.getNetworkCapabilities(cm.activeNetwork)

        return (capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET))

    }

    private fun drawLayout() {
        if (isNetworkAvailable()) {
            noInternetLayout.visibility = View.GONE
        } else {
            noInternetLayout.visibility = View.VISIBLE
        }
    }
}