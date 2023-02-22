package com.example.practest.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter @AllArgsConstructor @NoArgsConstructor
public class DeviceDto {
    @NotEmpty(message = "RecordType is a mandatory field, it can't be empty")
    @NotNull(message = "RecordType is a mandatory field, it can't be null")
    private String RecordType;


    @NotEmpty(message = "deviceId is a mandatory field, it can't be empty")
    //   @NotNull(message = "DeviceId is a mandatory field, it can't be null")
    private String DeviceId;

    @NotEmpty(message = "EventDateTime is a mandatory field, it can't be empty")
    @NotNull(message = "EventDateTime is a mandatory field, it can't be null")
    private String EventDateTime;

    @NotNull(message = "FieldA is a mandatory field, it can't be null")
    private Integer FieldA;

    @NotEmpty(message = "FieldB is a mandatory field, it can't be empty")
    @NotNull(message = "FieldB is a mandatory field, it can't be null")
    private String FieldB;

    @NotNull(message = "FieldC is a mandatory field, it can't be null")
    private Double FieldC;

}
