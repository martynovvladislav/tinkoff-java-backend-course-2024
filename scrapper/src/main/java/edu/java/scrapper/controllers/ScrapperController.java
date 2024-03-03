package edu.java.scrapper.controllers;

import edu.java.scrapper.dtos.AddLinkRequest;
import edu.java.scrapper.dtos.LinkResponse;
import edu.java.scrapper.dtos.ListLinkResponse;
import edu.java.scrapper.dtos.RemoveLinkRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class ScrapperController {
    @PostMapping("/tg-chat/{id}")
    public ResponseEntity<Void> registerChat(
        @PathVariable("id") Long tgChatId
    ) {
        log.info("The chat has been registered");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/tg-chat/{id}")
    public ResponseEntity<Void> deleteChat(
        @PathVariable("id") Long tgChatId
    ) {
        log.info("The chat has been deleted");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/links")
    public ResponseEntity<ListLinkResponse> getLinks(
        @RequestHeader("Tg-Chat-Id") Long tgChatId
    ) {
        ListLinkResponse listLinkResponse = new ListLinkResponse();
        log.info("Links has been received");
        return new ResponseEntity<>(
            listLinkResponse,
            HttpStatus.OK
        );
    }

    @PostMapping("/links")
    public ResponseEntity<LinkResponse> addLink(
        @RequestHeader("Tg-Chat-Id") Long tgChatId,
        @RequestBody AddLinkRequest addLinkRequest
    ) {
        LinkResponse linkResponse = new LinkResponse(
            tgChatId,
            addLinkRequest.getLink()
        );
        log.info("link has been added");
        return new ResponseEntity<>(
            linkResponse,
            HttpStatus.OK
        );
    }

    @DeleteMapping("/links")
    public ResponseEntity<LinkResponse> deleteLink(
        @RequestHeader("Tg-Chat-Id") Long tgChatId,
        @RequestBody RemoveLinkRequest removeLinkRequest
    ) {
        LinkResponse linkResponse = new LinkResponse(
            tgChatId,
            removeLinkRequest.getLink()
        );
        log.info("link has been deleted");
        return new ResponseEntity<>(
            linkResponse,
            HttpStatus.OK
        );
    }
}
