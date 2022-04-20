package com.example.URL.service;


import org.springframework.stereotype.Service;

import com.example.URL.model.Url;
import com.example.URL.model.UrlDto;

@Service
public interface UrlService {
	public Url generateShortLink(UrlDto urlDto);
	public Url persistShortLink(Url url);
	public Url getEncodedUrl(String url);
	public void deleteShortLink(Url url);
}
