package es.aroldan.rickandmorty.presentation.mapper

abstract class ViewMapper<D, V> {

    abstract fun map(entity: D): V

    //abstract fun reverseMap(entity: V): D

    fun map(entities: List<D>): List<V> =
        entities.map { map(it) }.toList()
}
