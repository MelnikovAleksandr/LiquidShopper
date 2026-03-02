package ru.asmelnikov.liquidshopper.presentation.navigation

import ru.asmelnikov.liquidshopper.presentation.mainstate.MainAppState

fun MainAppState.popUp() {
    backStack.removeLastOrNull()
}

fun MainAppState.navigate(route: Routes) {
    backStack.add(route)
}