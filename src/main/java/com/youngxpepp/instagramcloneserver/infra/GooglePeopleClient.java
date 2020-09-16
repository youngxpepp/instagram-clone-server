package com.youngxpepp.instagramcloneserver.infra;

import java.io.IOException;
import java.security.GeneralSecurityException;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.people.v1.PeopleService;
import com.google.api.services.people.v1.model.Person;
import org.springframework.stereotype.Service;

@Service
public class GooglePeopleClient {

	private static final int CONNECTION_TIMEOUT = 3000;
	private static final int READ_TIMEOUT = 1000;
	private static final int NUMBER_OF_RETRIES = 1;

	private final PeopleService peopleService;

	private HttpRequestInitializer getHttpRequestInitializer(HttpRequestInitializer requestInitializer) {
		return request -> {
			if(requestInitializer != null) {
				requestInitializer.initialize(request);
			}
			request.setConnectTimeout(CONNECTION_TIMEOUT);
			request.setReadTimeout(READ_TIMEOUT);
			request.setNumberOfRetries(NUMBER_OF_RETRIES);
		};
	}

	GooglePeopleClient() throws GeneralSecurityException, IOException {
		HttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();
		JacksonFactory jacksonFactory = JacksonFactory.getDefaultInstance();
		this.peopleService = new PeopleService.Builder(transport, jacksonFactory, getHttpRequestInitializer(null))
			.setApplicationName("instagram-clone")
			.build();
	}

	Person getPerson(String accessToken) throws IOException {
		return this.peopleService.people().get("people/me")
			.setPersonFields("emailAddresses")
			.setRequestHeaders(new HttpHeaders().setAuthorization(accessToken))
			.execute();
	}
}
