package io.r3chain.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * Checkboxes allow users to select one or more items from a set. Checkboxes can turn an option on
 * or off.

 * @see [TriStateCheckbox] if you require support for an indeterminate state, or more advanced
 * color customization between states.
 *
 * @param checked whether Checkbox is checked or unchecked
 * @param modifier Modifier to be applied to the layout of the checkbox
 * @param label a text near the Checkbox
 * @param enabled whether the component is enabled or grayed out
 * @param interactionSource the [MutableInteractionSource] representing the stream of
 * [Interaction]s for this Checkbox. You can create and pass in your own remembered
 * [MutableInteractionSource] if you want to observe [Interaction]s and customize the
 * appearance / behavior of this Checkbox in different [Interaction]s.
 * @param onCheckedChange callback to be invoked when checkbox is being clicked,
 * therefore the change of checked state in requested.  If null, then this is passive
 * and relies entirely on a higher-level component to control the "checked" state.
 */
@Composable
fun CheckboxLabel(
    checked: Boolean,
    modifier: Modifier = Modifier,
    label: String? = null,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    onCheckedChange: (Boolean) -> Unit
) {
    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = Modifier
            .then(modifier)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                onClickLabel = null,
                role = Role.Checkbox,
                onClick = {
                    onCheckedChange(!checked)
                }
            )
    ) {
        if (label != null) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 44.dp)
            )
        }
        Checkbox(
            checked = checked,
            enabled = enabled,
            interactionSource = interactionSource,
            onCheckedChange = onCheckedChange
        )
    }
}


@Preview(
    name = "Demo"
)
@Composable
private fun InputsPreview() {
    MaterialTheme {
        Surface {
            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CheckboxLabel(
                    checked = true,
                    label = "Select me"
                ) {}
            }
        }
    }
}
