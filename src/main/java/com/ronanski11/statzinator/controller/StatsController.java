package com.ronanski11.statzinator.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ronanski11.statzinator.dto.StatsDto;
import com.ronanski11.statzinator.model.MessageResponse;
import com.ronanski11.statzinator.model.Stats;
import com.ronanski11.statzinator.security.Roles;
import com.ronanski11.statzinator.service.StatsService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.security.RolesAllowed;

@RestController
@RequestMapping("/stats")
@SecurityRequirement(name = "bearerAuth")
@Validated
public class StatsController {
	
	@Autowired
	StatsService statsService;
	
	@GetMapping()
	public ResponseEntity<List<Stats>> getAllStats() {
		return ResponseEntity.ok(statsService.getAllStats());
	}
	
	@PostMapping()
	public ResponseEntity<Stats> saveNewStats(@RequestBody StatsDto statsDto) {
		return ResponseEntity.ok(statsService.saveNewStats(statsDto));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Stats> getStatsById(@PathVariable int id) {
		return ResponseEntity.ok(statsService.getStatsById(id));
	}
	
	@PutMapping("/{id}")
	@RolesAllowed(Roles.Admin)
	public ResponseEntity<Stats> updateStats(@RequestBody StatsDto statsDto, @PathVariable int id) {
		return ResponseEntity.ok(statsService.updateStats(statsDto, id));
	}

	@DeleteMapping("/{id}")
	@RolesAllowed(Roles.Admin)
	public ResponseEntity<MessageResponse> deleteStats(@PathVariable Integer id) {
		try {
			return ResponseEntity.ok(statsService.deleteStats(id));
		} catch (Throwable t) {
			return ResponseEntity.internalServerError().build();
		}
	}

}
