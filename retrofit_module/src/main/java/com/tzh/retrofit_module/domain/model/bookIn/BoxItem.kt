package com.tzh.retrofit_module.domain.model.bookIn

data class BoxItem(
    val actualCalDate: String = "",
    val airbase: String = "",
    val airbaseId: String = "",
    val basicDate: String = "",
    val box: String = "",
    val calDate: String = "",
    val calDateString: String = "",
    val calDueDate: String = "",
    val classification: String = "",
    val csNo: String = "",
    val ctk: String = "",
    val ctkBox: String = "",
    val description: String = "",
    val docNo: String = "",
    val epc: String = "",
    val flight: String = "",
    val flightId: String = "",
    val id: String = "",
    val ipAddress: String = "",
    val isBox: Boolean = false,
    val isCalRequired: Boolean = false,
    val isDeleted: Boolean = false,
    val issuerId: String = "",
    val itemLocation: String = "",
    val itemStatus: String = "",
    val itemStatusUpdatedDate: String = "",
    val itemType: String = "",
    val itemTypeEnum: String = "",
    val lastUpdatedDate: String = "",
    val loanDueDate: String = "",
    val ncr: Boolean = false,
    val no: String = "",
    val nsn: String = "",
    val otherBox: String = "",
    val partNo: String = "",
    val qty: String = "",
    val receiverId: String = "",
    val remarks: String = "",
    val serialNo: String = "",
    val showCalAlert: Boolean = false,
    val storeLocation: String = "",
    val storeName: String = "",
    val storeType: String = "",
    val tagQty: String = "",
    val tagType: String = "",
    val title: String = "",
    val tmdeBox: String = "",
    val unit: String = "",
    val unitCost: String = "",
    val unitId: String = "",
    val updatedVersion: String = "",
    val vendor: String = "",
    val workLocation: String = "",
    var isScanned:Boolean = false
)
fun BoxItem.safeCopy(
    actualCalDate: String? = this.actualCalDate,
    airbase: String? = this.airbase,
    airbaseId: String? = this.airbaseId,
    basicDate: String? = this.basicDate,
    box: String? = this.box,
    calDate: String? = this.calDate,
    calDateString: String? = this.calDateString,
    calDueDate: String? = this.calDueDate,
    classification: String? = this.classification,
    csNo: String? = this.csNo,
    ctk: String? = this.ctk,
    ctkBox: String? = this.ctkBox,
    description: String? = this.description,
    docNo: String? = this.docNo,
    epc: String? = this.epc,
    flight: String? = this.flight,
    flightId: String? = this.flightId,
    id: String? = this.id,
    ipAddress: String? = this.ipAddress,
    isBox: Boolean? = this.isBox,
    isCalRequired: Boolean? = this.isCalRequired,
    isDeleted: Boolean? = this.isDeleted,
    issuerId: String? = this.issuerId,
    itemLocation: String? = this.itemLocation,
    itemStatus: String? = this.itemStatus,
    itemStatusUpdatedDate: String? = this.itemStatusUpdatedDate,
    itemType: String? = this.itemType,
    itemTypeEnum: String? = this.itemTypeEnum,
    lastUpdatedDate: String? = this.lastUpdatedDate,
    loanDueDate: String? = this.loanDueDate,
    ncr: Boolean? = this.ncr,
    no: String? = this.no,
    nsn: String? = this.nsn,
    otherBox: String? = this.otherBox,
    partNo: String? = this.partNo,
    qty: String? = this.qty,
    receiverId: String? = this.receiverId,
    remarks: String? = this.remarks,
    serialNo: String? = this.serialNo,
    showCalAlert: Boolean? = this.showCalAlert,
    storeLocation: String? = this.storeLocation,
    storeName: String? = this.storeName,
    storeType: String? = this.storeType,
    tagQty: String? = this.tagQty,
    tagType: String? = this.tagType,
    title: String? = this.title,
    tmdeBox: String? = this.tmdeBox,
    unit: String? = this.unit,
    unitCost: String? = this.unitCost,
    unitId: String? = this.unitId,
    updatedVersion: String? = this.updatedVersion,
    vendor: String? = this.vendor,
    workLocation: String? = this.workLocation,
    isScanned: Boolean? = this.isScanned

): BoxItem {
    return this.copy(
        actualCalDate = actualCalDate ?: "",
        airbase = airbase ?: "",
        airbaseId = airbaseId ?: "",
        basicDate = basicDate ?: "",
        box = box ?: "",
        calDate = calDate ?: "",
        calDateString = calDateString ?: "",
        calDueDate = calDueDate ?: "",
        classification = classification ?: "",
        csNo = csNo ?: "",
        ctk = ctk ?: "",
        ctkBox = ctkBox ?: "",
        description = description ?: "",
        docNo = docNo ?: "",
        epc = epc ?: "",
        flight = flight ?: "",
        flightId = flightId ?: "",
        id = id ?: "",
        ipAddress = ipAddress ?: "",
        isBox = isBox ?: false,
        isCalRequired = isCalRequired ?: false,
        isDeleted = isDeleted ?: false,
        issuerId = issuerId ?: "",
        itemLocation = itemLocation ?: "",
        itemStatus = itemStatus ?: "",
        itemStatusUpdatedDate = itemStatusUpdatedDate ?: "",
        itemType = itemType ?: "",
        itemTypeEnum = itemTypeEnum ?: "",
        lastUpdatedDate = lastUpdatedDate ?: "",
        loanDueDate = loanDueDate ?: "",
        ncr = ncr ?: false,
        no = no ?: "",
        nsn = nsn ?: "",
        otherBox = otherBox ?: "",
        partNo = partNo ?: "",
        qty = qty ?: "",
        receiverId = receiverId ?: "",
        remarks = remarks ?: "",
        serialNo = serialNo ?: "",
        showCalAlert = showCalAlert ?: false,
        storeLocation = storeLocation ?: "",
        storeName = storeName ?: "",
        storeType = storeType ?: "",
        tagQty = tagQty ?: "",
        tagType = tagType ?: "",
        title = title ?: "",
        tmdeBox = tmdeBox ?: "",
        unit = unit ?: "",
        unitCost = unitCost ?: "",
        unitId = unitId ?: "",
        updatedVersion = updatedVersion ?: "",
        vendor = vendor ?: "",
        workLocation = workLocation ?: "",
        isScanned = isScanned ?: false
    )
}

