package com.plex.multiplelists

import androidx.compose.foundation.interaction.FocusInteraction
import androidx.compose.foundation.interaction.MutableInteractionSource

data class PlexGrid(
    val grid: List<PlexRow>
)

data class PlexRow(
    val rows: List<PlexTile>
)

data class PlexTile(
    // Immutable business state
    val title: String,

    // Mutable, transient, view state. This does not respect unidirectional state flow principles.
    // For example, a button typically fires an onClick callback when the button is pressed and released, but it may still want to show that it is being pressed before this callback is fired.
    val mutableInteractionSource: MutableInteractionSource = MutableInteractionSource(),
    val focus: FocusInteraction.Focus = FocusInteraction.Focus(),
)