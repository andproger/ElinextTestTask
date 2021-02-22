package by.andryproger.elinexttesttask.domain.usecases

import io.reactivex.Observable

interface LoadItemsUseCase {
    fun load(count: Int): Observable<RequestResult<Long>>
}