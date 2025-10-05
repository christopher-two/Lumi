package org.christophertwo.qr.core.common

sealed class RoutesStart(val route: String) {
    object Generator : RoutesStart("Generador")
    object Scanner : RoutesStart("Escáner")
    object Start : RoutesStart("Start")
}