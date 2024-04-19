package com.ronanski11.statzinator;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import com.ronanski11.statzinator.dto.PlayerDto;
import com.ronanski11.statzinator.model.MessageResponse;
import com.ronanski11.statzinator.model.Player;
import com.ronanski11.statzinator.model.Team;
import com.ronanski11.statzinator.repository.PlayerRepository;
import com.ronanski11.statzinator.repository.TeamRepository;
import com.ronanski11.statzinator.service.PlayerService;

import java.time.LocalDate;
import java.util.Optional;

@SpringBootTest()
public class PlayerServiceTests {

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private TeamRepository teamRepository;

    private PlayerService playerService;

    private PlayerDto playerDto;
    private Team team;
    private Player player;
    private final Integer playerId = 1;

    @BeforeEach
    void setUp() {

        team = new Team();
        team.setId(1); 

        playerDto = new PlayerDto();
        playerDto.setFullName("John Doe");
        playerDto.setAge(25);
        playerDto.setHeight(180.0);
        playerDto.setWeight(75.0);
        playerDto.setDateOfBirth(LocalDate.of(1998, 1, 1));
        playerDto.setTeamId(1);

        player = new Player();
        player.setId(playerId);
        player.setFullName(playerDto.getFullName());
        player.setAge(playerDto.getAge());
        player.setHeight(playerDto.getHeight());
        player.setWeight(playerDto.getWeight());
        player.setDateOfBirth(playerDto.getDateOfBirth());
        player.setTeam(team);

        when(teamRepository.findById(playerDto.getTeamId())).thenReturn(Optional.of(team));

        playerService = new PlayerService(playerRepository, teamRepository);
    }

    @Test
    void testSaveNewPlayer() {
        when(playerRepository.save(any(Player.class))).thenReturn(player);

        Player savedPlayer = playerService.saveNewPlayer(playerDto);

        verify(playerRepository).save(any(Player.class));
        assertNotNull(savedPlayer);
        assertEquals(playerDto.getFullName(), savedPlayer.getFullName());
        assertEquals(team, savedPlayer.getTeam()); 
    }

    @Test
    void testUpdatePlayerFound() {
        when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));
        when(playerRepository.save(any(Player.class))).thenReturn(player);

        Player updatedPlayer = playerService.updatePlayer(player, playerId);

        verify(playerRepository).findById(playerId);
        verify(playerRepository).save(player);
        assertNotNull(updatedPlayer);
        assertEquals(playerId, updatedPlayer.getId());
    }

    @Test
    void testUpdatePlayerNotFound() {
        when(playerRepository.findById(playerId)).thenReturn(Optional.empty());
        when(playerRepository.save(any(Player.class))).thenReturn(player);

        Player updatedPlayer = playerService.updatePlayer(player, playerId);

        verify(playerRepository).findById(playerId);
        verify(playerRepository).save(player);
        assertNotNull(updatedPlayer);
    }

    @Test
    void testDeletePlayer() {
        doNothing().when(playerRepository).deleteById(playerId);
        MessageResponse response = playerService.deletePlayer(playerId);

        verify(playerRepository).deleteById(playerId);
        assertEquals("Player " + playerId + " deleted", response.getMessage());
    }
}