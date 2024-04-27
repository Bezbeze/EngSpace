package com.engSpace.service;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.engSpace.dto.response.WordTranslateResponse;

import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Data
@NoArgsConstructor
public class ApiTranslaterSerser {

	@Value("${translate_url}")
	String translateUrl;

	@Value("${api_key}")
	String apiKey;
	
	RestTemplate restTemplate = new RestTemplate();

	public WordTranslateResponse getTranslate(String text) {
		

		HttpHeaders headers = new HttpHeaders();
		headers.add("content-type", "application/x-www-form-urlencoded");
		headers.add("X-RapidAPI-Key", apiKey);
		headers.add("X-RapidAPI-Host", "google-translate113.p.rapidapi.com");
		HttpEntity<String> httpEntity=new HttpEntity<String>("from=en&to=ru&text=" + text, headers);

		ResponseEntity<WordTranslateResponse> response = 
				restTemplate.exchange(translateUrl, HttpMethod.POST, httpEntity,
				WordTranslateResponse.class);
		return response.getBody();
	}
}
