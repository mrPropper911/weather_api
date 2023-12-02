# Weather REST API

Test assignment for internship. 

#### This is a simple implementation of a weather analyzer.
#### The application requests weather with the frequency specified in the settings from https://openweathermap.org/api for locations that are stored in the database and provides an API for working with it.

## 0. REST API endpoint
 
 Two ways see all endpoint:

- OpenApi(Swagger)- http://localhost:8080/swagger-ui-custom.html
- run [Postman json file](./weather_api.postman_collection.json) on postman

## 1. Setup Environment and System Variable
Make sure you have Java 8+ and Docker environment installed.

For example:

```shell
$ java -version
#openjdk version "21" 2023-09-19
#OpenJDK Runtime Environment (build 21+35-2513)
#OpenJDK 64-Bit Server VM (build 21+35-2513, mixed mode, sharing)
$ docker version
#...
#Server: Docker Desktop 4.15.0 (93002)
# Engine:
#  Version:          20.10.21
#  API version:      1.41 (minimum version 1.12)
#...
```
Also, for the application to work, the following system variables must be set:

Windows example:

```shell
$ setx.exe WEATHER_DATABASE_HOST "localhost"
$ setx.exe WEATHER_DATABASE_NAME "postgres"
$ setx.exe WEATHER_DATABASE_PASSWORD "change_password"
$ setx.exe WEATHER_DATABASE_PORT "5432"
$ setx.exe WEATHER_DATABASE_USERNAME "postgres"
# You can get a valid token at https://openweathermap.org/api
$ setx.exe WEATHER_OPENWEATHER_TOKEN "token_api"
```

## 2. Setup Project

- Clone the repository 

```shell
$ git clone https://github.com/mrPropper911/weather_api.git
$ cd weather_api
```
- Open the **weather_api** project in your favorite IDE.
- Run [Docker-compose](docker-compose.yml) for up PostgreSQL database.
The project root contains a [script](start.sh) to run  

```shell
$ bash.exe ./start.sh
#...
#[+] Running 5/5
# - Network weather_api_postgres   Created            0.7s
# - Volume "weather_api_postgres"  Created            0.0s
# - Volume "weather_api_pgadmin"   Created            0.0s
# - Container pgadmin_container    Started            3.7s
# - Container postgres_container   Started            3.6s
#...
```

- Run test. For Integration test need add token from https://openweathermap.org/api.
Also for Integration tests and test Database used [Testcontainers](https://testcontainers.com/) which uses Docker.
```shell
$ mvn clean -DWEATHER_OPENWEATHER_TOKEN=change_on_your_secret_token test
#...
#[INFO] Results:
#[INFO]
#[INFO] Tests run: 10, Failures: 0, Errors: 0, Skipped: 0
#[INFO]
#[INFO] ------------------------------------------------------------------------
#[INFO] BUILD SUCCESS
#[INFO] ------------------------------------------------------------------------
#[INFO] Total time:  34.752 s
#[INFO] Finished at: 2023-12-02T13:08:44+03:00
#[INFO] ------------------------------------------------------------------------
#...
```
Application has [two execution profile](../weather_api/demo/src/main/resources/application.yml):
- **develop** (default) (For development and testing in the IDE)
- **production** (Settings for building application to jar file)

Build application for production:
```shell
$ mvn 

```










