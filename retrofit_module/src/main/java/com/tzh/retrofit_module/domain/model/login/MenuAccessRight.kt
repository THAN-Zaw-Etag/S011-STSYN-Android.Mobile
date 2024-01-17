package com.tzh.retrofit_module.domain.model.login

data class MenuAccessRight(
    val allowAccountabilityCheck: Boolean,
    val allowBookIn: Boolean,
    val allowBookInBox: Boolean,
    val allowBookInCal: Boolean,
    val allowBookInP_Loan: Boolean,
    val allowBookInP_LoanBox: Boolean,
    val allowBookInT_Loan: Boolean,
    val allowBookInT_LoanBox: Boolean,
    val allowBookOut: Boolean,
    val allowBookOutBox: Boolean,
    val allowOnSiteTransfer: Boolean,
    val allowOnSiteVerification: Boolean,
    val allowPermanentLoan: Boolean,
    val allowPermanentLoanBox: Boolean,
    val allowT_Loan: Boolean,
    val allowT_LoanBox: Boolean
)