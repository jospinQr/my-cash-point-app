package org.megamind.mycashpoint.di

import androidx.room.Room
import org.koin.dsl.module
import org.megamind.mycashpoint.data.data_source.local.AppDatabase
 


val roomModule = module {


    single {
        Room.databaseBuilder(
            context = get(),
            klass = AppDatabase::class.java,
            name = "shop_database"
        )
            .fallbackToDestructiveMigration(false)
            .build()
    }

    single { get<AppDatabase>().userDao() }
    single { get<AppDatabase>().soldeDao() }
    single { get<AppDatabase>().transactionDao() }
    single { get<AppDatabase>().agenceDao() }
    single {get<AppDatabase>().etablissementDao()}


}