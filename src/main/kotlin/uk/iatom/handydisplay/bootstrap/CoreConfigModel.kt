package uk.iatom.handydisplay.bootstrap

import kotlinx.serialization.Serializable

/** Configuration model for the core.properties file. */
@Serializable data class CoreConfigModel(val key: List<String>, val yay: String)
