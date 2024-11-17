package com.example.androidanimation

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ShoppingFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shopping, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        val db = BuyingViewModal(activity?.application!!)
        val arrayBuying = arguments?.getParcelableArrayList<Buying>(Buying::class.java.simpleName) as ArrayList<Buying>
        val recyclerView=view.findViewById<RecyclerView>(R.id.recyclerView)
        val adapter = RecyclerViewAdapterProducts(requireContext())
        var sum = 0.0
        val products = mutableListOf<Product>()
        val sumTV = view.findViewById<TextView>(R.id.sumTV)
        arrayBuying.forEach {
            products.add(
                Product(
                    "${it.name} (${it.count})",
                    it.image,
                    it.price
                )
            )
            sum += it.count*it.price
        }
        adapter.updateList(products)
        recyclerView.layoutManager = LinearLayoutManager(this.requireContext())
        recyclerView.adapter = adapter
        sumTV.text = "Итого по чеку $sum"
    }
}