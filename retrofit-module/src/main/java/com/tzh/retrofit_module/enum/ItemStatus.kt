package com.tzh.retrofit_module.enum

enum class ItemStatus(val title: String) {
    BookIn("IN"),
    OUTSTANDING("U/S");

    companion object {
        private val titles = entries.associateBy(ItemStatus::title)

        fun isUSCase(title: String): Boolean {
            return titles.get(title) == OUTSTANDING
        }
    }
}