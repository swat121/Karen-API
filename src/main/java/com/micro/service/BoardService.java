package com.micro.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final ConnectionService connectionService;
    private final ClientService clientService;

    public String makeRequest(String name, String module, String id, String endpoint, HttpMethod method, @Nullable HttpEntity<MultiValueMap<String, String>> requestEntity) {
        String baseUrl = clientService.getIpOfAvailableClient(name) + ":80/api/v1/" + endpoint;

        String requestUrl = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .path("/{module}/{id}")
                .buildAndExpand(module, id)
                .toUriString();

        return connectionService.requestForBoard(requestUrl, method, requestEntity, String.class);
    }

    public String makeSensorRequest(String name, String module, String id) {
        return makeRequest(name, module, id, "sensors", HttpMethod.GET, null);
    }

    public String makeTrackerRequest(String name, String module, String id) {
        return makeRequest(name, module, id, "trackers", HttpMethod.GET, null);
    }

    public String makeSwitcherRequest(String name, String module, String id) {
        return makeRequest(name, module, id, "switchers", HttpMethod.PUT, null);
    }


//    public String message(String name, String key, String text) {
//        LOG.info("======================== Micro controller service: message " + key + " response from service - " + name + " | " + key + " ========================");
//        String ip = clientService.getIpOfAvailableClient(name);
//        DataResponse dataResponse = connectionService.getResponseFromMicro(ip + ":80/", key + "?text=" + text, DataResponse.class);
//        return dataResponse.getMessage();
//    }
//
//    public String setting(String name, String key) {
//        LOG.info("======================== Micro controller service: setting - " + name + " | " + key + " ========================");
//        String ip = clientService.getIpOfAvailableClient(name);
//        DataResponse dataResponse = connectionService.getResponseFromMicro(ip + ":80/", key, DataResponse.class);
//        return switch (key) {
//            case "relay1" -> dataResponse.getRelay1();
//            case "relay2" -> dataResponse.getRelay2();
//            case "relay3" -> dataResponse.getRelay3();
//            case "backlight" -> dataResponse.getBacklight();
//            default -> throw new ApiRequestException(String.format("Command not found: [%s]", key));
//        };
//    }
//
//    public String sensor(String name, String key) {
//        LOG.info("======================== Micro controller service: sensor - " + name + " | " + key + " ========================");
//        String ip = clientService.getIpOfAvailableClient(name);
//        DataResponse dataResponse = connectionService.getResponseFromMicro(ip + ":80/", key, DataResponse.class);
//        return switch (key) {
//            case "temperature" -> dataResponse.getTemperature();
//            case "light" -> dataResponse.getLight();
//            default -> throw new ApiRequestException("Opps");
//        };
//    }
}