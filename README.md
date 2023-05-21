# Surgery Report microservice

![workflow status](https://github.com/smartoperatingblock/surgery-report-microservice/actions/workflows/build-and-deploy.yml/badge.svg)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
![Version](https://img.shields.io/github/v/release/smartoperatingblock/surgery-report-microservice?style=plastic)

[![codecov](https://codecov.io/gh/SmartOperatingBlock/surgery-report-microservice/branch/main/graph/badge.svg?token=7GL0gAUkQp)](https://codecov.io/gh/SmartOperatingBlock/surgery-report-microservice)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=SmartOperatingBlock_surgery-report-microservice&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=SmartOperatingBlock_surgery-report-microservice)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=SmartOperatingBlock_surgery-report-microservice&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=SmartOperatingBlock_surgery-report-microservice)
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=SmartOperatingBlock_surgery-report-microservice&metric=duplicated_lines_density)](https://sonarcloud.io/summary/new_code?id=SmartOperatingBlock_surgery-report-microservice)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=SmartOperatingBlock_surgery-report-microservice&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=SmartOperatingBlock_surgery-report-microservice)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=SmartOperatingBlock_surgery-report-microservice&metric=sqale_index)](https://sonarcloud.io/summary/new_code?id=SmartOperatingBlock_surgery-report-microservice)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=SmartOperatingBlock_surgery-report-microservice&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=SmartOperatingBlock_surgery-report-microservice)

This is the repository of the Surgery Report microservice of the Smart Operating Block project.

## Usage
You need to specify the following environment variable:
- `BOOTSTRAP_SERVER_URL`: the kafka connection endpoint
- `BUILDING_MANAGEMENT_MICROSERVICE_URL`: the url of the Building Management microservice
- `MONGODB_CONNECTION_STRING`: the mongodb's connection string
- `PATIENT_MANAGEMENT_INTEGRATION_MICROSERVICE_URL`: the url of the Patient Management Integration microservice
- `SCHEMA_REGISTRY_URL`: the kafka schema registry url
- `STAFF_TRACKING_MICROSERVICE_URL`: the url of the Staff Tracking microservice

If you want to run it via docker container:
1. Provide a `.env` file with all the environment variable described above
2. Run the container with the command:
   ```bash
    docker run ghcr.io/smartoperatingblock/surgery-report-microservice:latest
    ```
    1. If you want to try the REST-API from the external you need to provide a port mapping to port 3000.
    2. If you want to pass an environment file whose name is different from `.env` use the `--env-file <name>` parameter.

## Documentation
- Check out the website [here](https://smartoperatingblock.github.io/surgery-report-microservice/)
- Direct link to the *REST-API* documentation [here](https://smartoperatingblock.github.io/surgery-report-microservice/documentation/openapi-doc/)
- Direct link to the *Code* documentation [here](https://smartoperatingblock.github.io/surgery-report-microservice/documentation/code-doc/)