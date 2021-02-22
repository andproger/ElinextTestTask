package by.andryproger.elinexttesttask.domain.usecases

import io.reactivex.Observable

interface ReloadItemsUseCase {
    fun reload(): Observable<RequestResult<Long>>
}