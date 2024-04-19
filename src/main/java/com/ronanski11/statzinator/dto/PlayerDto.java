package com.ronanski11.statzinator.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class PlayerDto {
	
    private String fullName;    
    
    private int age;    
   
    private double height;
    
    private double weight;    

    private LocalDate dateOfBirth;

    private Integer teamId;

}
