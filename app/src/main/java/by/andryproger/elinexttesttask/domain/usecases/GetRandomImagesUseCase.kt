package by.andryproger.elinexttesttask.domain.usecases

import io.reactivex.Observable
import io.reactivex.Single

interface GetRandomImagesUseCase {
    fun getAsList(count: Int): Single<RequestResult<List<String>>>

    fun getByOne(count: Int): Observable<RequestResult<String>>
}

sealed class RequestResult<T> {
    class Success<T>(val value: T) : RequestResult<T>()
    class Error<T>(val e: Throwable) : RequestResult<T>()
}