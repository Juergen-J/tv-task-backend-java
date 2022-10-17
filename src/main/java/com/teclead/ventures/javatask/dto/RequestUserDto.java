package com.teclead.ventures.javatask.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Data
@Builder
public class RequestUserDto {

    @JsonProperty("name")
    @NotEmpty(message = "The field should not be empty")
    private String name;

    @JsonProperty("vorname")
    @NotEmpty(message = "The field should not be empty")
    private String vorname;

    @JsonProperty("email")
    @NotEmpty(message = "The field should not be empty")
    @Pattern(regexp = "^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,10}$", message = "Email is not valid. Please, try again")
    private String email;
}
