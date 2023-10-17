package com.example.learningcenter.ui

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.content.Context.DOWNLOAD_SERVICE
import android.graphics.Bitmap
import android.media.Image
import android.net.ConnectivityManager
import android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.webkit.*
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.getSystemService
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import com.example.learningcenter.R
import com.example.learningcenter.databinding.FragmentFileSharingBinding
import com.example.learningcenter.ui.viewmodel.WordViewModel

private lateinit var internetLayout: WebView
private lateinit var noInternetLayout: RelativeLayout

class FileSharingFragment : Fragment() {
    private var _binding: FragmentFileSharingBinding? = null

    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFileSharingBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        internetLayout = binding.webview
        noInternetLayout = binding.noInternetLayout

        drawLayout()


        val wv: WebView = view.findViewById(R.id.webview)

        wv.loadUrl("https://drive.google.com/drive/folders/1vMvdcUo0VpUoZPb_56FKozrzcEO3L7hx?usp=share_link")
        wv.webViewClient = Client()
        val ws: WebSettings = wv.settings

        ws.javaScriptEnabled = true;
        wv.settings.javaScriptCanOpenWindowsAutomatically = true;
        wv.clearCache(true);
        wv.clearHistory();


        wv.setDownloadListener { url, userAgent, contentDisposition, mimeType, contentLength ->
            val request = DownloadManager.Request(Uri.parse(url))
            request.setMimeType(mimeType)
            request.addRequestHeader("cookie", CookieManager.getInstance().getCookie(url))
            request.addRequestHeader("User-Agent", userAgent)
            request.setDescription("Downloading file...")
            request.setTitle(URLUtil.guessFileName(url, contentDisposition, mimeType))
            request.allowScanningByMediaScanner()
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            request.setDestinationInExternalFilesDir(
                this.context,
                Environment.DIRECTORY_DOWNLOADS, ""
            )
            val dm = this.context?.getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            dm.enqueue(request)
            Toast.makeText(this.context, "Downloading File", Toast.LENGTH_LONG).show()
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
                noInternetLayout.visibility = VISIBLE
            } catch (e: Exception){

            }

            if(view?.canGoBack() == true){
                view.goBack()
            }

            view?.loadUrl("about:blank")

            super.onReceivedError(view, request, error)
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val cm = this.context?.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = cm.getNetworkCapabilities(cm.activeNetwork)

        return (capabilities != null && capabilities.hasCapability(NET_CAPABILITY_INTERNET))

    }

    private fun drawLayout() {
        if (isNetworkAvailable()) {
            noInternetLayout.visibility = GONE
        } else {
            noInternetLayout.visibility = VISIBLE
        }
    }




}