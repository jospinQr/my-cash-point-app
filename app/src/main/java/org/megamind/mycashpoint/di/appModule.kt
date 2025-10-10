package org.megamind.mycashpoint.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import org.megamind.mycashpoint.data.data_source.repositoryImpl.UserRepositoryImpl
import org.megamind.mycashpoint.domain.repository.UserRepository
import org.megamind.mycashpoint.ui.screen.auth.RegisterViewModel
import org.megamind.mycashpoint.ui.screen.auth.SignInViewModel

val appModule = module {


    single<UserRepository> {
        UserRepositoryImpl(get())
    }


    viewModel {
        SignInViewModel(get())
    }

    viewModel {
        RegisterViewModel(get())
    }


}

