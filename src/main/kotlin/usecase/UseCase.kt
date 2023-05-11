/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package usecase

/**
 * This models a simple use case that return an object of type [X].
 */
fun interface UseCase<out X> {
    /** Execute the use case returning an object of type [X]. */
    fun execute(): X
}
