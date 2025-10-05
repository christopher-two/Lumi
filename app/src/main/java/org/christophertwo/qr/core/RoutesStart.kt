package org.christophertwo.qr.core

sealed class RoutesStart(val route: String) {
    object Generator : RoutesStart("Generador")
    object Scanner : RoutesStart("Escáner")
}