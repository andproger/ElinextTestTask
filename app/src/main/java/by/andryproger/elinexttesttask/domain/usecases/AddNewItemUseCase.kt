package by.andryproger.elinexttesttask.domain.usecases

import io.reactivex.Single

interface AddNewItemUseCase {
    fun add(): Single<RequestResult<Long>>
}