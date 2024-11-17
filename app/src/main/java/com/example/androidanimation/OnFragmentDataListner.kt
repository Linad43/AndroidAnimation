package com.example.androidanimation

import android.os.Parcelable
import androidx.fragment.app.Fragment

interface OnFragmentDataListner {
    fun onData(array: ArrayList<Buying>, fragment: Fragment)
    fun onData(shop: Shop, fragment: Fragment)
}