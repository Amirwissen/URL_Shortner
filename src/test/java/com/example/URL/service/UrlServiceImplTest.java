package com.example.URL.service;

import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;

import com.example.URL.model.Url;
import com.example.URL.model.UrlDto;
import com.example.URL.repository.UrlRepository;

@ContextConfiguration(classes = { UrlServiceImpl.class })
@ExtendWith(MockitoExtension.class)
class UrlServiceImplTest {
	@Mock
	private UrlRepository urlRepository;

	@InjectMocks
	private UrlServiceImpl urlServiceImpl;

	@Test
	void test_persistShortLink() {
		Url url = new Url(1L, "https://www.google.com", "312adbe3", LocalDateTime.MIN, LocalDateTime.MAX);
		Mockito.when(urlRepository.save(Mockito.any(Url.class))).thenReturn(url);

		Url actual = urlServiceImpl.persistShortLink(url);
		Assertions.assertNotNull(actual);
	

		Mockito.verify(urlRepository, Mockito.times(1)).save(Mockito.any(Url.class));
	}

	@Test
	void test_getEncodedUrl() {
		Url url = new Url(1L, "https://www.google.com", "312adbe3", LocalDateTime.MIN, LocalDateTime.MAX);
		Mockito.when(urlRepository.findByShortLink("312adbe3")).thenReturn(url);

		Url actual = urlServiceImpl.getEncodedUrl("312adbe3");
		Assertions.assertNotNull(actual);

		Mockito.verify(urlRepository, Mockito.times(1)).findByShortLink("312adbe3");
	}

	@Test
	void test_deleteShortLink() {
		Url url = new Url(1L, "https://www.google.com", "312adbe3", LocalDateTime.MIN, LocalDateTime.MAX);

		urlServiceImpl.deleteShortLink(url);

		Mockito.verify(urlRepository, Mockito.times(1)).delete(url);
	}

	@Test 
	void test_generateShortLink() {
		
		UrlDto urlDto = new UrlDto("https://www.google.com",String.valueOf(LocalDateTime.now()));
		Mockito.when(urlRepository.save(Mockito.any(Url.class))).thenReturn(new Url(1L,"","",LocalDateTime.now(),LocalDateTime.now()));
		Url url = urlServiceImpl.generateShortLink(urlDto);
		org.assertj.core.api.Assertions.assertThat(url);
		
	}
	/*@Test
	void test_generateShortLinkForNull() {
	/*
		Url url = new Url(1L, "https://www.google.com", "312adbe3", LocalDateTime.MIN, LocalDateTime.MAX);
		UrlDto urlDto = new UrlDto("https://www.google.com", String.valueOf(LocalDateTime.MAX));
		Mockito.when(urlRepository.findByShortLink("312adbe3")).thenReturn(url);
		Url actual = urlServiceImpl.generateShortLink(urlDto);
		assertTrue(StringUtils.isNotEmpty("https://www.google.com"));
		*/
		
		// given
		
		// when
		
		
		// then
		Url url = new Url(1L, "https://www.google.com", "312adbe3", LocalDateTime.MIN, LocalDateTime.MAX);
		//UrlDto urlDto = new UrlDto("https://www.google.com", String.valueOf(LocalDateTime.MAX));
		/*Mockito.when(urlRepository.findByShortLink("312adbe3")).thenReturn(url);
		
		assertTrue(StringUtils.isBlank(null));
	}*/

}
