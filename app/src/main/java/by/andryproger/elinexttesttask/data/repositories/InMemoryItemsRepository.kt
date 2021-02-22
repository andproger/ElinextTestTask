package by.andryproger.elinexttesttask.data.repositories

import by.andryproger.elinexttesttask.domain.entities.ImageItem
import by.andryproger.elinexttesttask.domain.gateways.repository.ItemsRepository
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

class InMemoryItemsRepository : ItemsRepository {

    private val items = mutableMapOf<Long, ImageItem>()
    private val changeSubject = BehaviorSubject.createDefault(true)

    override fun save(imageItem: ImageItem) {
        items[imageItem.itemId] = imageItem
        changeSubject.onNext(true)
    }

    override fun get(): List<ImageItem> {
        return items.values.toList()
    }

    override fun getWithUpdates(): Observable<List<ImageItem>> {
        return changeSubject.map { get() }
    }

    override fun clear() {
        items.clear()
        changeSubject.onNext(true)
    }
}