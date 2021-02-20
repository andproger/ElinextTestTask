package by.andryproger.elinexttesttask.presentation.di.app

import by.andryproger.elinexttesttask.domain.gateways.repository.RandomImageRepository
import by.andryproger.elinexttesttask.domain.usecases.GetRandomImagesUseCase
import by.andryproger.elinexttesttask.domain.usecases.GetRandomImagesUseCaseImpl
import dagger.Module
import dagger.Provides

@Module
class UseCasesModule {

    @Provides
    fun provideGetRandomImagesUseCase(
        randomImageRepository: RandomImageRepository
    ): GetRandomImagesUseCase {
        return GetRandomImagesUseCaseImpl(randomImageRepository)
    }
}