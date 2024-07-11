package com.crm.streamline.forms;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EmployeeForm {
    
    @NotBlank(message="First Name is Required")
    private String firstName;

    @NotBlank(message="Last Name is Required")
    private String lastName;

    @NotBlank(message="Email is required")
    @Email(message="Invalid Email Address")
    private String email;

    @NotBlank(message="Password is Required")
    private String password;

    @NotBlank(message="Employee Type is Required")
    private String employeeType;

    @NotBlank(message="Phone number is required")
    @Pattern(regexp="^[0-9]{10}$", message="Invalid Phone Number")
    private String phoneNumber;

    @NotBlank(message="Address is required")
    private String address;

    @NotBlank(message="City is Required")
    private String city;

    @NotBlank(message="State is Required")
    private String state;

}
