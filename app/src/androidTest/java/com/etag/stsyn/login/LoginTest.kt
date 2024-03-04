package com.etag.stsyn.login

import androidx.compose.ui.test.junit4.createComposeRule
import com.etag.stsyn.ui.screen.login.LoginScreen
import org.junit.Rule
import org.junit.Test

//@HiltAndroidTest
class LoginTest {

    /*@get:Rule(order = 1)
    var hiltTestRule = HiltAndroidRule(this)*/

    @get:Rule(order = 1)
    val rule = createComposeRule()

    /*@Before
    fun setUp() {
        hiltTestRule.inject()
    }*/

    @Test
    fun enterPassword() {
        rule.setContent {
            LoginScreen(navigateToLoginContentScreen = { /*TODO*/ })
        }
    }
}