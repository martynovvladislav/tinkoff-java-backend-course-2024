package edu.java.scrapper.dtos;

import java.net.URI;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LinkUpdateDto {
    @NotNull
    URI url;

    @NotNull
    String description;

    @NotNull
    List<Long> tgChatIds;
}
