package com.gthr.gthrcollect.utils.helper

import com.gthr.gthrcollect.model.domain.RecentSaleDomainModel

fun getEmptyRecentSaleDomainModel() = RecentSaleDomainModel("","","","",-1,"","","")

private fun getDashRecentSaleDomainModel() = RecentSaleDomainModel("","","--.--.----","",-1,"","\$-.--","-")

private fun getDateRecentSaleDomainModel(date : String) = RecentSaleDomainModel("","",date,"",-1,"","\$-.--","-")

fun getEmptyRecentSaleDomainModelList() : List<RecentSaleDomainModel>{
    val list = mutableListOf<RecentSaleDomainModel>()
    list.add(getEmptyRecentSaleDomainModel())
    list.add(getDashRecentSaleDomainModel())
    list.add(getDashRecentSaleDomainModel())
    list.add(getDashRecentSaleDomainModel())
    list.add(getDashRecentSaleDomainModel())
    return list
}

fun getRandomDateRecentSaleDomainModelList() : List<RecentSaleDomainModel>{
    val list = mutableListOf<RecentSaleDomainModel>()
    list.add(getDateRecentSaleDomainModel("1.3.2021"))
    list.add(getDateRecentSaleDomainModel("1.2.2021"))
    list.add(getDateRecentSaleDomainModel("1.5.2021"))
    list.add(getDateRecentSaleDomainModel("1.1.2021"))
    list.add(getDateRecentSaleDomainModel("1.4.2021"))
    list.add(getDateRecentSaleDomainModel("1.6.2021"))
    return list
}