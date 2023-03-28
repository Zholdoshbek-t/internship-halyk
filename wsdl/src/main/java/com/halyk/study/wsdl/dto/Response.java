package com.halyk.study.wsdl.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {

    private String message;

    private Object data;

    private Status status;

    @JsonProperty(value = "employee_id")
    private Long employeeId;

    @JsonProperty(value = "first_name")
    private String firstName;

    @JsonProperty(value = "last_name")
    private String lastName;

    @JsonProperty(value = "middle_name")
    private String middleName;

    private Integer age;

    @JsonProperty(value = "date_of_birth")
    private LocalDate dateOfBirth;

    @JsonProperty(value = "department_id")
    private Long departmentId;

    @JsonProperty(value = "department_name")
    private String departmentName;

    @JsonProperty(value = "position_id")
    private Long positionId;

    @JsonProperty(value = "position_name")
    private String positionName;

}
