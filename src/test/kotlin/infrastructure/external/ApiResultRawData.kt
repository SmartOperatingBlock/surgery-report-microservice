/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package infrastructure.external

object ApiResultRawData {
    val healthcareUserResultRawData = """
        {
            "taxCode": "RSSMRA98E18F205J",
            "name": "Mario",
            "surname": "Rossi",
            "height": 170.5,
            "weight": 70.2,
            "birthdate": "2000-01-01",
            "bloodGroup": "A_POSITIVE"
        }
    """.trimIndent()

    val roomTrackingResultRawData = """
        {
            "entries": [
                {
                    "dateTime": "2023-03-14T22:39:58.295Z",
                    "roomId": "room",
                    "healthProfessionalId": "12345678",
                    "trackingType": "ENTER"
                },
                {
                    "dateTime": "2023-03-14T22:41:20.136Z",
                    "roomId": "room",
                    "healthProfessionalId": "12345678",
                    "trackingType": "EXIT"
                }
            ],
            "total": 2
        }
    """.trimIndent()

    val roomEnvironmentalResultRawData = """
        {
            "entries": [
                {
                    "entry": {
                        "temperature": {
                            "value": 55.5,
                            "unit": "CELSIUS"
                        },
                        "humidity": 34.2,
                        "luminosity": null,
                        "presence": false
                    },
                    "date": "2023-03-14T22:39:58.295Z"
                },
                {
                    "entry": {
                        "temperature": {
                            "value": 55.7,
                            "unit": "CELSIUS"
                        },
                        "humidity": 36.2,
                        "luminosity": {
                            "value": 150.0,
                            "unit": "LUX"
                        },
                        "presence": true
                    },
                    "date": "2023-03-14T22:41:20.136Z"
                }
            ],
            "total": 2
        }
    """.trimIndent()
}
