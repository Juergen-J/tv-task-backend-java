package com.teclead.ventures.javatask.controller;

import com.teclead.ventures.javatask.dto.ErrorDto;
import com.teclead.ventures.javatask.dto.RequestUserDto;
import org.junit.jupiter.api.*;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserControllerIntegrationTest {

    @LocalServerPort
    private int port;

    private final TestRestTemplate testRestTemplate = new TestRestTemplate();

    @BeforeEach
    void setUp() {
    }

    @Test
    @Order(1)
    void loadContext() {
        assertNotNull(testRestTemplate);
    }

    @Test
    @Order(1)
    void addUser_ShouldReturnUser() {
        RequestUserDto requestUserDto = RequestUserDto
                .builder()
                .name("Jurk")
                .vorname("Juergen")
                .email("juergen.jurk@mymail.de")
                .build();
        final var request = RequestEntity.post("").body(requestUserDto);

        final var response =
                testRestTemplate.exchange(createUrlWithRandomPort(""), HttpMethod.POST, request, Object.class);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    @Order(2)
    void addUser_ShouldReturnBadRequest_ValidationIsFailure() {
        RequestUserDto requestUserDto = RequestUserDto.builder().build();
        final var request = RequestEntity.post("").body(requestUserDto);

        final var response =
                testRestTemplate.exchange(createUrlWithRandomPort(""), HttpMethod.POST, request, Object.class);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @Order(4)
    void getAllUsers_ShouldReturnEmptyList() {
        final var response = testRestTemplate.getForEntity(createUrlWithRandomPort("/all/"), Object[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, response.getBody().length);
    }

    @Test
    @Order(5)
    void getAllUsers_ShouldReturnUsersList() {
        RequestUserDto requestUserDto = RequestUserDto
                .builder()
                .name("Jurk")
                .vorname("Juergen")
                .email("juergen.jurk@mymail.de")
                .build();
        final var request = RequestEntity.post("").body(requestUserDto);

        final var post =
                testRestTemplate.exchange(createUrlWithRandomPort(""), HttpMethod.POST, request, Object.class);
        assertEquals(HttpStatus.CREATED, post.getStatusCode());

        final var response = testRestTemplate.getForEntity(createUrlWithRandomPort("/all/"), Object[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().length);
    }

    @Test
    void getAllUsersByVorname_ShouldReturnUserList() {
        final var response = testRestTemplate.getForEntity(createUrlWithRandomPort("/juergen/all/"), Object[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().length);
    }

    @Test
    void getAllUsersByVorname_ShouldReturnEmptyList() {
        final var response = testRestTemplate.getForEntity(createUrlWithRandomPort("/any/all/"), Object[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, response.getBody().length);

    }

    @Test
    void getUserById_ShouldReturnUser() {
        final var response = testRestTemplate.getForEntity(createUrlWithRandomPort("/2/"), Object.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getUserById_ShouldReturnNotFound() {
        final var response = testRestTemplate.getForEntity(createUrlWithRandomPort("/10/"), Object.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("{errorMsg=User with id 10 doesn't exist}", response.getBody().toString());
    }

    @Test
    void updateUser_Success() {
        RequestUserDto requestUserDto = RequestUserDto
                .builder()
                .name("Juergen")
                .vorname("Jurk")
                .email("juergen.jurk@newmail.de")
                .build();
        final var request = RequestEntity.put("").body(requestUserDto);

        final var response =
                testRestTemplate.exchange(createUrlWithRandomPort("/2/"), HttpMethod.PUT, request, Object.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void updateUser_ShouldReturnNotFound() {
        RequestUserDto requestUserDto = RequestUserDto
                .builder()
                .name("Juergen")
                .vorname("Jurk")
                .email("juergen.jurk@newmail.de")
                .build();
        final var request = RequestEntity.put("").body(requestUserDto);

        final var response =
                testRestTemplate.exchange(createUrlWithRandomPort("/3/"), HttpMethod.PUT, request, Object.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @Order(3)
    void deleteUser_Success() {
        ErrorDto errorDto = new ErrorDto("");
        final var request = testRestTemplate
                .exchange(createUrlWithRandomPort("/1/"), HttpMethod.DELETE, new HttpEntity<ErrorDto>(errorDto), Object.class);

        assertEquals(HttpStatus.OK, request.getStatusCode());
    }

    @Test
    void deleteUser_UserNotFound() {
        ErrorDto errorDto = new ErrorDto("");
        final var request = testRestTemplate
                .exchange(createUrlWithRandomPort("/3/"), HttpMethod.DELETE, new HttpEntity<ErrorDto>(errorDto), Object.class);

        assertEquals(HttpStatus.NOT_FOUND, request.getStatusCode());

        assertEquals("{errorMsg=User with id 3 doesn't exist}", request.getBody().toString());
    }


    private String createUrlWithRandomPort(String uri) {
        return "http://localhost:" + port + "/api/v1/user/" + uri;
    }
}