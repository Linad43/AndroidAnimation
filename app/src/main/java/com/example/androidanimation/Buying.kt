package com.example.androidanimation

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
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
import com.example.androidanimation.Buying.CREATOR.TABLE_NAME
import com.example.androidanimation.BuyingDatabase.Companion.EXPORT_SCHEMA
import com.example.androidanimation.BuyingDatabase.Companion.VERSION
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.Serializable

@SuppressLint("ParcelCreator")
@Entity(tableName = TABLE_NAME)
data class Buying(
    @ColumnInfo(name = KEY_NAME) val name: String?,
    @ColumnInfo(name = KEY_IMAGE) val image: Int,
    @ColumnInfo(name = KEY_PRICE) val price: Double,
    @ColumnInfo(name = KEY_COUNT) val count: Int,
) : Serializable, Parcelable {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readInt(),
        parcel.readDouble(),
        parcel.readInt()
    ) {
        id = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeInt(image)
        parcel.writeDouble(price)
        parcel.writeInt(count)
        parcel.writeInt(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Buying> {
        override fun createFromParcel(parcel: Parcel): Buying {
            return Buying(parcel)
        }

        override fun newArray(size: Int): Array<Buying?> {
            return arrayOfNulls(size)
        }

        const val TABLE_NAME = "buying_table"
        const val KEY_NAME = "name"
        const val KEY_IMAGE = "image"
        const val KEY_PRICE = "price"
        const val KEY_COUNT = "count"
    }
}

@Dao
interface BuyingDao {
    @Insert
    suspend fun insert(buying: Buying)

    @Delete
    suspend fun delete(buying: Buying)

    @Query("SELECT * FROM ${Buying.TABLE_NAME} ORDER BY id ASC")
    fun getAllBuying(): LiveData<List<Buying>>

    @Query("DELETE FROM ${Buying.TABLE_NAME}")
    fun deleteAll()
}

@Database(entities = [Buying::class], version = VERSION, exportSchema = EXPORT_SCHEMA)
abstract class BuyingDatabase : RoomDatabase() {
    abstract fun getBuyingDao(): BuyingDao

    companion object {
        const val VERSION = 1
        const val NAME_DATABASE = "buying_database"
        const val EXPORT_SCHEMA = false
        private var INSTANCE: BuyingDatabase? = null
        fun getDatabase(
//            fragment: Fragment,
            context: Context,
        ): BuyingDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
//                    fragment.requireContext().applicationContext,
                    context.applicationContext,
                    BuyingDatabase::class.java,
                    NAME_DATABASE
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

class BuyingRepos(
    private val buyingDao: BuyingDao,
) {
    val buyings: LiveData<List<Buying>> = buyingDao.getAllBuying()
    suspend fun insert(buying: Buying) {
        buyingDao.insert(buying)
    }

    suspend fun delete(buying: Buying) {
        buyingDao.delete(buying)
    }

    fun deleteAll() {
        buyingDao.deleteAll()
    }
}

class BuyingViewModal(
    application: Application,
) : AndroidViewModel(application) {
    private val repos: BuyingRepos
    val buyings: LiveData<List<Buying>>

    init {
        val dao = BuyingDatabase.getDatabase(application).getBuyingDao()
        repos = BuyingRepos(dao)
        buyings = repos.buyings
    }

    fun insertBuying(buying: Buying) = viewModelScope.launch(Dispatchers.IO) {
        repos.insert(buying)
    }

    fun deleteAllBuying() = viewModelScope.launch(Dispatchers.IO) {
        repos.deleteAll()
    }
}