package edu.java.scrapper.domain.jdbc.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ChatLinkDto {
    Long chatId;
    Long linkId;
}
