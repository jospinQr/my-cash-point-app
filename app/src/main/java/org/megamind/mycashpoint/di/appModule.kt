package org.megamind.mycashpoint.di

import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import org.megamind.mycashpoint.data.repositoryImpl.AgenceRepositoryImpl
import org.megamind.mycashpoint.data.repositoryImpl.CommissionRepositoryImpl
import org.megamind.mycashpoint.data.repositoryImpl.SoldeRepositoryImpl
import org.megamind.mycashpoint.data.repositoryImpl.TransactionRepositoryImpl
import org.megamind.mycashpoint.data.repositoryImpl.UserRepositoryImpl
import org.megamind.mycashpoint.domain.repository.AgenceRepository
import org.megamind.mycashpoint.domain.repository.CommissionRepository
import org.megamind.mycashpoint.domain.repository.SoldeRepository
import org.megamind.mycashpoint.domain.repository.TransactionRepository
import org.megamind.mycashpoint.domain.repository.UserRepository
import org.megamind.mycashpoint.domain.usecase.agence.GetAgencesUseCase
import org.megamind.mycashpoint.domain.usecase.agence.SaveOrUpdateAgenceUseCase
import org.megamind.mycashpoint.domain.usecase.auth.GetUserByIdUseCase
import org.megamind.mycashpoint.domain.usecase.auth.LoginUseCase
import org.megamind.mycashpoint.domain.usecase.auth.RegisterUseCase
import org.megamind.mycashpoint.domain.usecase.commission.DeleteCommissionUseCase
import org.megamind.mycashpoint.domain.usecase.commission.GetAllCommissionsUseCase
import org.megamind.mycashpoint.domain.usecase.commission.GetCommissionStatsParDeviseUseCase
import org.megamind.mycashpoint.domain.usecase.commission.GetCommissionStatsParOperateurUseCase
import org.megamind.mycashpoint.domain.usecase.commission.GetCommissionUseCase
import org.megamind.mycashpoint.domain.usecase.commission.GetCommissionsByOperateurUseCase
import org.megamind.mycashpoint.domain.usecase.commission.SaveOrUpdateCommissionUseCase
import org.megamind.mycashpoint.domain.usecase.commission.SearchCommissionsUseCase
import org.megamind.mycashpoint.domain.usecase.rapport.GetTransactionsByOperatorAndDeviceUseCase
import org.megamind.mycashpoint.domain.usecase.solde.GetSoldeByOperateurEtTypeEtDeviseUseCase
import org.megamind.mycashpoint.domain.usecase.solde.GetSoldeFromServerByCreteriaUseCase
import org.megamind.mycashpoint.domain.usecase.solde.SaveOrUpdateSoldeUseCase
import org.megamind.mycashpoint.domain.usecase.solde.SyncSoldesUseCase

import org.megamind.mycashpoint.domain.usecase.transaction.DeleteTransactionUseCase
import org.megamind.mycashpoint.domain.usecase.transaction.GetTopOperateurUseCase
import org.megamind.mycashpoint.domain.usecase.transaction.InsertTransactionAndUpdateSoldesUseCase
import org.megamind.mycashpoint.domain.usecase.transaction.SendOneTransactToServerUseCase
import org.megamind.mycashpoint.domain.usecase.transaction.SyncTransactionUseCase
import org.megamind.mycashpoint.domain.usecase.transaction.UpdateTransactionUseCase
import org.megamind.mycashpoint.ui.screen.Agence.AgenceViewModel
import org.megamind.mycashpoint.ui.screen.admin.dash_board.DashBoardViewModel
import org.megamind.mycashpoint.ui.screen.auth.RegisterViewModel
import org.megamind.mycashpoint.ui.screen.auth.LoginViewModel
import org.megamind.mycashpoint.ui.screen.caisse.SoldeViewModel
import org.megamind.mycashpoint.ui.screen.main.MainViewModel
import org.megamind.mycashpoint.utils.DataStorageManager
import org.megamind.mycashpoint.ui.screen.operateur.OperateurViewModel
import org.megamind.mycashpoint.ui.screen.rapport.RapportViewModel
import org.megamind.mycashpoint.ui.screen.splash.SplashViewModel
import org.megamind.mycashpoint.ui.screen.transaction.TransactionViewModel

val appModule = module {


    single { DataStorageManager(androidContext()) }


    //repo
    single<UserRepository> {
        UserRepositoryImpl(get(), get(), get())
    }

    single<SoldeRepository> {
        SoldeRepositoryImpl(get(), get())
    }

    single<TransactionRepository> {
        TransactionRepositoryImpl(get(), get(), get())
    }

    single<AgenceRepository> {
        AgenceRepositoryImpl(get())
    }

    single<CommissionRepository> {
        CommissionRepositoryImpl(get())
    }

    // Use cases
    single { GetAgencesUseCase(get()) }
    single { SaveOrUpdateAgenceUseCase(get()) }
    single { GetSoldeByOperateurEtTypeEtDeviseUseCase(get()) }
    single { SaveOrUpdateSoldeUseCase(get()) }
    single { InsertTransactionAndUpdateSoldesUseCase(get()) }
    single { GetTransactionsByOperatorAndDeviceUseCase(get()) }
    single { GetCommissionUseCase(get()) }
    single { GetCommissionsByOperateurUseCase(get()) }
    single { GetAllCommissionsUseCase(get()) }
    single { SaveOrUpdateCommissionUseCase(get()) }
    single { DeleteCommissionUseCase(get()) }
    single { SearchCommissionsUseCase(get()) }
    single { GetCommissionStatsParOperateurUseCase(get()) }
    single { GetCommissionStatsParDeviseUseCase(get()) }
    single { DeleteTransactionUseCase(get()) }
    single { UpdateTransactionUseCase(get()) }
    single { LoginUseCase(get()) }
    single { RegisterUseCase(get()) }
    single { GetUserByIdUseCase(get()) }
    single { SendOneTransactToServerUseCase(get()) }
    single { SyncSoldesUseCase(get()) }
    single { SyncTransactionUseCase(get()) }
    single { GetAgencesUseCase(get()) }
    single { GetSoldeFromServerByCreteriaUseCase(get()) }
    single { GetTopOperateurUseCase(get()) }


//view models
    viewModel {
        LoginViewModel(get())
    }

    viewModel {
        RegisterViewModel(get())
    }

    viewModel {
        MainViewModel(get())
    }

    viewModel {
        OperateurViewModel(get())
    }
    viewModel {
        TransactionViewModel(get(), get())
    }

    viewModel {
        SoldeViewModel(get(), get(), get())
    }


    viewModel {
        AgenceViewModel(get(), get())
    }

    viewModel {
        RapportViewModel(get(), get(), get(), get(), get(), get())
    }

    viewModel {
        SplashViewModel(get())
    }

    viewModel {
        DashBoardViewModel(get(), get(), get())
    }


}

