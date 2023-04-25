
# Rick & Morty App
Simple app Android que consume la API de [Rick and Morty](https://rickandmortyapi.com/).

## Features
- Splash
- Listado de personajes
- Detalle de personaje
- Favoritos

## Arquitectura
La arquitectura de la app se basa en la Clean Architecture, de manera que la capa que corresponde a la lógica de negocio queda aislada del resto.

La app está dividida en 4 módulos:
- app: contiene los componentes necesarios de una app Android además del código de inyección de dependencias. En este módulo se incluyen los otros tres módulos.
- data: código de acceso a datos. Incluye al módulo `domain`.
- domain: lógica de negocio. No depende de ningún otro módulo, de forma que no conoce nada del framework de Android, es 100% librería Kotlin.
- presentation: actividades, fragmentos, adaptadores, y en general, todo lo que engloba a la vista. Incluye al módulo `domain`. Para la capa de presentación se ha usado el patrón de diseño MVVM.

## Stack
- Corrutinas
- Flow
- Navigation
- Lifecycle
- ViewModel
- Retrofit
- Dagger Hilt
- Room
