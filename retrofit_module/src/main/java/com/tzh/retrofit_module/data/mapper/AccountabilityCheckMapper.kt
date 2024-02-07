package com.tzh.retrofit_module.data.mapper

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