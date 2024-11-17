package com.example.androidanimation

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewAdapterProducts(
    private val context: Context,
) : RecyclerView.Adapter<RecyclerViewAdapterProducts.RecyclerViewHolder>() {
    private var list = mutableListOf<Product>()
    private var onItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(product: Product, position: Int)
    }

    inner class RecyclerViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {
        private val nameTV = view.findViewById<TextView>(R.id.textTV)
        private val priceTV = view.findViewById<TextView>(R.id.priceTV)
        private val image = view.findViewById<ImageView>(R.id.imageIV)

        @SuppressLint("SetTextI18n")
        fun bind(product: Product) {
            nameTV.text = product.name
            priceTV.text = product.price.toString()
            image.setImageResource(product.image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val viewHolder =
            RecyclerViewHolder(
                LayoutInflater
                    .from(context)
                    .inflate(
                        R.layout.grid_view_item_products,
                        parent,
                        false
                    )
            )
        return viewHolder
    }

    //
//    override fun onBindViewHolder(
//        holder: RecyclerViewHolder,
//        position: Int,
//        payloads: MutableList<Any>) {
//        val product = list[position]
//    }
    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val currentProduct = list[position]
        holder.bind(currentProduct)
        holder.itemView.setOnClickListener {
            if (onItemClickListener != null) {
                onItemClickListener!!.onItemClick(currentProduct, position)
            }
        }
    }

    interface ProductClickListener {
        fun onItemClicked(product: Product)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newList: List<Product>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

    fun setOnItemClickLictener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }
}