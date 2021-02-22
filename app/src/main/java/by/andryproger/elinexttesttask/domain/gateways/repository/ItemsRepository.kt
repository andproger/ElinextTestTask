package by.andryproger.elinexttesttask.domain.gateways.repository

import by.andryproger.elinexttesttask.domain.entities.ImageItem
import io.reactivex.Observable

interface ItemsRepository {

    fun save(imageItem: ImageItem)

    fun get(): List<ImageItem>

    fun getWithUpdates(): Observable<List<ImageItem>>

    fun clear()
}