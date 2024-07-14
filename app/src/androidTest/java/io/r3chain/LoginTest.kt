package io.r3chain

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTextReplacement
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.r3chain.feature.root.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testSuccessfulLogin() {
//        // Ввод имени пользователя
//        composeTestRule.onNodeWithText("Email")
//            .performTextReplacement("testuser")
//
//        // Ввод пароля
//        composeTestRule.onNodeWithText("Password")
//            .performTextReplacement("password123")

        // Нажатие кнопки "Login"
        composeTestRule.onNodeWithText("Sign In")
            .performClick()

        // Ожидание завершения всех асинхронных операций и появления нужного текста
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodesWithText("Inventory").fetchSemanticsNodes().isNotEmpty()
        }

        // Проверка, что отображается нужный текст
        composeTestRule.onNodeWithText("Inventory").assertExists()
    }

}