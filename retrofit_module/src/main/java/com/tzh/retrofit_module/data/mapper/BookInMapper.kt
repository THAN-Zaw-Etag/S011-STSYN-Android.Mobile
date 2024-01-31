package com.tzh.retrofit_module.data.mapper

import com.tzh.retrofit_module.data.model.book_in.ItemMovementLog
import com.tzh.retrofit_module.domain.model.ExpandedScannedItemModel
import com.tzh.retrofit_module.domain.model.bookIn.BookInItem
import com.tzh.retrofit_module.domain.model.bookIn.BoxItem
import com.tzh.retrofit_module.enum.ItemStatus
import com.tzh.retrofit_module.util.DateUtil

/**
 * For Book In
 * */
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
            calDate = it.calDate, //TODO it.calDate
            iS_ONSITE_TRANSFER = "0",
            remarks = it.remarks,
            receiverName = "",
            buddyId = "0",
            itemType = it.itemType
        )
    }
}

fun List<BookInItem>.toItemMovementLogs(
    handleHeldId: Int,
    currentDate: String,
    userId: String,
    workLocation: String,
    itemStatus: String,
): List<ItemMovementLog> {
    return map {
        ItemMovementLog(
            itemId = it.id,
            description = it.description,
            itemStatus = itemStatus,
            workLoc = workLocation,
            issuerId = userId.toInt(),
            receiverId = it.receiverId,
            approverId = 0,
            date = currentDate,
            handheldReaderId = handleHeldId,
            calDate = it.calDate, //TODO it.calDate
            iS_ONSITE_TRANSFER = "0",
            remarks = it.remarks,
            receiverName = "",
            buddyId = "0",
            itemType = it.itemType
        )
    }
}


/**
 * For Book in box
 * */
fun List<BoxItem>.toItemMovementLogs(
    date: String,
    itemStatus: ItemStatus,
    buddyId: String?,
    isVisualChecked: Boolean,
    handleHeldId: Int
): List<ItemMovementLog> {
    return map {
        ItemMovementLog(
            itemId = it.id.toInt(),
            description = it.description,
            itemStatus = itemStatus.title,
            workLoc = "",
            issuerId = it.issuerId.toInt(),
            receiverId = it.receiverId.toInt(),
            approverId = 0,
            date = date,
            handheldReaderId = handleHeldId,
            calDate = it.calDate,
            iS_ONSITE_TRANSFER = "0",
            remarks = if (isVisualChecked) "Visual Check" else "",
            receiverName = "",
            buddyId = buddyId ?: "0",
            itemType = it.itemType
        )
    }
}

fun BookInItem.toExpandedScannedItems(): ExpandedScannedItemModel {
    return ExpandedScannedItemModel(
        serialNo = "${this.serialNo} - ${this.partNo}",
        description = this.description,
        code = this.unit,
        location = this.itemLocation,
        storeLocation = this.storeLocation,
        status = ItemStatus.BookIn.title
    )
}

fun BoxItem.toExpandedScannedItems(): ExpandedScannedItemModel {
    return ExpandedScannedItemModel(
        serialNo = "${this.serialNo} - ${this.partNo}",
        description = this.description,
        code = this.unit,
        location = this.itemLocation,
        storeLocation = this.storeLocation,
        status = ItemStatus.BookIn.title
    )
}
