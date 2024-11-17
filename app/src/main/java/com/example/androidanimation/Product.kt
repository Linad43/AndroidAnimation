package com.example.androidanimation

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.androidanimation.Product.Companion.TABLE_NAME
import com.example.androidanimation.ProductDatabase.Companion.EXPORT_SCHEMA
import com.example.androidanimation.ProductDatabase.Companion.VERSION
import java.io.Serializable

@Entity(tableName = TABLE_NAME)
data class Product(
    @ColumnInfo(name = KEY_NAME) val name: String,
    @ColumnInfo(name = KEY_IMAGE) val image: Int,
    @ColumnInfo(name = KEY_PRICE) val price: Double,
) : Serializable {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    companion object {
        const val TABLE_NAME = "shop_table"
        const val KEY_NAME = "name"
        const val KEY_IMAGE = "image"
        const val KEY_PRICE = "price"
    }
}

@Dao
interface ProductDao {
    @Insert
    suspend fun insert(product: Product)

    @Delete
    suspend fun delete(product: Product)

    @Query("SELECT * FROM ${Product.TABLE_NAME} ORDER BY id ASC")
    fun getAllProducts(): LiveData<List<Product>>

    @Query("DELETE FROM ${Product.TABLE_NAME}")
    fun deleteAll()
}

@Database(entities = [Product::class], version = VERSION, exportSchema = EXPORT_SCHEMA)
abstract class ProductDatabase : RoomDatabase() {
    abstract fun getProductDao(): ProductDao

    companion object {
        const val VERSION = 1
        const val NAME_DATABASE = "product_database"
        const val EXPORT_SCHEMA = false
        private var INSTANCE: ProductDatabase? = null
        fun getDatabase(
//            fragment: Fragment,
            context: Context,
        ): ProductDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
//                    fragment.requireContext().applicationContext,
                    context.applicationContext,
                    ProductDatabase::class.java,
                    NAME_DATABASE
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

class ProductRepos(
    private val productDao: ProductDao,
) {
    val products: LiveData<List<Product>> = productDao.getAllProducts()
    suspend fun insert(product: Product) {
        productDao.insert(product)
    }

    suspend fun delete(product: Product) {
        productDao.delete(product)
    }

    fun deleteAll() {
        productDao.deleteAll()
    }
}

class ProductViewModal(
    application: Application,
) : AndroidViewModel(application) {
    private val repos: ProductRepos
    val products: LiveData<List<Product>>

    init {
        val dao = ProductDatabase.getDatabase(application).getProductDao()
        repos = ProductRepos(dao)
        products = repos.products
    }
}