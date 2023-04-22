package com.kasperserzysko.dynaapp.tools;

import com.kasperserzysko.dynaapp.exceptions.BadRequestException;
import com.kasperserzysko.dynaapp.exceptions.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Component
@Slf4j
public class Connector {


    public String getResponse(String apiUrl) throws IOException, NotFoundException, BadRequestException {
        StringBuilder response = new StringBuilder();

        URL url = new URL(apiUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        switch (conn.getResponseCode()){
            case 404:
                throw new NotFoundException("Couldn't find any data in selected date");
            case 400:
                throw new BadRequestException("Wrong parameters!");
            case 200:
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()))) {
                    for (String line; (line = reader.readLine()) != null; ) {
                        response.append(line);
                    }
                }
        }

        conn.disconnect();
        return response.toString();
    }
}
