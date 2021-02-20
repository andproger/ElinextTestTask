package by.andryproger.elinexttesttask.presentation.di.app

import by.andryproger.elinexttesttask.data.network.RandomImageRepositoryImpl
import by.andryproger.elinexttesttask.domain.gateways.repository.RandomImageRepository
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient

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
}