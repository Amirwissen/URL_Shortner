package com.example.URL.contoller;

import java.io.IOException;

import java.time.LocalDateTime;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.URL.model.Url;
import com.example.URL.model.UrlDto;
import com.example.URL.model.UrlErrorResponseDto;
import com.example.URL.model.UrlResponseDto;
import com.example.URL.service.UrlService;

@RestController
public class UrlShorteningController {

	@Autowired
	private UrlService urlService;
	
	@PostMapping("/urls")
	public ResponseEntity<?> generateShortLink(@RequestBody UrlDto urlDto){
		Url urlToRet = urlService.generateShortLink(urlDto);
		
		if(urlToRet != null) {
			UrlResponseDto urlResponseDto = new UrlResponseDto();
			urlResponseDto.setOriginalUrl(urlToRet.getOriginalUrl());
			urlResponseDto.setExpirationDate(urlToRet.getExpirationDate());
			urlResponseDto.setShortLink(urlToRet.getShortLink());
			return new ResponseEntity<UrlResponseDto>(urlResponseDto,HttpStatus.CREATED);
		}
		
		UrlErrorResponseDto urlErrorResponseDto = new UrlErrorResponseDto();
		urlErrorResponseDto.setStatus(204);
		urlErrorResponseDto.setError("There was an error processing your request, Please try again.");
		return new ResponseEntity<UrlErrorResponseDto>(urlErrorResponseDto,HttpStatus.NO_CONTENT);
	}
	
	@GetMapping("/urls/{shortLink}")
	public ResponseEntity<?> redirectToOriginalUrl(@PathVariable String shortLink , HttpServletResponse response) throws IOException
	{
		if(StringUtils.isEmpty(shortLink))
		{
			UrlErrorResponseDto urlErrorResponseDto = new UrlErrorResponseDto();
			urlErrorResponseDto.setError("Invalid Request ");
			urlErrorResponseDto.setStatus(400);
			return new ResponseEntity<UrlErrorResponseDto>(urlErrorResponseDto,HttpStatus.BAD_REQUEST);
		}
		Url urlToRet = urlService.getEncodedUrl(shortLink);
		
		if(urlToRet == null)
		{
			UrlErrorResponseDto urlErrorResponseDto = new UrlErrorResponseDto();
			urlErrorResponseDto.setError("URL does not exist or it might have expired");
			urlErrorResponseDto.setStatus(404);
			return new ResponseEntity<UrlErrorResponseDto>(urlErrorResponseDto,HttpStatus.NOT_FOUND);
		}
		
		if(urlToRet.getExpirationDate().isBefore(LocalDateTime.now()))
		{
			urlService.deleteShortLink(urlToRet);
			UrlErrorResponseDto urlErrorResponseDto = new UrlErrorResponseDto();
			urlErrorResponseDto.setError("Url Expired or Gone, Please try generating a fresh one.");
			urlErrorResponseDto.setStatus(410);
			return new ResponseEntity<UrlErrorResponseDto>(urlErrorResponseDto,HttpStatus.GONE);
		}
		response.sendRedirect(urlToRet.getOriginalUrl());
		
		return null;
		
	}
}
