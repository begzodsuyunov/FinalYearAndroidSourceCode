package com.example.learningcenter.data

import com.example.learningcenter.R
import com.example.learningcenter.model.Services

object ServicesData {
    fun getServicesData(userName: String): ArrayList<Services> {
        return arrayListOf(
            Services(
                id = 1,
                serviceName = R.string.service1,
                stringResourceId = R.drawable.beginner_removebg_preview,
                servicePrice = "300,000 UZS",
                serviceLink = "https://my.click.uz/services/pay/?service_id=15&merchant_id=20&amount=300000&transaction_param=$userName"
            ),
            Services(
                id = 2,
                serviceName = R.string.service2,
                stringResourceId = R.drawable.elementary_removebg_preview,
                servicePrice = "300,000 UZS",
                serviceLink = "https://my.click.uz/services/pay/?service_id=15&merchant_id=20&amount=300000&transaction_param=$userName"
            ),
            Services(
                id = 3,
                serviceName = R.string.service3,
                stringResourceId = R.drawable.pre_int_removebg_preview,
                servicePrice = "400,000 UZS",
                serviceLink = "https://my.click.uz/services/pay/?service_id=15&merchant_id=20&amount=400000&transaction_param=$userName"
            ),
            Services(
                id = 4,
                serviceName = R.string.service4,
                stringResourceId = R.drawable.intro_removebg_preview,
                servicePrice = "450,000 UZS",
                serviceLink = "https://my.click.uz/services/pay/?service_id=15&merchant_id=20&amount=450000&transaction_param=$userName"
            ),
            Services(
                id = 5,
                serviceName = R.string.service5,
                stringResourceId = R.drawable.pre_removebg_preview,
                servicePrice = "450,000 UZS",
                serviceLink = "https://my.click.uz/services/pay/?service_id=15&merchant_id=20&amount=450000&transaction_param=$userName"
            ),
            Services(
                id = 6,
                serviceName = R.string.service6,
                stringResourceId = R.drawable.foundation_removebg_preview,
                servicePrice = "500,000 UZS",
                serviceLink = "https://my.click.uz/services/pay/?service_id=15&merchant_id=20&amount=500000&transaction_param=$userName"
            ),
            Services(
                id = 7,
                serviceName = R.string.service7,
                stringResourceId = R.drawable.graduration,
                servicePrice = "500,000 UZS",
                serviceLink = "https://my.click.uz/services/pay/?service_id=15&merchant_id=20&amount=500000&transaction_param=$userName"
            )
        )
    }
}