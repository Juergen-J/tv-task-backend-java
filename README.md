# TV---Task-Backend---Java

## Run the service locally

use commands

- `mvn spring-boot:run`   with port 8080

or

- `mvn spring-boot:run -Dspring.profiles.active=dev`   with port 8000

or

- `mvn spring-boot:run -Dspring.profiles.active=prod`     with port 9000

Alternatively, you can add `-Dspring.profiles.active=dev` to VM Options and then re-ran that configuration.

## DOCKER

### build docker container

execute in terminal

`docker build -t java-task:latest .`

### run the docker container with profile "dev" or "prod"

#### for "dev"

`docker run --name container-name -e "SPRING_PROFILES_ACTIVE=dev" -p 8081:8000 java-task`

#### for "prod"

`docker run --name container-name -e "SPRING_PROFILES_ACTIVE=prod" -p 8081:9000 java-task`

## API definition

For a detailed description of the API, see here:

- [Local openapi file](api-docs/openapi.yaml)
- [On Github](https://github.com/Juergen-J/tv-task-backend-java/blob/main/api-docs/user_v1_openapi.yaml)


| METHOD |            path             |              description |
|--------|:---------------------------:|-------------------------:|
| POST   |        /api/v1/user/        |             add new user |
| PUT    |     /api/v1/user/{id}/      |              update user |
| DELETE |     /api/v1/user/{id}/      |              delete user |
| GET    |     /api/v1/user/{id}/      |  get one user by user id |
| GET    | /api/v1/user/{vorname}/all/ | get all users by vorname |
| GET    |      /api/v1/user/all/      |            get all users |

### example

#### Get list of users

#### Request

`GET /api/v1/user/all/`

    curl -i -H 'Accept: application/json' http://localhost:8080/api/v1/user/all/

#### Response

    HTTP/1.1 200 OK
    Date: Thu, 24 Feb 2022 12:36:30 GMT
    Status: 200 OK
    Connection: close
    Content-Type: application/json
    Content-Length: 2

    []

### VALIDATION

- for the RequestUserDto are following fields required

``name: string`` 

``vorname: string``

``email: string``

- all fields must not be null or empty
- email must match the email format
- if either condition is not met, the user will receive a response with a description of the error


## SWAGGER

Start the server open the following link in browser:

- http://localhost:8080/swagger-ui  default
- http://localhost:8000/swagger-ui  dev
- http://localhost:9000/swagger-ui  prod



