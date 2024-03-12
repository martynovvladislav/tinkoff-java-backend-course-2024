package edu.java.scrapper.dtos;

import java.net.URI;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LinkResponseDto {
    Long id;
    URI url;
}
