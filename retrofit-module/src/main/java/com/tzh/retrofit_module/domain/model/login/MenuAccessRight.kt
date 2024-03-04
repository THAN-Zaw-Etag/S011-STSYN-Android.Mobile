package com.tzh.retrofit_module.domain.model.login

import com.google.gson.annotations.SerializedName

data class MenuAccessRight(
    val allowAccountabilityCheck: Boolean = true,
    val allowBookIn: Boolean = true,
    val allowBookInBox: Boolean = true,
    val allowBookInCal: Boolean = true,
    @SerializedName("allowBookInP_Loan")
    val allowBookInPLoan: Boolean = true,
    @SerializedName("allowBookInP_LoanBox")
    val allowBookInPLoanBox: Boolean = true,
    @SerializedName("allowBookInT_Loan")
    val allowBookInTLoan: Boolean = true,
    @SerializedName("allowBookInT_LoanBox")
    val allowBookInTLoanBox: Boolean = true,
    val allowBookOut: Boolean = true,
    val allowBookOutBox: Boolean = true,
    val allowOnSiteTransfer: Boolean = true,
    val allowOnSiteVerification: Boolean = true,
    val allowPermanentLoan: Boolean = true,
    val allowPermanentLoanBox: Boolean = true,
    @SerializedName("allowT_Loan")
    val allowTLoan: Boolean = true,
    @SerializedName("allowT_LoanBox")
    val allowTLoanBox: Boolean = true
)