package me.genel.handydisplay.core.registry

/**
 * Declares that a type may be registered.
 *
 * @sample AbstractPlugin
 */
interface IRegistrable<out IRegistrable> {


    val registryName: String
}