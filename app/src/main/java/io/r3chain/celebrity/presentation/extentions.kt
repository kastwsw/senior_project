package io.r3chain.celebrity.presentation

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import io.r3chain.R

/**
 * Запускает интент для открытия ссылки в ОС.
 *
 * @param link Строка ссылки, которую нужно открыть.
 */
fun Context.openLink(link: String) {
    try {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link)))
    } catch (e: Exception) {
        Toast.makeText(this, R.string.no_intent_receiver, Toast.LENGTH_SHORT).show()
    }
}