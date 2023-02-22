package com.example.practest.service;

import com.example.practest.dto.DeviceDto;
import com.example.practest.model.Device;
import com.example.practest.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class DeviceService {
    @Autowired
    DeviceRepository repository;

    public DeviceDto saveDevice(DeviceDto deviceDto){
        Device device = repository.save(getDeviceEntityFromDto(deviceDto));
        return getDeviceDtoFromEntity(device);
    }

    public List<DeviceDto> getAllDevices() {
        List<DeviceDto> deviceDtoList = new ArrayList<>();
        for (Device device :
                repository.findAll()) {
            deviceDtoList.add(getDeviceDtoFromEntity(device));
        }
        return deviceDtoList;
    }

    private DeviceDto getDeviceDtoFromEntity(Device device) {
        return new DeviceDto(device.getRecordType(),
                device.getDeviceId(),
                device.getEventDateTime().toString(),
                device.getFieldA(),
                device.getFieldB(),
                device.getFieldC());
    }

    public Device getDeviceEntityFromDto(com.example.practest.dto.DeviceDto deviceDto) {
        return new Device(deviceDto.getRecordType(),
                deviceDto.getDeviceId(),
                ZonedDateTime.parse(deviceDto.getEventDateTime(), DateTimeFormatter.ISO_DATE_TIME),
                deviceDto.getFieldA(),
                deviceDto.getFieldB(),
                deviceDto.getFieldC());
    }
}
