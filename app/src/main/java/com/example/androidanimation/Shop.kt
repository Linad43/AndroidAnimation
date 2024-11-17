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
import com.example.androidanimation.Shop.Companion.TABLE_NAME
import com.example.androidanimation.ShopDatabase.Companion.EXPORT_SCHEMA
import com.example.androidanimation.ShopDatabase.Companion.VERSION
import java.io.Serializable

@Entity(tableName = TABLE_NAME)
data class Shop(
    @ColumnInfo(name = KEY_NAME) val name: String,
    @ColumnInfo(name = KEY_IMAGE) val image: Int,
) : Serializable {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    companion object {
        const val TABLE_NAME = "shop_table"
        const val KEY_NAME = "name"
        const val KEY_IMAGE = "image"
    }
}

@Dao
interface ShopDao {
    @Insert
    suspend fun insert(shop: Shop)

    @Delete
    suspend fun delete(shop: Shop)

    @Query("SELECT * FROM ${Shop.TABLE_NAME} ORDER BY id ASC")
    fun getAllShops(): LiveData<List<Shop>>

    @Query("DELETE FROM ${Shop.TABLE_NAME}")
    fun deleteAll()
}

@Database(entities = [Shop::class], version = VERSION, exportSchema = EXPORT_SCHEMA)
abstract class ShopDatabase : RoomDatabase() {
    abstract fun getShopDao(): ShopDao

    companion object {
        const val VERSION = 1
        const val NAME_DATABASE = "shop_database"
        const val EXPORT_SCHEMA = false
        private var INSTANCE: ShopDatabase? = null
        fun getDatabase(
//            fragment: Fragment,
            context: Context,
        ): ShopDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
//                    fragment.requireContext().applicationContext,
                    context.applicationContext,
                    ShopDatabase::class.java,
                    NAME_DATABASE
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

class ShopRepos(
    private val shopDao: ShopDao,
) {
    val shops: LiveData<List<Shop>> = shopDao.getAllShops()
    suspend fun insert(shop: Shop) {
        shopDao.insert(shop)
    }

    suspend fun delete(shop: Shop) {
        shopDao.delete(shop)
    }

    fun deleteAll() {
        shopDao.deleteAll()
    }
}
class ShopViewModal(
    application: Application
):AndroidViewModel(application){
    private val repos: ShopRepos
    val shops:LiveData<List<Shop>>
    init {
        val dao=ShopDatabase.getDatabase(application).getShopDao()
        repos= ShopRepos(dao)
        shops=repos.shops
    }
}