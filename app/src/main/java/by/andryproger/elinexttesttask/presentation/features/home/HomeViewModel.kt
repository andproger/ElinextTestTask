package by.andryproger.elinexttesttask.presentation.features.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import by.andryproger.elinexttesttask.domain.gateways.repository.ItemsRepository
import by.andryproger.elinexttesttask.domain.usecases.AddNewItemUseCase
import by.andryproger.elinexttesttask.domain.usecases.ReloadItemsUseCase
import by.andryproger.elinexttesttask.domain.usecases.ReloadItemsUseCaseImpl.Companion.RELOAD_COUNT
import by.andryproger.elinexttesttask.domain.usecases.RequestResult
import by.andryproger.elinexttesttask.presentation.di.Injector
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MainViewModel(
    private val addNewItemUseCase: AddNewItemUseCase,
    private val reloadItemsUseCase: ReloadItemsUseCase,
    private val itemsRepository: ItemsRepository
) : ViewModel() {

    val modelSources: ModelSources = ModelSourcesImpl(
        addNewOneImpl = { addNewOne() },
        reloadAllImpl = { reloadAll() }
    )

    private val mutableModelSources = modelSources as ModelSourcesImpl
    private val items = mutableModelSources.items
    private val loading = mutableModelSources.loading

    private val scrollToEndEvent = mutableModelSources.scrollToEndEvent
    private val errorEvent = mutableModelSources.errorEvent

    private var loadingDisposable = Disposables.disposed()
    private var compositeDisposable = CompositeDisposable()

    init {
        subscribeToItemsUpdates()
        reloadAll()
    }

    private fun subscribeToItemsUpdates() {
        itemsRepository.getWithUpdates()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { imageItems ->
                mutateItems {
                    imageItems.forEachIndexed { index, imageItem ->
                        if (getOrNull(index) != null) {
                            set(index, ImageState(imageItem.itemId, imageItem.link))
                        } else {
                            add(ImageState(imageItem.itemId, imageItem.link))
                        }
                    }
                }
            }.let(compositeDisposable::add)
    }

    private fun mutateItems(mutate: MutableList<ImageState>.() -> Unit) {
        items.value = items.value?.toMutableList()?.apply { mutate() }
    }

    private fun addNewOne() {
        if (loadingDisposable.isDisposed) {
            val newItem = emptyItems(1).first()
            mutateItems { add(newItem) }

            loadingDisposable = addNewItemUseCase.add()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    loading.value = true
                    errorEvent.value = null
                }
                .doFinally {
                    loading.value = false
                }.subscribe { result ->
                    items.value?.indexOfFirst { it.itemId == newItem.itemId }
                        ?.let { indexOfNew ->
                            when (result) {
                                is RequestResult.Success -> scrollToEnd()
                                is RequestResult.Error -> {
                                    onError(result.e)
                                    mutateItems { removeAt(indexOfNew) }
                                }
                            }
                        }
                }
        }
    }

    private fun reloadAll() {
        if (loadingDisposable.isDisposed) {
            items.value = emptyItems(RELOAD_COUNT)

            loadingDisposable = reloadItemsUseCase.reload()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    loading.value = true
                    errorEvent.value = null
                }
                .doFinally {
                    loading.value = false
                }.subscribe { result ->
                    when (result) {
                        is RequestResult.Error -> onError(result.e)
                    }
                }
        }
    }

    private fun scrollToEnd() {
        scrollToEndEvent.value = true
    }

    private fun emptyItems(count: Int): MutableList<ImageState> {
        return mutableListOf<ImageState>().apply {
            repeat(count) {
                add(ImageState(ImageState.generateId(), null))
            }
        }
    }

    private fun onError(error: Throwable) {
        if (errorEvent.value?.shown != true) {
            errorEvent.value = ErrorState("Error:${error.message}", false)
        }
    }

    override fun onCleared() {
        loadingDisposable.dispose()
        super.onCleared()
    }
}

interface ModelSources {
    fun addNewOne()
    fun reloadAll()

    val items: LiveData<List<ImageState>>
    val loading: LiveData<Boolean>

    val scrollToEndEvent: MutableLiveData<Boolean?>
    val errorEvent: MutableLiveData<ErrorState?>
}

class ModelSourcesImpl(
    private val addNewOneImpl: () -> Unit,
    private val reloadAllImpl: () -> Unit
) : ModelSources {

    override val items: MutableLiveData<List<ImageState>> = MutableLiveData()
    override val loading: MutableLiveData<Boolean> = MutableLiveData(false)

    override val scrollToEndEvent: MutableLiveData<Boolean?> = MutableLiveData()
    override val errorEvent: MutableLiveData<ErrorState?> = MutableLiveData()

    override fun addNewOne() {
        addNewOneImpl()
    }

    override fun reloadAll() {
        reloadAllImpl()
    }
}

class HomeVMFactory : ViewModelProvider.Factory {

    @Inject
    lateinit var addNewItemUseCase: AddNewItemUseCase

    @Inject
    lateinit var reloadItemsUseCase: ReloadItemsUseCase

    @Inject
    lateinit var itemsRepository: ItemsRepository

    init {
        Injector.component.inject(this)
    }

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")

        return MainViewModel(
            addNewItemUseCase,
            reloadItemsUseCase,
            itemsRepository
        ) as T
    }
}

class ErrorState(
    val message: String,
    val shown: Boolean
)