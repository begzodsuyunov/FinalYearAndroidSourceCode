package com.example.learningcenter.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.example.learningcenter.databinding.FragmentVideoCallBinding
import us.zoom.sdk.*


class VideoCallFragment : Fragment() {

    private var _binding: FragmentVideoCallBinding? = null
    private val binding get() = _binding!!

    private lateinit var meetingName: EditText
    private lateinit var meetingId: EditText
    private lateinit var meetingPassword: EditText
    private lateinit var joinButton: Button


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentVideoCallBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        meetingName = binding.meetingName
        meetingId = binding.meetingId
        meetingPassword = binding.meetingPassword
        joinButton = binding.joinButton

        initializeZoomSdk(this.context)
        joinButton.setOnClickListener{
            val name1: String = meetingName.text.toString().filter { !it.isWhitespace() }
            val meetingId1: String = meetingId.text.toString().filter { !it.isWhitespace() }
            val password1: String = meetingPassword.text.toString().filter { !it.isWhitespace() }
            if(name1.isNotBlank() && meetingId1.isNotBlank() && password1.isNotBlank()){
                startMeeting(name1, meetingId1, password1, this.context)
            }
            println(meetingId1)
        }
    }

    private fun startMeeting(name1: String, meetingId1: String, password1: String, context: Context?) {
        val meetingService = ZoomSDK.getInstance().meetingService
        val options = JoinMeetingOptions()
        val params = JoinMeetingParams().apply {
            displayName = name1
            meetingNo = meetingId1
            password = password1
        }
        meetingService.joinMeetingWithParams(context, params, options)

    }

    private fun initializeZoomSdk(context: Context?) {
        val sdk = ZoomSDK.getInstance()
        val params = ZoomSDKInitParams()
        params.appKey = "Fp9kDuPybNP0WkIzqd2XoBMZAyQPo3qAmUhY"
        params.appSecret = "bmbyYNoh37YI1inPTN3eXqAwVpRtMicKK7A6"
        params.domain = "zoom.us"
        params.enableLog = false

        val zoomSDKInitializeListener = object : ZoomSDKInitializeListener {
            override fun onZoomSDKInitializeResult(p0: Int, p1: Int) = Unit

            override fun onZoomAuthIdentityExpired() = Unit
        }
        sdk.initialize(context, zoomSDKInitializeListener, params)
    }
}