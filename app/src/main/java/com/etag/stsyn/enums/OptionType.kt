package com.etag.stsyn.enums

enum class OptionType(val title: String = "") {
    BookOut("Book Out"),
    BookOutBox("Book Out (Box)"),
    BookIn("Book In"),
    BookInBox("Book In (Box)"),
    BookInCalibration("Book In (Cal)"),
    BookInTLoan("Book In (T-Loan)"),
    BookInTLoanBox("In (T-Loan Box)"),
    BookInDetPLoan("In (Det/P-Loan)"),
    BookInDetPLoanBox("Det/P-Loan Box"),
    OnsiteCheckInOut("Check In/Out"),
    OnsiteVerification("Onsite Verify"),
    OtherTLoan("T-Loan Out"),
    OtherTLoanBox("T-Loan Box"),
    OtherDetPLoan("Det/P-Loan"),
    OtherDetPLoanBox("Det/P-Loan Box"),
    AccountCheck("Acct Check")
}