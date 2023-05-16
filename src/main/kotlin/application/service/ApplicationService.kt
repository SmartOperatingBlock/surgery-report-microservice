/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package application.service

/**
 * Interface that models an Application Service.
 * An Application Service handle application logic without business elements.
 * @param[T] the type returned by the service.
 */
fun interface ApplicationService<out T> {
    /**
     * Method to execute the application service.
     * @return the result of type [T]
     */
    fun execute(): T
}
