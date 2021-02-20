package by.andryproger.elinexttesttask.domain.usecases

import by.andryproger.elinexttesttask.domain.gateways.repository.RandomImageRepository
import io.reactivex.Observable
import io.reactivex.Single

class GetRandomImagesUseCaseImpl(
    private val randomImageRepository: RandomImageRepository
) : GetRandomImagesUseCase {

    override fun getByOne(count: Int): Observable<RequestResult<String>> {
        return Observable.create<RequestResult<String>> { emitter ->
            var countDone = 0

            @Synchronized
            fun onResult(link: String?, error: Throwable?) {
                val item = error?.let { RequestResult.Error<String>(it) }
                    ?: link?.let { RequestResult.Success(it) }

                item?.let(emitter::onNext)

                countDone++
                if (count == countDone) emitter.onComplete()
            }

            repeat(count) {
                randomImageRepository.get { link, error -> onResult(link, error) }
            }
        }.onErrorReturn {
            RequestResult.Error(it)
        }
    }

    override fun getAsList(count: Int): Single<RequestResult<List<String>>> {
        return getByOne(count)
            .toList()
            .map { list ->
                RequestResult.Success(list.mapNotNull {
                    (it as? RequestResult.Success)?.value
                }) as RequestResult<List<String>>
            }
            .onErrorReturn {
                RequestResult.Error(it)
            }
    }
}