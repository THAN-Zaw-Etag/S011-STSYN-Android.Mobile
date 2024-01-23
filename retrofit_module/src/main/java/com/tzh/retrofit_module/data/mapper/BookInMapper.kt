package com.tzh.retrofit_module.data.mapper

import com.tzh.retrofit_module.data.model.book_in.ItemMovementLog
import com.tzh.retrofit_module.domain.model.bookIn.BookInItem
import com.tzh.retrofit_module.enum.ItemStatus
import com.tzh.retrofit_module.util.DateUtil


fun List<BookInItem>.toItemMovementLog(
    handleHeldId: Int
): List<ItemMovementLog> {
    val currentDate = DateUtil.getCurrentDate()
    return map {
        ItemMovementLog(
            itemId = it.id,
            description = it.description,
            itemStatus = ItemStatus.BookIn.title,
            workLoc = it.workLocation,
            issuerId = 0,
            receiverId = it.receiverId,
            approverId = 0,
            date = currentDate,
            handheldReaderId = handleHeldId,
            calDate = "2024-01-22T03:27:46.114Z", //TODO it.calDate
            iS_ONSITE_TRANSFER = "0",
            remarks = it.remarks,
            receiverName = "",
            buddyId = "0",
            itemType = it.itemType
        )
    }
}

/*
fun List<BookInItem>.toExpandedScannedItems(): List<ExpandedScannedItemModel> {
    return map {
        ExpandedScannedItemModel(
            serialNo = "${it.serialNo} - ${it.partNo}",
            description = it.description,
            code = it.unit,
            location = it.itemLocation,
            storeLocation = it.storeLocation,
            status = ItemStatus.BookIn.title
        )
    }
}

fun List<BoxItem>.toExpandedScannedItems(): List<ExpandedScannedItemModel> {
    return map {
        ExpandedScannedItemModel(
            serialNo = "${it.serialNo} - ${it.partNo}",
            description = it.description,
            code = it.unit,
            location = it.itemLocation,
            storeLocation = it.storeLocation,
            status = ItemStatus.BookIn.title
        )
    }
}*/