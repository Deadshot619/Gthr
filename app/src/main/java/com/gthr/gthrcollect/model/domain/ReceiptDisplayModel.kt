package com.gthr.gthrcollect.model.domain

data class ReceiptDisplayModel(
    var receiptDomainModel: ReceiptDomainModel,
    var productDisplayModel: ProductDisplayModel,
    var saleHistoryDomainModel: SaleHistoryDomainModel,
)