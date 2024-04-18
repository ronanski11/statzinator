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

import com.ronanski11.statzinator.dto.TeamDto;
import com.ronanski11.statzinator.model.MessageResponse;
import com.ronanski11.statzinator.model.Team;
import com.ronanski11.statzinator.security.Roles;
import com.ronanski11.statzinator.service.TeamService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.security.RolesAllowed;

@RestController
@RequestMapping("/team")
@SecurityRequirement(name = "bearerAuth")
@Validated
public class TeamController {
	
	@Autowired
	TeamService teamService;
	
	@GetMapping()
	public ResponseEntity<List<Team>> getAllTeam() {
		return ResponseEntity.ok(teamService.getAllTeams());
	}
	
	@PostMapping()
	public ResponseEntity<Team> saveNewTeam(@RequestBody TeamDto teamDto) {
		return ResponseEntity.ok(teamService.saveNewTeam(teamDto));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Team> getTeamById(@PathVariable int id) {
		return ResponseEntity.ok(teamService.getTeamById(id));
	}
	
	@PutMapping("/{id}")
	@RolesAllowed(Roles.Admin)
	public ResponseEntity<Team> updateTeam(@RequestBody TeamDto teamDto, @PathVariable int id) {
		return ResponseEntity.ok(teamService.updateTeam(teamDto, id));
	}

	@DeleteMapping("/{id}")
	@RolesAllowed(Roles.Admin)
	public ResponseEntity<MessageResponse> deleteTeam(@PathVariable Integer id) {
		try {
			return ResponseEntity.ok(teamService.deleteTeam(id));
		} catch (Throwable t) {
			return ResponseEntity.internalServerError().build();
		}
	}

}
