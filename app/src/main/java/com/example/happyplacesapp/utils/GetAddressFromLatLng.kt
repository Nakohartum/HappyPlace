package com.example.happyplacesapp.utils

import android.content.Context
import android.location.Geocoder
import android.os.AsyncTask
import com.squareup.okhttp.Address
import java.lang.StringBuilder
import java.util.Locale

class GetAddressFromLatLng(context: Context, private val latitude: Double, private val longitude: Double):
    AsyncTask<Void, String, String>() {
        private val geocoder = Geocoder(context, Locale.getDefault())
    private lateinit var mAddressListener: AddressListener
    override fun doInBackground(vararg params: Void?): String {
        val addressList: List<android.location.Address>? = geocoder.getFromLocation(latitude, longitude, 1)

        if (addressList != null && !addressList.isEmpty()){
            try {
                val address = addressList[0]
                val sb = StringBuilder()
                for (i in 0..address.maxAddressLineIndex){
                    sb.append(address.getAddressLine(i)).append(" ")
                }
                sb.deleteCharAt(sb.length - 1)
                return sb.toString()
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
        return ""
    }

    override fun onPostExecute(result: String?) {
        if (result == null){
            mAddressListener.onError()
        }
        else{
            mAddressListener.onAddressFound(result)
        }
        super.onPostExecute(result)
    }

    fun setAddressListener(addressListener: AddressListener){
        mAddressListener = addressListener
    }

    fun getAddress(){
        execute()
    }

    interface AddressListener{
        fun onAddressFound(address: String?)
        fun onError()
    }

}