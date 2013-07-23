package com.example.PointsGraph.model;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

/**
 * Ответ от сервера в вида:
 *
 * {"result":0,"response":{"points":[{"x":"1.23", "y":"2.44"},{"x":"2.17", "y":"3.66"}]}}
 *
 * парсится в данную модель
 */
public class ServerResponse {
    Integer result;
    Response response;

    public static ServerResponse getContactsObject(String contacts) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ServerResponse contactsObj = mapper.readValue(contacts, ServerResponse.class);

        return contactsObj;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    @Override
    public String toString() {
        return "ServerResponse{" +
                "result=" + result +
                ", response=" + response +
                '}';
    }
}

