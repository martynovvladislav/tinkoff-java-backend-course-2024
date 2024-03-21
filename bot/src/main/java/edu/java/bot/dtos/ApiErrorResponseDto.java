package edu.java.bot.dtos;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ApiErrorResponseDto {
    String description;
    String code;
    String exceptionName;
    String exceptionMessage;
    List<String> stackTrace;
}
