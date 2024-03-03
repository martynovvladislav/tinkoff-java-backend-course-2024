package edu.java.bot.dtos;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ListLinkResponse {
    List<LinkResponse> links;
    Integer size;
}
