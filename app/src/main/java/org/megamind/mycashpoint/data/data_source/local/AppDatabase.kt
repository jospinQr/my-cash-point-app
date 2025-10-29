package org.megamind.mycashpoint.data.data_source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.megamind.mycashpoint.data.data_source.local.dao.AgenceDao
import org.megamind.mycashpoint.data.data_source.local.dao.SoldeDao
import org.megamind.mycashpoint.data.data_source.local.dao.TransactionDao
import org.megamind.mycashpoint.data.data_source.local.dao.UserDao
import org.megamind.mycashpoint.data.data_source.local.entity.Agence
import org.megamind.mycashpoint.data.data_source.local.entity.SoldeEntity
import org.megamind.mycashpoint.data.data_source.local.entity.TransactionEntity
import org.megamind.mycashpoint.data.data_source.local.entity.User
import org.megamind.mycashpoint.utils.EnumConverters


@Database(
    entities = [User::class, SoldeEntity::class, TransactionEntity::class, Agence::class],
    version = 6,
    exportSchema = false
)
@TypeConverters(EnumConverters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun soldeDao(): SoldeDao
    abstract fun transactionDao(): TransactionDao

    abstract fun agenceDao(): AgenceDao

}