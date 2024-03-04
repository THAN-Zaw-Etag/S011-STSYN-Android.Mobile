package com.tzh.retrofit_module.data.mapper

import com.tzh.retrofit_module.data.model.account_check.AccountCheckOutstandingItemsRequest
import com.tzh.retrofit_module.data.model.account_check.StockTake
import com.tzh.retrofit_module.domain.model.FilterItem
import com.tzh.retrofit_module.domain.model.accountabilityCheck.DropdownSet
import com.tzh.retrofit_module.domain.model.bookIn.BoxItem

fun DropdownSet.toFilterList(): List<FilterItem> {
    return listOf(
        FilterItem("Store Type", "", this.storeType),
        FilterItem("CS Number", "", this.csNo),
        FilterItem("Unit/SQN", "", this.unitSqn),
        FilterItem("Flight", "", this.flight),
        FilterItem("Item Type", "", this.itemType),
        FilterItem("Location", "", this.itemLocation),
        FilterItem("Box", "", this.box),
        FilterItem("Remarks", "", this.remarks),
    )
}

fun List<FilterItem>.toAccountabilityCheckRequest(): AccountCheckOutstandingItemsRequest {
    return AccountCheckOutstandingItemsRequest(
        storeType = this[0].selectedOption,
        csNo = this[1].selectedOption.ifEmpty { "0" },
        unitSqn = this[1].selectedOption,
        flight = this[1].selectedOption,
        itemType = this[1].selectedOption,
        itemLocation = this[1].selectedOption,
        box = this[1].selectedOption,
        remarks = this[1].selectedOption,
    )
}

fun BoxItem.toStockTake(
    readerId: String,
    date: String,
    checkStatusId: String,
    shift: String,
    userId: String
): StockTake {
    return StockTake(
        chkStatusId = checkStatusId,
        date = date,
        handheldReaderId = readerId,
        isDone = true,
        isStockTake = true,
        itemId = this.id,
        shift = shift,
        userId = userId
    )
}