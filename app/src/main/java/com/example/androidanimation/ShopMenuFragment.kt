package com.example.androidanimation

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidanimation.RecyclerViewAdapterProducts.ProductClickListener
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ShopMenuFragment : Fragment(), ProductClickListener {
    private var buying = mutableMapOf<Product, Int>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_shop_menu, container, false)
    }

    @SuppressLint("NewApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val shop = arguments?.getSerializable(Shop::class.java.simpleName, Shop::class.java)!!
        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = "Магазин ${shop.name}"
//        val db = ProductViewModal(activity?.application!!)
        var products = mutableListOf<Product>()
//        products = db.products.value!!.toMutableList()
        if (products.isEmpty()) {
            products.add(
                Product(
                    "first",
                    R.drawable.ic_launcher_foreground,
                    10.0
                )
            )
            products.add(
                Product(
                    "second",
                    R.drawable.ic_launcher_foreground,
                    20.0
                )
            )
        }
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        val adapter = RecyclerViewAdapterProducts(requireContext())
        adapter.updateList(products)
        recyclerView
            .startAnimation(
                AnimationUtils
                    .loadAnimation(
                        context,
                        R.anim.move_rigth_to_center
                    )
            )
        recyclerView.layoutManager = LinearLayoutManager(this.requireContext())
        recyclerView.adapter = adapter

        val button = view.findViewById<FloatingActionButton>(R.id.button)
        button.animate().apply {
            duration = 1000
            rotation(360f)
        }
        val onFragmentDataListner = requireActivity() as OnFragmentDataListner
        button.setOnClickListener {
            val array = arrayListOf<Buying>()
            buying.forEach {
                array.add(
                    Buying(
                        it.key.name,
                        it.key.image,
                        it.key.price,
                        it.value
                    )
                )
            }
            onFragmentDataListner.onData(array, ShoppingFragment())
        }
        adapter.setOnItemClickLictener(object :
            RecyclerViewAdapterProducts.OnItemClickListener {
            override fun onItemClick(product: Product, position: Int) {
                addBuyind(product)
            }
        })


    }

    fun addBuyind(product: Product) {
        if (buying.containsKey(product)) {
            buying[product] = buying[product]!! + 1
        } else {
            buying[product] = 1
        }
        Toast.makeText(
            requireContext(),
            "${product.name} добавлен в корзину",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onItemClicked(product: Product) {
        TODO("Not yet implemented")
    }
}