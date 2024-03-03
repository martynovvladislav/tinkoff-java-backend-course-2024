package edu.java.scrapper.dtos;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListLinkResponse {
    List<LinkResponse> links;
    Integer size;
}
