package com.jagerlipton.dots_lines.DI

import com.jagerlipton.dots_lines.storage.storage.IStorage
import com.jagerlipton.dots_lines.storage.storage.Storage
import com.jagerlipton.dots_lines.ui.home.HomeViewModel
import com.jagerlipton.dots_lines.ui.options.OptionsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<IStorage> {
        Storage(context = get())
    }

    viewModel {
        OptionsViewModel(
            storage = get()
        )
    }

    viewModel {
        HomeViewModel(
            storage = get()
        )
    }
}