package com.ronanski11.statzinator.dto;

import java.util.List;

import lombok.Data;

@Data
public class TeamDto {
	
    private String name;
    
    private String coach;
    
    private List<Integer> playerIds;
    
}
