package com.example.whitefox.auth.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@Slf4j
public class SmsService {

    @Value("${smsindiahub.apikey:kcN8wmb81kGru7k5z66zpg}")
    private String apiKey;

    @Value("${smsindiahub.sid:IIDMTB}")
    private String sid;

    @Value("${smsindiahub.gwid:2}")
    private String gwid;

    private final RestTemplate restTemplate = new RestTemplate();

    public void sendOtp(String phone, String otp) {
        try {
            String msg = "The OTP for login is " + otp + ".IIDMT";
            
            String url = UriComponentsBuilder.fromUriString("https://cloud.smsindiahub.in/vendorsms/pushsms.aspx")
                    .queryParam("APIKey", apiKey)
                    .queryParam("msisdn", phone)
                    .queryParam("sid", sid)
                    .queryParam("msg", msg)
                    .queryParam("fl", "0")
                    .queryParam("gwid", gwid)
                    .toUriString();
                    
            String response = restTemplate.getForObject(url, String.class);
            log.info("SMS sent to {}: {}", phone, response);
        } catch (Exception e) {
            log.error("Failed to send SMS to {}: {}", phone, e.getMessage());
            // In a real app we might throw an exception, but for now we just log it
            // to prevent blocking if the SMS gateway is down during testing.
        }
    }
}
