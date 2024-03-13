package com.tzh.retrofit_module.data.mapper

import com.tzh.retrofit_module.data.model.book_in.ItemMovementLog
import com.tzh.retrofit_module.domain.model.ExpandedScannedItemModel
import com.tzh.retrofit_module.domain.model.bookIn.BoxItem
import com.tzh.retrofit_module.enum.ItemStatus

fun List<BoxItem>.toItemMovementLogs(
    handleHeldId: Int,
    currentDate: String,
    userId: String,
    workLocation: String,
    itemStatus: String,
): List<ItemMovementLog> {
    return map {
        ItemMovementLog(
            itemId = it.id.toInt(),
            description = it.description,
            itemStatus = itemStatus,
            workLoc = workLocation,
            issuerId = userId.toInt(),
            receiverId = it.receiverId.toInt(),
            approverId = 0,
            date = currentDate,
            handheldReaderId = handleHeldId,
            calDate = it.calDate,
            isOnSiteTransfer = "0",
            remarks = it.remarks,
            receiverName = "",
            buddyId = "0",
            itemType = it.itemType
        )
    }
}

fun BoxItem.toItemMovementLog(
    itemStatus: String,
    workLocation: String,
    issuerId: String,
    date: String,
    readerId: String,
    buddyId: String = "0",
    visualChecked: Boolean = false,
): ItemMovementLog {
    return ItemMovementLog(
            itemId = this.id.toInt(),
            description = this.description,
            itemStatus = itemStatus,
            workLoc = workLocation,
            issuerId = issuerId.toInt(),
            receiverId = this.receiverId.toInt(),
            approverId = 0,
            date = date,
            handheldReaderId = readerId.toInt(),
            calDate = this.calDate,
            isOnSiteTransfer = "0",
            remarks = if (visualChecked) "Visual Check" else "",
            receiverName = "",
            buddyId = buddyId,
            itemType = this.itemType
        )
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
            workLoc = it.workLocation,
            issuerId = it.issuerId.toInt(),
            receiverId = it.receiverId.toInt(),
            approverId = 0,
            date = date,
            handheldReaderId = handleHeldId,
            calDate = it.calDate,
            isOnSiteTransfer = "0",
            remarks = if (isVisualChecked) "Visual Check" else "",
            receiverName = "",
            buddyId = buddyId ?: "0",
            itemType = it.itemType
        )
    }
}

fun BoxItem.toExpandedScannedItems(): ExpandedScannedItemModel {
    return ExpandedScannedItemModel(
        serialNo = "${this.serialNo} - ${this.partNo}",
        description = this.description,
        code = this.unit,
        location = this.itemLocation,
        storeLocation = this.storeLocation,
        status = this.itemStatus
    )
}
