package by.andryproger.elinexttesttask.domain.entities

class ImageItem(
    val itemId: Long,
    val link: String
) {
    companion object {
        private var lastId = 1L

        fun generateId() = lastId++
    }
}