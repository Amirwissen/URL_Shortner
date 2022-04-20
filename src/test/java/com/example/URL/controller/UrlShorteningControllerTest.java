 package com.example.URL.controller;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.example.URL.contoller.UrlShorteningController;
import com.example.URL.model.Url;
import com.example.URL.model.UrlDto;
import com.example.URL.model.UrlResponseDto;
import com.example.URL.service.UrlService;
import com.fasterxml.jackson.databind.ObjectMapper;

@ContextConfiguration(classes = { UrlShorteningController.class })
@ExtendWith(MockitoExtension.class)
@WebMvcTest(UrlShorteningController.class)
class UrlShorteningControllerTest {
	@MockBean
	private UrlService urlService;

	@InjectMocks
	private UrlShorteningController urlShorteningController;

	@Autowired
	private MockMvc mockMvc;

	@Test
	void generateShortLink() throws Exception {
		Url url = new Url(1L, "https://www.google.com", "nasd123p", LocalDateTime.MAX, LocalDateTime.MIN);
		UrlDto urlDto = new UrlDto("https://www.google.com", String.valueOf(LocalDateTime.MAX));
		Url actual = urlService.generateShortLink(urlDto);
		//Mockito.when(urlService.generateShortLink(urlDto)).thenReturn(url);
		UrlResponseDto urlResponseDto = new UrlResponseDto();
		//Mockito.when(actual).thenReturn(urlResponseDto);
		mockMvc.perform(MockMvcRequestBuilders.post("/urls").contentType(MediaType.APPLICATION_JSON)				
				.content(asJsonString(urlDto))).andExpect(MockMvcResultMatchers.status().isCreated());
	}

	
	@Test
	void negativeSynario() throws Exception {
		Url url = new Url(1L,null,null,null,null);
		UrlDto urlDto = new UrlDto("https://www.google.com", String.valueOf(LocalDateTime.MAX));
		Mockito.when(urlService.generateShortLink(urlDto)).thenReturn(url);
		
		mockMvc.perform(MockMvcRequestBuilders.post("/urls").contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(urlDto))).andExpect(MockMvcResultMatchers.status().isNoContent())
				.andExpect(MockMvcResultMatchers.status().is(204));
	}
	
	private static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Test
    void testredirectToOriginalUrl() throws Exception {
		
	}
	
}
