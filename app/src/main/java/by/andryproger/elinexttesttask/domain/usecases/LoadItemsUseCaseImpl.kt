package by.andryproger.elinexttesttask.domain.usecases

import by.andryproger.elinexttesttask.domain.entities.ImageItem
import by.andryproger.elinexttesttask.domain.gateways.repository.ItemsRepository
import io.reactivex.Observable

class LoadItemsUseCaseImpl(
    private val itemsRepository: ItemsRepository,
    private val getRandomImagesUseCase: GetRandomImagesUseCase
) : LoadItemsUseCase {
    override fun load(count: Int): Observable<RequestResult<Long>> {
        return getRandomImagesUseCase.getByOne(count).map { result ->
            when (result) {
                is RequestResult.Success -> {
                    val item = ImageItem(ImageItem.generateId(), result.value)
                    itemsRepository.save(item)
                    RequestResult.Success(item.itemId)
                }
                is RequestResult.Error -> {
                    RequestResult.Error<Long>(result.e)
                }
            }
        }
    }
}