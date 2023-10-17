package com.example.learningcenter.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.learningcenter.model.Services

class ServicesViewModel : ViewModel() {
    internal var _currentService: MutableLiveData<Services> = MutableLiveData<Services>()
    val currentService: LiveData<Services>
        get() = _currentService

    internal var _serviceData: ArrayList<Services> = ArrayList()
    val serviceData: ArrayList<Services>
        get() = _serviceData

    fun updateCurrentService(services: Services) {
        _currentService.value = services
    }
}