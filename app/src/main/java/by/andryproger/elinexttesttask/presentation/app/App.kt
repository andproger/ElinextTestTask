package by.andryproger.elinexttesttask.presentation.app

import android.app.Application
import by.andryproger.elinexttesttask.presentation.di.Injector
import by.andryproger.elinexttesttask.presentation.di.app.AppComponent
import by.andryproger.elinexttesttask.presentation.di.app.DaggerAppComponent

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        initDagger()
    }

    private fun initDagger() {
        val appComponent: AppComponent = DaggerAppComponent.builder()
            .build()

        Injector.init(appComponent)
        Injector.component.inject(this)
    }
}