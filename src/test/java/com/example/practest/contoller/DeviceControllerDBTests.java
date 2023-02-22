package com.example.practest.contoller;

import com.example.practest.TestData;
import com.example.practest.dto.DeviceDto;
import com.example.practest.model.Device;
import com.example.practest.repository.DeviceRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.isA;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@ComponentScan(basePackages = {"com.example"})
public class DeviceControllerDBTests {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    DeviceRepository deviceRepository;

    @Before
    public void setup() {
        deviceRepository.deleteAll();
    }
    @Test
    public void saveAndGetDeviceByIdFromDBTest() throws Exception{

        // Create device 01 with DeviceId: 357370040159770. Please refer to the TestData class for details.
        String accessToken = obtainAccessToken("foo", "foo");
        String resultDeviceId01 = mockMvc.perform(post("/device")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestData.getTestData().get("device_01")))
                .andDo(print())
                //Todo: should this be isCreated or isOk? Need to get this clarified.
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();

        //Create device 02 with DeviceId: 457370040159880. Please refer to the TestData class for details.
        String resultDeviceId02 = mockMvc.perform(post("/device")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestData.getTestData().get("device_02")))
                .andDo(print())
                //Todo: should this be isCreated or isOk? Need to get this clarified.
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();

        List<DeviceDto> actualDeviceList = objectMapper.readValue(mockMvc.perform(get("/device")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString(),new TypeReference<List<DeviceDto>>(){});

        //Assertions to test the device ID received via the end point /device (/device fetches data from the DB)
        //           VS the device ID given as input (refer to the InputData class for details) is indeed the same.
        //           Plz refer to the DeviceController class for further details.
        assertEquals(resultDeviceId01, actualDeviceList.get(0).getDeviceId());
        assertEquals(resultDeviceId02, actualDeviceList.get(1).getDeviceId());

        //Assertion to test if the devices are indeed saved in the DB (checked via DeviceRepository).
        assertEquals(true, deviceRepository.findById(resultDeviceId01).isPresent());
        assertEquals(true, deviceRepository.findById(resultDeviceId02).isPresent());
    }

    @Test
    public void saveAndGetAllDevicesFromDBTest() throws Exception {
        String accessToken = obtainAccessToken("foo", "foo");
/*        DeviceDto expectedDeviceDto01 = TestData.getTestData().get("device_01");
        DeviceDto expectedDeviceDto02 = TestData.getTestData().get("device_02");*/

        //Inserting device 01 with the device ID: "357370040159770".
        String resultDeviceId01 = mockMvc.perform(post("/device")
                                .header("Authorization", "Bearer " + accessToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestData.getTestData().get("device_01")))
               .andReturn().getResponse().getContentAsString();

        //Inserting device 02 with the device ID: "457370040159880".
        String resultDeviceId02 = mockMvc.perform(post("/device")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestData.getTestData().get("device_02")))
                .andReturn().getResponse().getContentAsString();


        //Testing the extraction of all devices from the DB via the GET /device REST endpoint.
        mockMvc.perform(get("/device")
                .header("Authorization", "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.*", isA(ArrayList.class)))
                .andExpect(jsonPath("$.*", hasSize(2)))
                .andExpect(jsonPath("$.[0].deviceId").value(resultDeviceId01))
                .andExpect(jsonPath("$.[1].deviceId").value(resultDeviceId02))
                .andExpect(status().isOk());

        //Assertion to compare the TestData vs the devices in DB (extracted via DeviceRepository).
        assertEquals(resultDeviceId01, deviceRepository.findById(resultDeviceId01).get().getDeviceId());
        assertEquals(resultDeviceId02, deviceRepository.findById(resultDeviceId02).get().getDeviceId());

    }

    private String obtainAccessToken(String username, String password) throws Exception {

        MvcResult mvcResult = mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestData.getLoginDetailsJsonString())).andReturn();
        String resultString = mvcResult.getResponse().getContentAsString();
        JacksonJsonParser jsonParser = new JacksonJsonParser();
        return jsonParser.parseMap(resultString).get("token").toString();

    }

}
