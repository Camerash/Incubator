package com.camerash.incubator.model

import java.io.Serializable

/**
 * @author Camerash
 */
data class Topping(
    val name: String,
    val maxAmount: Int,
    val defaultAmount: Int,
    val color: Int
): Serializable