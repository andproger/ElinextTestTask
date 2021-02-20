package by.andryproger.elinexttesttask.presentation.features.home

class ImageState(
    val itemId: Int,
    val link: String?
) {

    companion object {
        private var lastId = 1

        fun generateId() = lastId++
    }
}