package com.example.practest.contoller;

import com.example.practest.TestData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
@ComponentScan(basePackages = {"com.example"})
public class DeviceControllerGenericTests {
    @Autowired
    MockMvc mockMvc;


    @Test
    public void authenticatedNoContentTest() throws Exception{
        String accessToken = obtainAccessToken("foo", "foo");
        mockMvc.perform(post("/nocontent")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestData.getTestData().get("device_01")))
               .andExpect(status().isNoContent());
    }

    /**Test to evaluate if the endpoint "/echo" returns the given device (payload) along with an HTTP 200.*/
    @Test
    public void authenticatedEchoTest() throws Exception{
        String accessToken = obtainAccessToken("foo", "foo");
        mockMvc.perform(post("/echo")
                                .header("Authorization", "Bearer " + accessToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestData.getTestData().get("device_01")))
               .andExpect(content().json(TestData.getTestData().get("device_01")));
    }

    /**Test to evaluate if the endpoint "/device" returns the device ID along with an HTTP 200.*/
    @Test
    public void authenticatedCreateDeviceTest() throws Exception{
        String accessToken = obtainAccessToken("foo", "foo");
        mockMvc.perform(post("/device")
                                .header("Authorization", "Bearer " + accessToken)
                                .contentType(MediaType.APPLICATION_JSON)
                        .content(TestData.getTestData().get("device_01")))
                .andExpect(content().string("357370040159770"))
                //Todo: should this be isCreated or isOk? Need to get this clarified.
                .andExpect(status().isCreated());
    }

    @Test
    public void authenticatedAllOtherEndpointsTest() throws Exception{
        String accessToken = obtainAccessToken("foo", "foo");

        //Test if the app gives us 400 (BadRequest) instead of a 404 (NotFound) for any other URI requests


        mockMvc.perform(post("/blah")
                                .header("Authorization", "Bearer " + accessToken)
                                .contentType(MediaType.APPLICATION_JSON)
                        .content(TestData.getTestData().get("device_01")))
               .andExpect(status().isBadRequest());


        mockMvc.perform(post("/")
                                .header("Authorization", "Bearer " + accessToken)
                                .contentType(MediaType.APPLICATION_JSON)
                            .content(TestData.getTestData().get("device_01")))
            .andExpect(status().isBadRequest());
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
