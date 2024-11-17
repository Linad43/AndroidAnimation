package com.example.androidanimation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.GridView
import androidx.fragment.app.Fragment

class ChoiseShopFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_choise_shop, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val gridView = view.findViewById<GridView>(R.id.gridView)
        val listShops = listOf(
            Shop("First", R.drawable.ic_launcher_foreground),
            Shop("Second", R.drawable.ic_launcher_foreground)
        )
        val adapter = GridViewAdapterShops(listShops, requireContext())
        gridView.adapter = adapter
        val onFragmentDataListner = requireActivity() as OnFragmentDataListner
        gridView.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                onFragmentDataListner.onData(listShops[position], ShopMenuFragment())
            }
    }
}