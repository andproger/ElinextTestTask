package by.andryproger.elinexttesttask.presentation.di.app

import by.andryproger.elinexttesttask.data.repositories.InMemoryItemsRepository
import by.andryproger.elinexttesttask.data.repositories.RandomImageRepositoryImpl
import by.andryproger.elinexttesttask.domain.gateways.repository.ItemsRepository
import by.andryproger.elinexttesttask.domain.gateways.repository.RandomImageRepository
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
class RepositoriesModule {

    @Provides
    fun provideRandomImageRepository(
        okHttpClient: OkHttpClient
    ): RandomImageRepository {
        return RandomImageRepositoryImpl(
            okHttpClient
        )
    }

    @Singleton
    @Provides
    fun provideItemsRepository(): ItemsRepository {
        return InMemoryItemsRepository()
    }
}