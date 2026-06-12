package com.pfa.app.dto.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlayerRegisterRequest {

    @NotBlank(message = "Username is required")
    @Size(min = 4, max = 50)
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 100)
    private String password;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Middle name is required")
    private String middleName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    private String email; // Optional for minor players

    private String phone;

    @NotNull(message = "Date of birth is required")
    private LocalDate dateOfBirth;

    @NotNull(message = "Address is required")
    private String address;

    @NotNull(message = "State of origin is required")
    private String stateOfOrigin;

    @NotNull(message = "Country is required")
    private String country;

    @NotNull(message = "Date of birth is required")
    @Pattern(regexp = "^(TRUE|FALSE)")
    private String healthy;

    private String healthConcernDescription;

    private Long parentId; // Optional link to a parent profile during registration
}