package com.example.practest.contoller;

import com.example.practest.TestData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/*added the folloing anotation instead of @WebMvcTest(controllers = JwtAuthController.class)
Due to just added for: UnsatisfiedDependencyException: Error creating bean with name 'jwtRequestFilter':
https://stackoverflow.com/questions/48078044/webmvctest-fails-with-java-lang-illegalstateexception-failed-to-load-applicati */

@SpringBootTest
@AutoConfigureMockMvc
@ComponentScan(basePackages = {"com.example"})

public class AuthControllerGenericTests {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void loginAndCreateTokenTest() throws Exception{
        mockMvc.perform(post("/login")
                                  .contentType(MediaType.APPLICATION_JSON)
                                  .content("{\"username\": \"foo\",\"password\": \"foo\"}"))
               .andDo(print())
               .andExpect(jsonPath("$.token").exists());
    }

    @Test
    public void tokenUnavailableAccessDeniedTest() throws Exception{
        mockMvc.perform(post("/nocontent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestData.getTestData().get("device_01")))
                .andExpect(status().is4xxClientError());

        mockMvc.perform(post("/echo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestData.getTestData().get("device_01")))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void tokenAvailableAccessGrantedTest() throws Exception{
        //Obtaining access token via the /login
        MvcResult mvcResult = mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestData.getLoginDetailsJsonString())).andReturn();
        String resultString = mvcResult.getResponse().getContentAsString();
        String accessToken = new JacksonJsonParser().parseMap(resultString).get("token").toString();

        //Test access to /nocontent with the access token set on the header
        mockMvc.perform(post("/nocontent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken)
                        .content(TestData.getTestData().get("device_01")))
                .andExpect(status().isNoContent());

        //Test access to /echo with the access token set on the header
        mockMvc.perform(post("/echo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken)
                        .content(TestData.getTestData().get("device_01")))
                .andExpect(status().isOk());

    }

}
