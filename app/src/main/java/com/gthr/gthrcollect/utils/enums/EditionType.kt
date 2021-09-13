package com.gthr.gthrcollect.utils.enums

import com.gthr.gthrcollect.model.domain.PokemonDomainModel
import com.gthr.gthrcollect.model.domain.ProductDisplayModel
import com.gthr.gthrcollect.model.domain.YugiohDomainModel


enum class EditionType(val title: String) {
    NOTHING("Nothing"),
    UNLIMITED("Unlimited"),
    LIMITED_EDITION("Limited Edition"),
    FIRST_EDITION("1st Edition"),
    FOURTH_PRINT_UK("4th Print (UK)"),
    REVERSE_HOLO_UNLIMITED("Rev. Holo"),
    HOLO_UNLIMITED("Holo Unlimited"),
    REVERSE_HOLO_FIRST_EDITION("Rev. Holo 1st Ed"),
    PROMO("Promo"),
    JUMBO_PROMO("Jumbo"),
    NON_FOIL("Non Foil"),
    FOIL("Foil"),
    NONE("None")
}


