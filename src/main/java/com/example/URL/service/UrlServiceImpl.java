	package com.example.URL.service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.example.URL.model.Url;
import com.example.URL.model.UrlDto;
import com.example.URL.repository.UrlRepository;
import com.google.common.hash.Hashing;

@Component
public class UrlServiceImpl implements UrlService {
	
	
	private final UrlRepository urlRepository;
	
	public UrlServiceImpl(
			UrlRepository urlRepository) {
		this.urlRepository=urlRepository;
	}

	@Override
	public Url generateShortLink(UrlDto urlDto) {
		if(StringUtils.isAlphanumeric(urlDto.getUrl()))
		{
			return null;
		}
		
		String encodeUrl = encodeUrl(urlDto.getUrl());
		Url urlToPersist = extracted(urlDto, encodeUrl);
		Url urlToRet = persistShortLink(urlToPersist);
		return urlToRet;
	}
	
	private String encodeUrl(String url) {
		String encodeUrl = "";
		LocalDateTime time = LocalDateTime.now();
		encodeUrl = Hashing.murmur3_32()
				.hashString(url.concat(time.toString()), StandardCharsets.UTF_8)
				.toString();
		return encodeUrl;
	}
	
	private Url extracted(UrlDto urlDto, String encodeUrl) {
		Url urlToPersist = new Url();
		urlToPersist.setCreationDate(LocalDateTime.now());
		urlToPersist.setOriginalUrl(urlDto.getUrl());
		urlToPersist.setShortLink(encodeUrl);
		urlToPersist.setExpirationDate(getExpirationDate(urlDto.getExpirationDate(),urlToPersist.getCreationDate()));
		return urlToPersist;
	}
	
	@Override
	public Url persistShortLink(Url url) {
		Url urlToRet = urlRepository.save(url);
		return urlToRet;
	}
	
	private LocalDateTime getExpirationDate(String expirationDate, LocalDateTime creationDate) {
		if(StringUtils.isBlank(expirationDate))
		{
			return creationDate.plusSeconds(120);
		}
		LocalDateTime expirationDateToRet = LocalDateTime.parse(expirationDate);
		return expirationDateToRet;
		
	}
	
	@Override
	public Url getEncodedUrl(String url) {
		Url urlToRet = urlRepository.findByShortLink(url);
		return urlToRet;
	}
	
	@Override
	public void deleteShortLink(Url url) {
		urlRepository.delete(url);
		
	}
	
	

}
