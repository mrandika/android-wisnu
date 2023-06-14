package space.mrandika.wisnu.ui.event

import space.mrandika.wisnu.model.event.Event

data class EventUiState (
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val event: Event? = null
)