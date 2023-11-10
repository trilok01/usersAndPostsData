package com.example.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.asm.TypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

@Service
public class RestService {
	public List<ResponseDTO> getResponse() {
		String URL1 = "https://jsonplaceholder.typicode.com/users";
		String URL2 = "https://jsonplaceholder.typicode.com/posts";
		HttpHeaders headers = new HttpHeaders();
		
		HttpEntity requestEntity = new HttpEntity<>(headers);
		
		ResponseEntity<String> usersData = new RestTemplate().exchange(URL1, HttpMethod.GET, requestEntity, String.class);
		ResponseEntity<String> postsData = new RestTemplate().exchange(URL2, HttpMethod.GET, requestEntity, String.class);
		
		JSONArray userDataArray = new JSONArray(usersData.getBody());
		JSONArray postsDataArray = new JSONArray(postsData.getBody());
		
		Map<Integer, Users> usersMap = new HashMap<Integer, Users>();
		Map<Integer, Posts> postsMap = new HashMap<Integer, Posts>();
		
		for (int i = 0; i < userDataArray.length(); i++) {
            JSONObject userObj = userDataArray.getJSONObject(i);
            JSONObject addressObj = userObj.getJSONObject("address");
            JSONObject companyObj = userObj.getJSONObject("company");
            JSONObject geoObj = addressObj.getJSONObject("geo");
            
            Geo geo = new Geo();
            geo.setLat(geoObj.getString("lat"));
            geo.setLng(geoObj.getString("lng"));
            
            Address address = new Address();
            address.setGeo(geo);
            
            Company company = new Company();
            company.setName(companyObj.getString("name"));
            
            Users users = new Users();
            users.setAddress(address);
            users.setCompany(company);
            users.setId(userObj.getInt("id"));
            
            usersMap.put(users.getId(), users);
        }
		
		for (int i = 0; i < postsDataArray.length(); i++) {
			JSONObject postsObj = postsDataArray.getJSONObject(i);
			
			Posts posts = new Posts();
			posts.setId(postsObj.getInt("id"));
			posts.setTitle(postsObj.getString("title"));
			posts.setBody(postsObj.getString("body"));
			
			postsMap.put(posts.getId(), posts);
		}
		
		List<ResponseDTO> responseDTO = new ArrayList<ResponseDTO>();
		
		for(Map.Entry<Integer, Users> user : usersMap.entrySet()) {
			Integer id = user.getKey();
			
			Posts post = postsMap.get(id);
			if(post != null) {
				ResponseDTO response = new ResponseDTO();
				
				response.setId(id);
				response.setCompanyName(user.getValue().getCompany().getName());
				response.setTitle(post.getTitle());
				response.setBody(post.getBody());
				response.setLatitude(user.getValue().getAddress().getGeo().getLat());
				response.setLongitude(user.getValue().getAddress().getGeo().getLng());
				
				responseDTO.add(response);
			}
		}
		
		return responseDTO;
	}
}
