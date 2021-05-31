package com.plex.multiplelists


import androidx.compose.foundation.interaction.FocusInteraction
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.key

suspend fun navigate(
    keyEvent: KeyEvent,
    focussedRowIndex: Int,
    focussedColumnIndex: Int,
    currentGrid: PlexGrid
): Pair<Int, Int> {
    val currentItem = currentGrid.grid[focussedRowIndex].rows[focussedColumnIndex]
    currentItem.mutableInteractionSource.emit(FocusInteraction.Unfocus(currentItem.focus))
    var nextSelectionRow = 0
    var nextSelectionColumn = 0
    if (keyEvent.key == Key(android.view.KeyEvent.KEYCODE_DPAD_DOWN)) {
        nextSelectionRow = focussedRowIndex + 1
        nextSelectionColumn = focussedColumnIndex
    } else if (keyEvent.key == Key(android.view.KeyEvent.KEYCODE_DPAD_UP)) {
        nextSelectionRow = focussedRowIndex - 1
        nextSelectionColumn = focussedColumnIndex
    } else if (keyEvent.key == Key(android.view.KeyEvent.KEYCODE_DPAD_RIGHT)) {
        nextSelectionRow = focussedRowIndex
        nextSelectionColumn = focussedColumnIndex + 1
    } else if (keyEvent.key == Key(android.view.KeyEvent.KEYCODE_DPAD_LEFT)) {
        nextSelectionRow = focussedRowIndex
        nextSelectionColumn = focussedColumnIndex - 1
    } else {
        nextSelectionRow = focussedRowIndex
        nextSelectionColumn = focussedColumnIndex
    }
    val nextItem = currentGrid.grid[nextSelectionRow].rows[nextSelectionColumn]
    nextItem.mutableInteractionSource.emit(nextItem.focus)
    return Pair(nextSelectionRow, nextSelectionColumn)
}