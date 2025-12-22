package org.megamind.mycashpoint.di

import io.ktor.utils.io.core.Sink
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import org.megamind.mycashpoint.data.repositoryImpl.AgenceRepositoryImpl
import org.megamind.mycashpoint.data.repositoryImpl.AnalyticsRepositryImpl
import org.megamind.mycashpoint.data.repositoryImpl.CommissionRepositoryImpl
import org.megamind.mycashpoint.data.repositoryImpl.EtablissementRepositoryImpl
import org.megamind.mycashpoint.data.repositoryImpl.SoldeRepositoryImpl
import org.megamind.mycashpoint.data.repositoryImpl.TransactionRepositoryImpl
import org.megamind.mycashpoint.data.repositoryImpl.UserRepositoryImpl
import org.megamind.mycashpoint.domain.repository.AgenceRepository
import org.megamind.mycashpoint.domain.repository.AnalyticsRepository
import org.megamind.mycashpoint.domain.repository.CommissionRepository
import org.megamind.mycashpoint.domain.repository.EtablissementRepository
import org.megamind.mycashpoint.domain.repository.SoldeRepository
import org.megamind.mycashpoint.domain.repository.TransactionRepository
import org.megamind.mycashpoint.domain.repository.UserRepository
import org.megamind.mycashpoint.domain.usecase.agence.GetAgencesUseCase
import org.megamind.mycashpoint.domain.usecase.agence.SaveOrUpdateAgenceUseCase
import org.megamind.mycashpoint.domain.usecase.analytics.GetAgenceAnalyticsUseCase
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
import org.megamind.mycashpoint.domain.usecase.rapport.GetNonSyncTransactByOperatorAndDeviseUseCase
import org.megamind.mycashpoint.domain.usecase.rapport.GetSyncTransactByOperatorAndDeviseUseCase
import org.megamind.mycashpoint.domain.usecase.solde.AdminSaveSoldeUseCase
import org.megamind.mycashpoint.domain.usecase.solde.GetRemoteSoldesByAgenceAndUserUseCase
import org.megamind.mycashpoint.domain.usecase.solde.GetSoldeByOperateurEtTypeEtDeviseUseCase
import org.megamind.mycashpoint.domain.usecase.solde.GetSoldeForSyncUsecas
import org.megamind.mycashpoint.domain.usecase.solde.GetSoldeFromServerByCreteriaUseCase
import org.megamind.mycashpoint.domain.usecase.solde.GetSoldeInRutureUseCase
import org.megamind.mycashpoint.domain.usecase.solde.InsertSoldeListLocallyUseCase
import org.megamind.mycashpoint.domain.usecase.solde.SaveOrUpdateSoldeUseCase
import org.megamind.mycashpoint.domain.usecase.solde.SyncSoldesUseCase
import org.megamind.mycashpoint.domain.usecase.etablissement.GetEtablissementFromServerUseCase
import org.megamind.mycashpoint.domain.usecase.etablissement.GetEtablissementFromLocalUseCase
import org.megamind.mycashpoint.domain.usecase.etablissement.UpdateEtablissementUseCase
import org.megamind.mycashpoint.domain.usecase.etablissement.GetEtablissementFromServerAndInsertLocalyUseCase

import org.megamind.mycashpoint.domain.usecase.transaction.DeleteTransactionUseCase
import org.megamind.mycashpoint.domain.usecase.transaction.GenerateTransactionReportUseCase
import org.megamind.mycashpoint.domain.usecase.transaction.GetAllTransactionFromServerUseCase
import org.megamind.mycashpoint.domain.usecase.transaction.GetTopOperateurUseCase
import org.megamind.mycashpoint.domain.usecase.transaction.GetRemoteTransactionsByAgenceAndUserUseCase
import org.megamind.mycashpoint.domain.usecase.transaction.InsertTransactionListLocallyUseCase
import org.megamind.mycashpoint.domain.usecase.transaction.InsertTransactionAndUpdateSoldesUseCase
import org.megamind.mycashpoint.domain.usecase.transaction.SendOneTransactToServerUseCase
import org.megamind.mycashpoint.domain.usecase.transaction.SyncTransactionUseCase
import org.megamind.mycashpoint.domain.usecase.transaction.UpdateTransactionUseCase
import org.megamind.mycashpoint.ui.screen.agence.AgenceViewModel
import org.megamind.mycashpoint.ui.screen.admin.dash_board.DashBoardViewModel
import org.megamind.mycashpoint.ui.screen.admin.rapport.AdminRapportViewModel
import org.megamind.mycashpoint.ui.screen.auth.RegisterViewModel
import org.megamind.mycashpoint.ui.screen.auth.LoginViewModel
import org.megamind.mycashpoint.ui.screen.admin.etablissement.EtablissementViewModel
import org.megamind.mycashpoint.ui.screen.solde.SoldeViewModel
import org.megamind.mycashpoint.ui.screen.main.MainViewModel
import org.megamind.mycashpoint.utils.DataStorageManager
import org.megamind.mycashpoint.ui.screen.operateur.OperateurViewModel
import org.megamind.mycashpoint.ui.screen.rapport.RapportViewModel
import org.megamind.mycashpoint.ui.screen.splash.SplashViewModel
import org.megamind.mycashpoint.ui.screen.transaction.AllTransactionViewModel
import org.megamind.mycashpoint.ui.screen.transaction.TransactionViewModel

val appModule = module {


    single { DataStorageManager(androidContext()) }


    //repo
    single<UserRepository> {
        UserRepositoryImpl(get(), get(), get())
    }

    single<SoldeRepository> {
        SoldeRepositoryImpl(get(), get(), get())
    }

    single<TransactionRepository> {
        TransactionRepositoryImpl(get(), get(), get(), get())
    }

    single<AgenceRepository> {
        AgenceRepositoryImpl(get())
    }

    single<CommissionRepository> {
        CommissionRepositoryImpl(get())
    }

    single<AnalyticsRepository> {
        AnalyticsRepositryImpl(get())
    }

    single<EtablissementRepository> {
        EtablissementRepositoryImpl(get(), get())
    }

    // Use cases
    single { GetAgencesUseCase(get()) }
    single { SaveOrUpdateAgenceUseCase(get()) }
    single { GetSoldeByOperateurEtTypeEtDeviseUseCase(get()) }
    single { SaveOrUpdateSoldeUseCase(get()) }
    single { InsertTransactionAndUpdateSoldesUseCase(get()) }
    single { GetNonSyncTransactByOperatorAndDeviseUseCase(get()) }
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
    single { GenerateTransactionReportUseCase(get()) }
    single { GetSoldeInRutureUseCase(get()) }
    single { GetSyncTransactByOperatorAndDeviseUseCase(get()) }
    single { InsertTransactionListLocallyUseCase(get()) }
    single { InsertSoldeListLocallyUseCase(get()) }
    single { GetAllTransactionFromServerUseCase(get()) }
    single { GetRemoteTransactionsByAgenceAndUserUseCase(get()) }
    single { GetRemoteSoldesByAgenceAndUserUseCase(get()) }
    single { GetSoldeForSyncUsecas(get()) }
    single { AdminSaveSoldeUseCase(get()) }
    single { GetAgenceAnalyticsUseCase(get()) }

    single { GetEtablissementFromServerUseCase(get()) }
    single { GetEtablissementFromLocalUseCase(get()) }
    single { UpdateEtablissementUseCase(get()) }
    single { GetEtablissementFromServerAndInsertLocalyUseCase(get()) }


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
        OperateurViewModel(get(), get(), get(), get(), get())
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
        DashBoardViewModel(get(), get(), get(), get(), get(), get(), get())
    }

    viewModel {
        AdminRapportViewModel(get(), get())
    }

    viewModel {
        AllTransactionViewModel(get())
    }

    viewModel {
        EtablissementViewModel(get(), get())
    }


}

