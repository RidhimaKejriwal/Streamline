package com.crm.streamline.forms;

import java.sql.Date;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class ProjectForm {
    
    @NotBlank(message="Name is Required")
    private String name;

    @NotBlank(message="Description is Required")
    private String description;

    @NotBlank(message="Status is Required")
    private String status;

    @NotNull(message="Amount Paid is required")
    private Double amountPaid;

    @NotNull(message="Start Date is Required")
    private Date startDate;

    private Date endDate;
}
