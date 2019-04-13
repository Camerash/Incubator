package com.camerash.incubator

import java.io.Serializable

/**
 * @author Camerash
 */
data class Topping(
    val name: String,
    val pickerDrawRes: Int,
    val pizzaTopDrawRes: Int
): Serializable