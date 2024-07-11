package com.crm.streamline.forms;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class AdminForm {
    
    @NotBlank(message="Username is required")
    @Size(min=3, message="Minimum 3 characters required")
    private String name;

    @NotBlank(message="Email is required")
    @Email(message="Invalid email address")
    private String email;

    @NotBlank(message="Password is required")
    @Size(min=6, message="Minimum 6 characters required")
    private String password;

    @NotBlank(message="Company Name is required")
    private String companyName;

    @NotBlank(message="Company City is required")
    private String city;

    @NotBlank(message="Company State is required")
    private String state;

    @NotBlank(message="Company Address is required")
    private String address;

    @NotBlank(message="Phone Number is required")
    @Pattern(regexp="^[0-9]{10}$", message="Invalid Phone Number")
    private String phoneNumber;
}
