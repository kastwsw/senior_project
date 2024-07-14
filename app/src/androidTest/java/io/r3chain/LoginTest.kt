package io.r3chain

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.runComposeUiTest
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun useAppContext() {
        composeTestRule.setContent {
            Text(text = "Dkjdfkj sdkjf askdjf.")
        }
    }

    @Test
    @OptIn(ExperimentalTestApi::class)
    fun testSuccessfulLogin() = runComposeUiTest {
        setContent {
            MaterialTheme {
                Text("You can set any Compose content!")
            }
        }
    }
}