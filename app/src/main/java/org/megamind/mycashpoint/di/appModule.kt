package org.megamind.mycashpoint.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import org.megamind.mycashpoint.data.data_source.repositoryImpl.AgenceRepositoryImpl
import org.megamind.mycashpoint.data.data_source.repositoryImpl.SoldeRepositoryImpl
import org.megamind.mycashpoint.data.data_source.repositoryImpl.TransactionRepositoryImpl
import org.megamind.mycashpoint.data.data_source.repositoryImpl.UserRepositoryImpl
import org.megamind.mycashpoint.domain.repository.AgenceRepository
import org.megamind.mycashpoint.domain.repository.SoldeRepository
import org.megamind.mycashpoint.domain.repository.TransactionRepository
import org.megamind.mycashpoint.domain.repository.UserRepository
import org.megamind.mycashpoint.ui.Agence.AgenceViewModel
import org.megamind.mycashpoint.ui.screen.auth.RegisterViewModel
import org.megamind.mycashpoint.ui.screen.auth.SignInViewModel
import org.megamind.mycashpoint.ui.screen.caisse.CaisseViewModel
import org.megamind.mycashpoint.ui.screen.main.MainViewModel
import org.megamind.mycashpoint.ui.screen.operateur.OperateurViewModel
import org.megamind.mycashpoint.ui.screen.transaction.TransationViewModel

val appModule = module {


    single<UserRepository> {
        UserRepositoryImpl(get())
    }

    single<SoldeRepository> {
        SoldeRepositoryImpl(get())
    }

    single<TransactionRepository> {
        TransactionRepositoryImpl(get(), get())
    }

    single<AgenceRepository> {
        AgenceRepositoryImpl(get())
    }


    viewModel {
        SignInViewModel(get())
    }

    viewModel {
        RegisterViewModel(get())
    }

    viewModel {
        MainViewModel()
    }

    viewModel {
        OperateurViewModel()
    }
    viewModel {
        TransationViewModel(get())
    }

    viewModel {
        CaisseViewModel(get())
    }


    viewModel {
        AgenceViewModel(get())
    }


}

