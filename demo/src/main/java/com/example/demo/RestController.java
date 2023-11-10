package com.example.demo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RestController {
	@Autowired
	RestService restService;
	
	@GetMapping("aggregatedResponse")
	private ResponseEntity<List<ResponseDTO>> aggregatedResponse() {
		List<ResponseDTO> response = restService.getResponse();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
