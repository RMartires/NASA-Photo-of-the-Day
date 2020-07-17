package com.example.nasaphotooftheday.POD
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE

@Database(entities = [POD::class], version = 1)
abstract class PODDatabase: RoomDatabase() {
    abstract fun podDao(): PODDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: PODDatabase? = null

        fun getDatabase(context: Context): PODDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PODDatabase::class.java,
                    "POD"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }

}


@Dao
interface PODDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(pod: POD)

    @Query("SELECT * FROM pod")
    fun load():LiveData<List<POD>>

}
