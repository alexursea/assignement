package com.assignment.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDto {

  private long id;
  private String name;
  private String age;
  private String emailAddress;
  private String password;
}
