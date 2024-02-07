package com.tzh.retrofit_module.data.mapper

import com.tzh.retrofit_module.data.model.account_check.AccountCheckOutstandingItemsRequest
import com.tzh.retrofit_module.domain.model.FilterItem
import com.tzh.retrofit_module.domain.model.accountabilityCheck.DropdownSet

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
        csNo = if (this[1].selectedOption.isEmpty()) "0" else this[1].selectedOption,
        unitSqn = this[1].selectedOption,
        flight = this[1].selectedOption,
        itemType = this[1].selectedOption,
        itemLocation = this[1].selectedOption,
        box = this[1].selectedOption,
        remarks = this[1].selectedOption,
    )
}