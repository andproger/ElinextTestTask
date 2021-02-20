package by.andryproger.elinexttesttask.presentation.features.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import by.andryproger.elinexttesttask.domain.usecases.GetRandomImagesUseCase
import by.andryproger.elinexttesttask.domain.usecases.RequestResult
import by.andryproger.elinexttesttask.presentation.di.Injector
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MainViewModel(
    private val getRandomImagesUseCase: GetRandomImagesUseCase
) : ViewModel() {

    val modelSources: ModelSources = ModelSourcesImpl(
        addNewOneImpl = { addNewOne() },
        reloadAllImpl = { reloadAll() }
    )

    private val mutableModelSources = modelSources as ModelSourcesImpl
    private val items = mutableModelSources.items
    private val loading = mutableModelSources.loading
    private val scrollToEnd = mutableModelSources.scrollToEnd
    private val error = mutableModelSources.error

    private var loadingDisposable = Disposables.disposed()

    init {
        reloadAll()
    }

    private fun addNewOne() {
        if (loadingDisposable.isDisposed) {
            val newItem = emptyItems(1).first()
            items.value = items.value?.toMutableList()?.apply { add(newItem) }

            items.value?.indexOfFirst { it.itemId == newItem.itemId }?.let { indexOfNew ->
                loadImages(1) { link ->
                    items.value = items.value?.toMutableList()?.apply {
                        set(indexOfNew, ImageState(newItem.itemId, link))
                    }
                    scrollToEnd()
                }
            }
        }
    }

    private fun reloadAll() {
        if (loadingDisposable.isDisposed) {
            val newItems = emptyItems(RELOAD_COUNT)
            items.value = newItems

            loadImages(RELOAD_COUNT) { link ->
                val indexOfEmpty = newItems.indexOfFirst { it.link == null }

                if (indexOfEmpty >= 0) {
                    newItems[indexOfEmpty] = ImageState(newItems[indexOfEmpty].itemId, link)
                } else {
                    newItems.add(ImageState(ImageState.generateId(), link))
                }
                items.value = newItems
            }
        }
    }

    private fun scrollToEnd() {
        scrollToEnd.value = true
    }

    private fun emptyItems(count: Int): MutableList<ImageState> {
        return mutableListOf<ImageState>().apply {
            repeat(count) {
                add(ImageState(ImageState.generateId(), null))
            }
        }
    }

    private fun loadImages(count: Int, onResult: (String) -> Unit) {
        loadingDisposable = getRandomImagesUseCase.getByOne(count)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                loading.value = true
                error.value = null
            }
            .doFinally {
                loading.value = false
            }
            .subscribe { result ->
                when (result) {
                    is RequestResult.Success -> onResult(result.value)
                    is RequestResult.Error -> {
                        if (error.value?.shown != true) {
                            error.value = ErrorState("Error:${result.e.message}", false)
                        }
                    }
                }
            }
    }

    override fun onCleared() {
        loadingDisposable.dispose()
        super.onCleared()
    }

    companion object {
        const val RELOAD_COUNT = 140
    }
}

interface ModelSources {
    fun addNewOne()
    fun reloadAll()

    val items: LiveData<List<ImageState>>
    val loading: LiveData<Boolean>

    val scrollToEnd: MutableLiveData<Boolean?>
    val error: MutableLiveData<ErrorState?>
}

class ModelSourcesImpl(
    private val addNewOneImpl: () -> Unit,
    private val reloadAllImpl: () -> Unit
) : ModelSources {

    override val items: MutableLiveData<List<ImageState>> = MutableLiveData()
    override val loading: MutableLiveData<Boolean> = MutableLiveData(false)

    override val scrollToEnd: MutableLiveData<Boolean?> = MutableLiveData()
    override val error: MutableLiveData<ErrorState?> = MutableLiveData()

    override fun addNewOne() {
        addNewOneImpl()
    }

    override fun reloadAll() {
        reloadAllImpl()
    }
}

class HomeVMFactory : ViewModelProvider.Factory {

    @Inject
    lateinit var getRandomImagesUseCase: GetRandomImagesUseCase

    init {
        Injector.component.inject(this)
    }

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")

        return MainViewModel(
            getRandomImagesUseCase
        ) as T
    }
}

class ErrorState(
    val message: String,
    val shown: Boolean
)