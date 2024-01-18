package com.tzh.retrofit_module.domain.model.login

data class MenuAccessRight(
    val allowAccountabilityCheck: Boolean = true,
    val allowBookIn: Boolean = true,
    val allowBookInBox: Boolean = true,
    val allowBookInCal: Boolean = true,
    val allowBookInP_Loan: Boolean = true,
    val allowBookInP_LoanBox: Boolean = true,
    val allowBookInT_Loan: Boolean = true,
    val allowBookInT_LoanBox: Boolean = true,
    val allowBookOut: Boolean = true,
    val allowBookOutBox: Boolean = true,
    val allowOnSiteTransfer: Boolean = true,
    val allowOnSiteVerification: Boolean = true,
    val allowPermanentLoan: Boolean = true,
    val allowPermanentLoanBox: Boolean = true,
    val allowT_Loan: Boolean = true,
    val allowT_LoanBox: Boolean = true
)