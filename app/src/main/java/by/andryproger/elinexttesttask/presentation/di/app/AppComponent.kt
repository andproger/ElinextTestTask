package by.andryproger.elinexttesttask.presentation.di.app

import by.andryproger.elinexttesttask.presentation.app.App
import by.andryproger.elinexttesttask.presentation.features.home.HomeVMFactory
import by.andryproger.elinexttesttask.presentation.di.network.NetworkModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        NetworkModule::class,
        UseCasesModule::class,
        RepositoriesModule::class
    ]
)
interface AppComponent {
    fun inject(application: App)

    fun inject(factory: HomeVMFactory)
}