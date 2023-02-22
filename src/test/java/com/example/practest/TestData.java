package com.example.practest;

import java.util.HashMap;
import java.util.Map;

public class TestData {
    public static Map<String, String> getTestData() {
        Map<String, String> data = new HashMap<>();

        data.put("device_01",
                "{\"recordType\": \"xxx\","
                + "\"deviceId\": \"357370040159770\","
                + "\"eventDateTime\": \"2014-05-12T05:09:48Z\","
                + "\"fieldA\": 68,"
                + "\"fieldB\": \"xxx\","
                + "\"fieldC\": 123.45}");


        data.put("device_02",
                "{\"recordType\": \"xxx\","
                        + "\"deviceId\": \"457370040159880\","
                        + "\"eventDateTime\": \"2014-05-12T05:09:48Z\","
                        + "\"fieldA\": 78,"
                        + "\"fieldB\": \"xxx\","
                        + "\"fieldC\": 223.45}");


        data.put("device_03_with_empty_RecordType_eventDateTime_fieldB",
                        "{\"recordType\":\"\","
                        + "\"deviceId\": \"567370040159880\","
                        + "\"eventDateTime\": \"\","
                        + "\"fieldA\": 99,"
                        + "\"fieldB\":\"\","
                        + "\"fieldC\": 224.45}");


        data.put("device_03_with_null_RecordType_fieldA_fieldB",
                        "{"
                        // + "\"RecordType\": \"xxx\","
                        + "\"deviceId\": \"567370040159880\","
                        + "\"eventDateTime\": \"2014-05-12T05:09:48Z\","
                        // + "\"FieldA\": 99,"
                        // + "\"FieldB\": \"xxx\","
                        + "\"fieldC\": 123.45}"
        );
        return data;
    }

    public static String getLoginDetailsJsonString(){
        return "{\"username\": \"foo\",\"password\": \"foo\"}";
    }


}
