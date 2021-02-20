package by.andryproger.elinexttesttask.domain.gateways.repository


interface RandomImageRepository {
    fun get(onResult: (String?, Throwable?) -> Unit)
}