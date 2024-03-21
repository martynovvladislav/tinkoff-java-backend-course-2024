package edu.java.bot.dtos;

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
    Long id;

    @NotNull
    URI url;

    @NotNull
    String description;

    @NotNull
    List<Integer> tgChatIds;
}
