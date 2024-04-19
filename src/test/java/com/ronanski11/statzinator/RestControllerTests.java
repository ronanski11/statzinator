package com.ronanski11.statzinator;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ronanski11.statzinator.dto.PlayerDto;
import com.ronanski11.statzinator.model.Player;
import com.ronanski11.statzinator.repository.PlayerRepository;
import com.ronanski11.statzinator.repository.TeamRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
@AutoConfigureDataJpa
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RestControllerTests {

	@Autowired
	private MockMvc api;

	@Autowired
	private PlayerRepository playerRepository;

	@Autowired
	private TeamRepository teamRepository;

	@BeforeAll
	void setup() {
		Player player = new Player();
		player.setAge(20);
		player.setDateOfBirth(LocalDate.now().minusYears(20).minusWeeks(1));
		player.setHeight(190.00);
		player.setWeight(100.00);
		player.setFullName("Max Mustermann");
		player.setTeam(teamRepository.findAll().get(0));
		playerRepository.save(player);

	}
	
	@Order(1)
	@Test
	void testGetPlayer() throws Exception {
		String accessToken = obtainAccessToken();

		api.perform(get("/player").header("Authorization", "Bearer " + accessToken).with(csrf())).andDo(print())
				.andExpect(status().isOk()).andExpect(content().string(containsString("Max Musterman")));
	}

	@Test
	@Order(1)
	void testSavePlayer() throws Exception {
	    PlayerDto player = new PlayerDto();

	    player.setAge(20);
	    player.setDateOfBirth(LocalDate.now().minusYears(20).minusWeeks(1));
	    player.setHeight(190.00);
	    player.setWeight(100.00);
	    player.setFullName("Test REST");
	    player.setTeamId(1);

	    String accessToken = obtainAccessToken();

	    ObjectMapper mapper = new ObjectMapper();
	    mapper.registerModule(new JavaTimeModule()); // Register JSR-310 datetime module
	    String body = mapper.writeValueAsString(player);

	    api.perform(post("/player")
	        .contentType(MediaType.APPLICATION_JSON)
	        .content(body)
	        .header("Authorization", "Bearer " + accessToken)
	        .with(csrf()))
	        .andDo(print())
	        .andExpect(status().isOk())
	        .andExpect(content().string(containsString("Test REST")));
	}

	private String obtainAccessToken() {

		RestTemplate rest = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		String body = "client_id=statzinator&" + "grant_type=password&" + "scope=openid profile roles offline_access&"
				+ "username=user&" + "password=admin";

		HttpEntity<String> entity = new HttpEntity<>(body, headers);

		ResponseEntity<String> resp = rest.postForEntity(
				"http://localhost:8080/realms/statzinator/protocol/openid-connect/token", entity, String.class);

		JacksonJsonParser jsonParser = new JacksonJsonParser();
		return jsonParser.parseMap(resp.getBody()).get("access_token").toString();
	}

}
