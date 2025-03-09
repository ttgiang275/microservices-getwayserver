package com.river.loans.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties(prefix = "river")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RiverDto {
    String name;
    Integer age;
    Map<String, String> address;
    String info;
}
