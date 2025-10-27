package org.megamind.mycashpoint.di

import androidx.room.Room
import org.koin.dsl.module
import org.megamind.mycashpoint.data.data_source.local.AppDatabase
import org.megamind.mycashpoint.data.data_source.local.dao.SoldeDao
import org.megamind.mycashpoint.data.data_source.local.dao.TransactionDao
import org.megamind.mycashpoint.data.data_source.local.dao.UserDao


val roomModule = module {



    single {

        Room.databaseBuilder(
            context = get(),
            klass = AppDatabase::class.java,
            name = "shop_database"
        ).build()


    }

    single { get<AppDatabase>().userDao() }
    single { get<AppDatabase>().soldeDao() }
    single { get<AppDatabase>().transactionDao() }


}