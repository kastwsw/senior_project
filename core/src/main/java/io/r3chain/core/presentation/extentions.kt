package io.r3chain.core.presentation

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import io.r3chain.core.R

/**
 * Запускает интент для открытия uri в ОС.
 *
 * @param uri Объект URI, который нужно открыть.
 */
fun Context.openUri(uri: Uri) {
    try {
        startActivity(Intent(Intent.ACTION_VIEW, uri))
    } catch (e: Exception) {
        Toast.makeText(this, R.string.no_intent_receiver, Toast.LENGTH_SHORT).show()
    }
}

/**
 * Запускает интент для открытия ссылки в ОС.
 *
 * @param link Строка ссылки, которую нужно открыть.
 */
fun Context.openLink(link: String) {
    openUri(Uri.parse(link))
}

