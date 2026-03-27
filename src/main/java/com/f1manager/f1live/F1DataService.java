package com.f1manager.f1live;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class F1DataService {

    @Value("${f1.service.url:http://localhost:8000}")
    private String baseUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    @SuppressWarnings("unchecked")
    public Map<String, Object> getSchedule(int year) {
        try {
            return restTemplate.getForObject(baseUrl + "/api/schedule/" + year, Map.class);
        } catch (ResourceAccessException e) {
            return errorMap("Serviciul F1 nu este pornit. Ruleaza f1-service/start.bat");
        } catch (Exception e) {
            return errorMap("Eroare calendar: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getResults(int year, int round, String sessionType) {
        try {
            return restTemplate.getForObject(
                    baseUrl + "/api/results/" + year + "/" + round + "/" + sessionType,
                    Map.class);
        } catch (ResourceAccessException e) {
            return errorMap("Serviciul F1 nu este pornit. Ruleaza f1-service/start.bat");
        } catch (Exception e) {
            return errorMap("Eroare rezultate: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getSessionDrivers(int year, int round, String sessionType) {
        try {
            return restTemplate.getForObject(
                    baseUrl + "/api/drivers/" + year + "/" + round + "/" + sessionType,
                    Map.class);
        } catch (ResourceAccessException e) {
            return errorMap("Serviciul F1 nu este pornit. Ruleaza f1-service/start.bat");
        } catch (Exception e) {
            return errorMap("Eroare driveri: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getTelemetry(int year, int round, String sessionType,
                                             String driver, String driver2) {
        try {
            String url = baseUrl + "/api/telemetry/" + year + "/" + round + "/"
                    + sessionType + "/" + driver;
            if (driver2 != null && !driver2.isBlank()) {
                url += "?driver2=" + driver2;
            }
            return restTemplate.getForObject(url, Map.class);
        } catch (ResourceAccessException e) {
            return errorMap("Serviciul F1 nu este pornit. Ruleaza f1-service/start.bat");
        } catch (Exception e) {
            return errorMap("Eroare telemetrie: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getFastestLaps(int year, int round) {
        try {
            return restTemplate.getForObject(
                    baseUrl + "/api/fastest-laps/" + year + "/" + round,
                    Map.class);
        } catch (ResourceAccessException e) {
            return errorMap("Serviciul F1 nu este pornit. Ruleaza f1-service/start.bat");
        } catch (Exception e) {
            return errorMap("Eroare ture rapide: " + e.getMessage());
        }
    }

    private Map<String, Object> errorMap(String message) {
        Map<String, Object> err = new HashMap<>();
        err.put("error", message);
        return err;
    }
}
