package com.example.ecom

import android.content.Intent
import android.os.Bundle
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.ecom.database.AppDatabase
import com.example.ecom.model.Product
import com.example.ecom.repos.ProductRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_main.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class MainFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_main, container, false)

//        doAsync {
//            val json = URL("https://finepointmobile.com/data/products.json").readText()
//
//            uiThread {
//                val products = Gson().fromJson(json, Array<Product>::class.java).toList()
//
//                root.recycler_view.apply {
//                    layoutManager = GridLayoutManager(activity, 2)
//                    adapter = ProductsAdapter(products)
//                    root.progressBar2.visibility = View.GONE
//                }
//            }
//        }

        //root.progressBar2.visibility = View.GONE

        val categories = listOf("Jeans", "Jacket", "Dress", "tShirt", "shirt", "socks", "Jeans", "Jacket", "Dress")

        root.categoriesRecyclerView.apply {
            layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
            adapter = CategoriesAdapter(categories)
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val productsRepository = ProductRepository().getAllProducts()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                d("budi","success :)")
                recycler_view.apply {
                        layoutManager = GridLayoutManager(activity, 2)
                        adapter = ProductsAdapter(it){
                            val intent = Intent(parent.context, ProductDetails::class.java)
                            intent.putExtra("title", products[holder.adapterPosition].title)
                            intent.putExtra("photo_url", products[holder.adapterPosition].photoUrl)
                            parent.context.startActivity(intent)
                        }
                    }
                    progressBar2.visibility = View.GONE
            },{
                d("budi","error :( ${it.message}")
            })

//        searchButton.setOnClickListener{
//            doAsync {
//                val db = Room.databaseBuilder(
//                    activity!!.applicationContext,
//                    AppDatabase::class.java, "database-name"
//                ).build()
//
//                val productsFromDatabase = db.productDao().searchFor("%${searchTerm.text}%")
//                val products = productsFromDatabase.map {
//                    Product(
//                        it.title,
//                        "https://finepointmobile.com/data/jeans1.jpg",
//                        it.price,
//                        isOnSale = true
//                    )
//                }
//
//                uiThread {
//                    recycler_view.apply {
//                        layoutManager = GridLayoutManager(activity, 2)
//                        adapter = ProductsAdapter(products)
//                    }
//                    progressBar2.visibility = View.GONE
//                }
//            }
//        }


    }

}