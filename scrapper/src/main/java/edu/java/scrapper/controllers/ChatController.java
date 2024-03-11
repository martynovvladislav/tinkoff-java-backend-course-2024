package edu.java.scrapper.controllers;

import edu.java.scrapper.dtos.AddLinkRequestDto;
import edu.java.scrapper.dtos.LinkResponseDto;
import edu.java.scrapper.dtos.ListLinkResponseDto;
import edu.java.scrapper.dtos.RemoveLinkRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class ChatController {

    @GetMapping("/links")
    public ResponseEntity<ListLinkResponseDto> getLinks(
        @RequestHeader("Tg-Chat-Id") Long tgChatId
    ) {
        ListLinkResponseDto listLinkResponseDto = new ListLinkResponseDto();
        log.info("Links has been received");
        return new ResponseEntity<>(
            listLinkResponseDto,
            HttpStatus.OK
        );
    }

    @PostMapping("/links")
    public ResponseEntity<LinkResponseDto> addLink(
        @RequestHeader("Tg-Chat-Id") Long tgChatId,
        @RequestBody AddLinkRequestDto addLinkRequestDto
    ) {
        LinkResponseDto linkResponseDto = new LinkResponseDto(
            tgChatId,
            addLinkRequestDto.getLink()
        );
        log.info("link has been added");
        return new ResponseEntity<>(
            linkResponseDto,
            HttpStatus.OK
        );
    }

    @DeleteMapping("/links")
    public ResponseEntity<LinkResponseDto> deleteLink(
        @RequestHeader("Tg-Chat-Id") Long tgChatId,
        @RequestBody RemoveLinkRequestDto removeLinkRequestDto
    ) {
        LinkResponseDto linkResponseDto = new LinkResponseDto(
            tgChatId,
            removeLinkRequestDto.getLink()
        );
        log.info("link has been deleted");
        return new ResponseEntity<>(
            linkResponseDto,
            HttpStatus.OK
        );
    }
}
