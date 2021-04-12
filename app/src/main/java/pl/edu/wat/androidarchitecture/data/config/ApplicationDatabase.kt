package pl.edu.wat.androidarchitecture.data.config

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import pl.edu.wat.androidarchitecture.data.repository.local.SheetDao
import pl.edu.wat.androidarchitecture.model.entity.Expense
import pl.edu.wat.androidarchitecture.model.entity.Income
import pl.edu.wat.androidarchitecture.model.entity.Sheet

@Database(
    entities = [Sheet::class, Income::class, Expense::class],
    version = 24,
    exportSchema = false
)
abstract class ApplicationDatabase : RoomDatabase() {

    abstract fun sheetDao(): SheetDao

    companion object {
        @Volatile
        private var instance: ApplicationDatabase? = null

        fun getDatabase(context: Context): ApplicationDatabase =
            instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also {
                    instance = it
                }
            }

        private fun buildDatabase(appContext: Context) =
            Room.databaseBuilder(appContext, ApplicationDatabase::class.java, "androidArchitectureDb")
                .fallbackToDestructiveMigration()
                .build()
    }

}