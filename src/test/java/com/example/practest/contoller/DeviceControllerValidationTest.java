package com.example.practest.contoller;

import com.example.practest.TestData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class DeviceControllerValidationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testTest(){
        ZonedDateTime zdt = ZonedDateTime.parse("2014-05-12T05:09:48Z", DateTimeFormatter.ISO_DATE_TIME);
        System.out.println(zdt);
    }
    @Test
    public void authenticatedNoContentRequestWithEmptyTest() throws Exception {
        String accessToken = obtainAccessToken("foo", "foo");
        MvcResult result = mockMvc.perform(post("/nocontent")
                    .header("Authorization", "Bearer " + accessToken)
                    .content(TestData.getTestData().get("device_03_with_empty_RecordType_eventDateTime_fieldB"))
                    .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest()).andReturn();
        System.out.println(result.getResolvedException().getMessage());

        // Test if the Fields RecordType, eventDateTime and FieldB are validated and the empty validation message is returned.
        assertTrue(result.getResolvedException().getMessage().contains("RecordType is a mandatory field, it can't be empty"));
        assertTrue(result.getResolvedException().getMessage().contains("EventDateTime is a mandatory field, it can't be empty"));
        assertTrue(result.getResolvedException().getMessage().contains("FieldB is a mandatory field, it can't be empty"));

        // not FieldC
        assertTrue(!result.getResolvedException().getMessage().contains("FieldC is a mandatory field, it can't be empty"));
    }
    @Test
    public void authenticatedNoContentRequestWithNullTest() throws Exception {
        String accessToken = obtainAccessToken("foo", "foo");

        MvcResult result = mockMvc.perform(post("/nocontent")
                        .content(TestData.getTestData().get("device_03_with_null_RecordType_fieldA_fieldB"))
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                  .andDo(print())
                  .andExpect(status().isBadRequest())
                  .andReturn();
        //debug message
        System.out.println(result.getResolvedException().getMessage());

        assertTrue(result.getResolvedException().getMessage().contains("RecordType is a mandatory field, it can't be null"));
        assertTrue(result.getResolvedException().getMessage().contains("FieldA is a mandatory field, it can't be null"));
        assertTrue(result.getResolvedException().getMessage().contains("FieldB is a mandatory field, it can't be null"));

        assertTrue(!result.getResolvedException().getMessage().contains("EventDateTime is a mandatory field, it can't be null"));
    }

    private String obtainAccessToken(String username, String password) throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"foo\",\"password\": \"foo\"}")).andReturn();
        String resultString = mvcResult.getResponse().getContentAsString();
        JacksonJsonParser jsonParser = new JacksonJsonParser();
        return jsonParser.parseMap(resultString).get("token").toString();

    }
}
