package es.aroldan.rickandmorty.data.mapper

abstract class EntityMapper<E, D> {

    abstract fun map(entity: E): D

    fun map(entities: List<E>): List<D> =
        entities.map { map(it) }.toList()
}
