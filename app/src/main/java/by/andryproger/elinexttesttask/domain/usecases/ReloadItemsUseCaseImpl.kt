package by.andryproger.elinexttesttask.domain.usecases

import by.andryproger.elinexttesttask.domain.gateways.repository.ItemsRepository
import io.reactivex.Observable

class ReloadItemsUseCaseImpl(
    private val itemsRepository: ItemsRepository,
    private val loadItemsUseCase: LoadItemsUseCase
) : ReloadItemsUseCase {

    override fun reload(): Observable<RequestResult<Long>> {
        return Observable.defer {
            itemsRepository.clear()
            loadItemsUseCase.load(RELOAD_COUNT)
        }
    }

    companion object {
        const val RELOAD_COUNT = 140
    }
}