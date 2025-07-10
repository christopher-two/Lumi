package org.christophertwo.qr.utils

sealed class RoutesStart(val route: String) {
    object Generator : RoutesStart("generator")
    object Scanner : RoutesStart("scanner")
}