package com.example.practest.contoller;

import com.example.practest.dto.DeviceDto;

import javax.validation.Valid;
import com.example.practest.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/")
public class DeviceController {

    @Autowired
    DeviceService service;

    /** The endpoint /nocontent via POST returns an HTTP 204 (NO_CONTENT),
     provided the requester is authorised via a JWT token (JWT Token should be available in the header as prerequisite) */
    @PostMapping("/nocontent")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public ResponseEntity<String> getNoContent(@RequestBody @Valid DeviceDto deviceDto){
        return new ResponseEntity<>("no content",HttpStatus.NO_CONTENT);
    }

    /** The endpoint "/echo" via POST returns the given device (payload) along with an HTTP 200,
     provided the requester is authorised via a JWT token (JWT Token should be available in the header as prerequisite) */
    @PostMapping("/echo")
    public ResponseEntity<Object> executeEcho(@RequestBody @Valid DeviceDto deviceDto){
        return new ResponseEntity<>(deviceDto,HttpStatus.OK);
    }

    /** The endpoint "/device" via POST will create a devices and persist it in the DB and return the device ID along with an HTTP 201 (CREATED),
     provided the requester is authorised via a JWT token (JWT Token should be available in the header as prerequisite) */
    @PostMapping("/device")
    public ResponseEntity<Object> saveDevice(@RequestBody @Valid DeviceDto deviceDto){
        return new ResponseEntity<>(service.saveDevice(deviceDto).getDeviceId(),HttpStatus.CREATED);
    }

    /** The endpoint "/device" via GET will return all the devices (extracted from the DB) along with an HTTP 200,
     provided the requester is authorised via a JWT token (JWT Token should be available in the header as prerequisite) */
    @GetMapping("/device")
    public ResponseEntity<Object> getAllDevices(){
        return new ResponseEntity<>(service.getAllDevices(),HttpStatus.OK);
    }

    /** This mapping is to ensure that all other quests, will be served with an HTTP 400 (BAD_REQUEST) instead of a 404 (NOT_FOUND),
     provided the requester is authorised via a JWT token (JWT Token should be available in the header as prerequisite) */
    @RequestMapping(value="**")
    public ResponseEntity<String>  getAnythingElse(){
        return new ResponseEntity<>(
                "Please check your request URL",
                HttpStatus.BAD_REQUEST);
    }
}
