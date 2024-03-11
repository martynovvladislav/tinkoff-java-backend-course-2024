package edu.java.bot.dtos;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ListLinkResponseDto {
    List<LinkResponseDto> links;
    Integer size;
}
