package com.ronanski11.statzinator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.ronanski11.statzinator.model.Team;
import com.ronanski11.statzinator.repository.TeamRepository;


@DataJpaTest()
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
class DBTests {

    @Autowired
    private TeamRepository teamRepository;

    @Test
    void insertVehicle() {
        Team team = new Team();
        team.setCoach("Coach K");
        team.setName("Basel Lions");
        Team savedTeam = teamRepository.save(team);
        Assertions.assertNotNull(savedTeam.getId());
        
    }
}