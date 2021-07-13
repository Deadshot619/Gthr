package com.gthr.gthrcollect.model.domain

data class CountryStatesModelItem(
    val country: String,
    val states: List<State>
)

data class State(
    val abbreviation: String,
    val name: String
)
