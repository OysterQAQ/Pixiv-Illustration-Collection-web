package com.pixivic.util;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Component
public class OauthUtil {
    private MultiValueMap<String, String> formData;
    private MultiValueMap<String, String> header;
    @Getter
    private String access_token;

    @Autowired
    private OauthUtil(@Value("${pixiv.client_id}") String client_id, @Value("${pixiv.client_secret}") String client_secret, @Value("${pixiv.username}") String username, @Value("${pixiv.password}") String password, @Value("${pixiv.device_token}") String device_token, @Value("${pixiv.refresh_token}") String refresh_token) throws NoSuchAlgorithmException {
        formData = new LinkedMultiValueMap<>() {{
            add("client_id", client_id);
            add("client_secret", client_secret);
            add("grant_type", "password");
            add("username", username);
            add("password", password);
            add("device_token", device_token);
            add("get_secure_url", "true");
        }};
        header = new LinkedMultiValueMap<>() {{
            add("User-Agent", "PixivAndroidApp/5.0.93 (Android 5.1; m2)");
            add("Content-Type", "application/x-www-form-urlencoded");
            add("App-OS", "android");
            add("App-OS-Version", "5.1");
            add("App-Version", "5.0.93");
            add("Accept-Language", "zh_CN");
        }};
        refreshToken();
        formData.set("grant_type", "refresh_token");
        formData.add("refresh_token", refresh_token);
    }

    @Scheduled(cron = "0 0 */1 * * ?")
    private void refreshToken() {
        WebClient.create().post()
                .uri("https://oauth.api.pixivic.com/auth/token")
                .headers(httpHeaders -> httpHeaders.addAll(header)).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .headers(httpHeaders -> {
                    try {
                        String[] hash = gethash();
                        httpHeaders.addAll(new LinkedMultiValueMap<>() {{
                            add("Authorization", "Bearer " + getAccess_token());
                            add("X-Client-Time", hash[0]);
                            add("X-Client-Hash", hash[1]);
                        }});
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                })
                .body(BodyInserters.fromFormData(formData))
                .retrieve().bodyToMono(String.class).map(s -> {
            return s;
        }).subscribe(this::setAccess_token);
    }

    private void setAccess_token(String response) {
        int index = response.indexOf("\"access_token\":\"");
        access_token = response.substring(index + 16, index + 59);
    }

    public String[] gethash() throws NoSuchAlgorithmException {
        SimpleDateFormat simpleDateFormat;
        String fortmat = "yyyy-MM-dd'T'HH:mm:ssZZZZZ";
        simpleDateFormat = new SimpleDateFormat(fortmat, Locale.US);
        Date date = new Date();
        String time = simpleDateFormat.format(date);
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        String seed = time + "28c1fdd170a5204386cb1313c7077b34f83e4aaf4aa829ce78c231e05b0bae2c";
        byte[] digest = md5.digest(seed.getBytes());
        StringBuilder hash = new StringBuilder();
        for (byte b : digest) {
            hash.append(String.format("%02x", b));
        }
        return new String[]{time, hash.toString()};
    }
}
