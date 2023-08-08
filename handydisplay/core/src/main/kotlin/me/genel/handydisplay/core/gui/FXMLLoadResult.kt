package me.genel.handydisplay.core.gui

/**
 * Returned by `loadFXML(..)`. Handy way of getting access to otherwise obscured properties
 * like the FXML controller.
 */
data class FXMLLoadResult<C, T>(
        val controller: C,
        val rootComponent: T
                               )