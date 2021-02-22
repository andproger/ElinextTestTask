package by.andryproger.elinexttesttask.domain.usecases


import io.reactivex.Single

class AddNewItemUseCaseImpl(
    private val loadItemsUseCase: LoadItemsUseCase
) : AddNewItemUseCase {

    override fun add(): Single<RequestResult<Long>> {
        return loadItemsUseCase.load(1).firstOrError()
    }
}