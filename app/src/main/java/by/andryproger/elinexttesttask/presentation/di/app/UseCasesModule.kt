package by.andryproger.elinexttesttask.presentation.di.app

import by.andryproger.elinexttesttask.domain.gateways.repository.ItemsRepository
import by.andryproger.elinexttesttask.domain.gateways.repository.RandomImageRepository
import by.andryproger.elinexttesttask.domain.usecases.*
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

    @Provides
    fun provideAddNewItemUseCase(
        loadItemsUseCase: LoadItemsUseCase
    ): AddNewItemUseCase {
        return AddNewItemUseCaseImpl(
            loadItemsUseCase
        )
    }

    @Provides
    fun provideLoadItemsUseCase(
        itemsRepository: ItemsRepository,
        getRandomImagesUseCase: GetRandomImagesUseCase
    ): LoadItemsUseCase {
        return LoadItemsUseCaseImpl(
            itemsRepository,
            getRandomImagesUseCase
        )
    }

    @Provides
    fun provideReloadItemsUseCase(
        itemsRepository: ItemsRepository,
        loadItemsUseCase: LoadItemsUseCase
    ): ReloadItemsUseCase {
        return ReloadItemsUseCaseImpl(
            itemsRepository,
            loadItemsUseCase
        )
    }
}