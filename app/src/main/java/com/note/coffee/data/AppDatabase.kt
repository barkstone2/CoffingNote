package com.note.coffee.data

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.note.coffee.data.dao.beans.BeanDao
import com.note.coffee.data.dao.drippers.DripperDao
import com.note.coffee.data.dao.handmills.HandMillDao
import com.note.coffee.data.dao.origin.OriginDao
import com.note.coffee.data.dao.recipes.RecipeDao
import com.note.coffee.data.dao.roastery.RoasteryDao
import com.note.coffee.data.dao.water.WaterDao
import com.note.coffee.data.entity.beans.Bean
import com.note.coffee.data.entity.beans.Origin
import com.note.coffee.data.entity.beans.Roastery
import com.note.coffee.data.entity.drippers.Dripper
import com.note.coffee.data.entity.handmills.HandMill
import com.note.coffee.data.entity.recipes.Recipe
import com.note.coffee.data.entity.water.Water
import com.note.coffee.data.room.converter.RoastDegreeConverter
import com.note.coffee.data.room.converter.StringListConverter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        Bean::class,
        Origin::class,
        Roastery::class,
        HandMill::class,
        Dripper::class,
        Recipe::class,
        Water::class
    ],
    version = 4,
    exportSchema = false
)
@TypeConverters(value = [StringListConverter::class, RoastDegreeConverter::class])
abstract class AppDatabase : RoomDatabase() {
    abstract fun beanDao(): BeanDao
    abstract fun originDao(): OriginDao
    abstract fun roasteryDao(): RoasteryDao
    abstract fun handMillDao(): HandMillDao
    abstract fun dripperDao(): DripperDao
    abstract fun recipeDao(): RecipeDao
    abstract fun waterDao(): WaterDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        val databaseCreated = MutableLiveData(false)

        fun getInstance(context: Context): AppDatabase = INSTANCE ?: Room
            .databaseBuilder(context, AppDatabase::class.java, "coffing_note.db")
            .addMigrations(migrate_1_2, migrate_2_3, migrate_3_4)
            .addCallback(object : Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    CoroutineScope(Dispatchers.IO).launch {
                        insertDefaultOrigins(context)
                        databaseCreated.postValue(true)
                    }
                }
            })
            .build()

        private suspend fun insertDefaultOrigins(context: Context) {
            Log.d("AppDatabase", "init default origins")
            var defaultOrigins = listOf("콜롬비아", "과테말라", "베트남", "케냐", "에티오피아")
            defaultOrigins = defaultOrigins.sorted()
            for ((index, origin) in defaultOrigins.withIndex()) {
                getInstance(context).originDao().insert(Origin(0, origin, (index+1).toLong()))
            }
        }

        private val migrate_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE roastery ADD COLUMN `comment` TEXT")
            }
        }

        private val migrate_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("""
                    CREATE TABLE IF NOT EXISTS `water` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `name` TEXT,
                        `comment` TEXT
                    )
                """.trimIndent())
                database.execSQL("ALTER TABLE recipe ADD COLUMN `waterId` INTEGER")
                database.execSQL("ALTER TABLE recipe ADD COLUMN `waterRatio` REAL")
                database.execSQL("ALTER TABLE recipe ADD COLUMN `beenRatio` REAL")
            }
        }

        private val migrate_3_4 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE bean ADD COLUMN `orderId` INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE recipe ADD COLUMN `orderId` INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE dripper ADD COLUMN `orderId` INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE hand_mill ADD COLUMN `orderId` INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE origin ADD COLUMN `orderId` INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE roastery ADD COLUMN `orderId` INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE water ADD COLUMN `orderId` INTEGER NOT NULL DEFAULT 0")

                database.execSQL("UPDATE bean SET orderId=id")
                database.execSQL("UPDATE recipe SET orderId=id")
                database.execSQL("UPDATE dripper SET orderId=id")
                database.execSQL("UPDATE hand_mill SET orderId=id")
                database.execSQL("UPDATE origin SET orderId=id")
                database.execSQL("UPDATE roastery SET orderId=id")
                database.execSQL("UPDATE water SET orderId=id")
            }
        }

    }

}