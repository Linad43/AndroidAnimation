package com.example.androidanimation

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import java.io.Serializable

class MainActivity : AppCompatActivity(), OnFragmentDataListner {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        supportFragmentManager
            .beginTransaction()
            .add(R.id.container, ChoiseShopFragment())
            .commit()
    }

    override fun onData(array: ArrayList<Buying>, fragment: Fragment) {
        val bundle = Bundle()
        bundle.putParcelableArrayList(Buying::class.java.simpleName, array)
        val transaction = this.supportFragmentManager.beginTransaction()
        fragment.arguments = bundle
        transaction.add(R.id.container, fragment)
        transaction.addToBackStack("")
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        transaction.commit()
    }

    override fun onData(shop: Shop, fragment: Fragment) {
        val bundle = Bundle()
        bundle.putSerializable(Shop::class.java.simpleName, shop)
        val transaction = this.supportFragmentManager.beginTransaction()
        fragment.arguments = bundle
        transaction.add(R.id.container, fragment)
        transaction.addToBackStack("")
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        transaction.commit()
    }
}