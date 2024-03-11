package edu.java.bot.dtos;

import java.net.URI;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddLinkRequestDto {
    URI link;
}
