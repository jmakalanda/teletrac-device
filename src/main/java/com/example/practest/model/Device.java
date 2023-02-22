package com.example.practest.model;

import java.time.ZonedDateTime;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter @AllArgsConstructor @NoArgsConstructor
@Entity
public class Device {

    private String recordType;

    @Id
    private String deviceId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'",timezone = "Europe/London" )
    private ZonedDateTime eventDateTime;

    private Integer fieldA;

    private String fieldB;

    private Double fieldC;

}
