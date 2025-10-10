package org.megamind.mycashpoint.data.data_source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import org.megamind.mycashpoint.data.data_source.local.dao.UserDao
import org.megamind.mycashpoint.data.data_source.local.entity.User


@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao


}