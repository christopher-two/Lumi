package org.christophertwo.qr.utils

sealed class RoutesStart(val route: String) {
    object Generator : RoutesStart("Generador")
    object Scanner : RoutesStart("Escáner")
}