package by.andryproger.elinexttesttask.presentation.di

import by.andryproger.elinexttesttask.presentation.di.app.AppComponent


object Injector {
    private lateinit var sAppComponent: AppComponent

    fun init(component: AppComponent) {
        sAppComponent = component
    }

    val component: AppComponent
        get() = sAppComponent
}