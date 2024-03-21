package edu.java.scrapper.dtos;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListLinkResponseDto {
    List<LinkResponseDto> links;
    Integer size;
}
