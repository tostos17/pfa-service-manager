package com.pfa.app.dto.response;

import com.pfa.app.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParentResponseDto {
    private Long id;
    private String title;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String address;
    private LocalDateTime registrationDate;
}