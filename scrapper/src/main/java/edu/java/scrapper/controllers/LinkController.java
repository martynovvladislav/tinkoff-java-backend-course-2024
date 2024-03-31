package edu.java.scrapper.controllers;

import edu.java.scrapper.domain.dtos.LinkDto;
import edu.java.scrapper.dtos.AddLinkRequestDto;
import edu.java.scrapper.dtos.LinkResponseDto;
import edu.java.scrapper.dtos.ListLinkResponseDto;
import edu.java.scrapper.dtos.RemoveLinkRequestDto;
import edu.java.scrapper.exceptions.BadLinkException;
import edu.java.scrapper.exceptions.TooManyRequestsException;
import edu.java.scrapper.services.LinkService;
import edu.java.scrapper.utils.BucketGrabber;
import edu.java.scrapper.utils.LinkResolver;
import io.github.bucket4j.Bucket;
import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class LinkController {
    private final LinkService linkService;
    private final BucketGrabber bucketGrabber;
    public static final String X_FORWARDED_FOR = "X-Forwarded-For";

    @GetMapping("/links")
    public ResponseEntity<ListLinkResponseDto> getLinks(
        @RequestHeader("Tg-Chat-Id") Long tgChatId,
        HttpServletRequest request
    ) {
        String clientIP = Optional.ofNullable(request.getHeader(X_FORWARDED_FOR))
            .orElseGet(request::getRemoteAddr);
        Bucket bucket = bucketGrabber.grabBucket(String.valueOf(clientIP));
        if (bucket.tryConsume(1)) {
            List<LinkDto> linkDtoList = linkService.listAll(tgChatId);
            ListLinkResponseDto listLinkResponseDto = new ListLinkResponseDto();
            listLinkResponseDto.setLinks(
                linkDtoList.stream()
                    .map(link -> {
                        try {
                            return new LinkResponseDto(link.getId().longValue(), new URI(link.getUrl()));
                        } catch (URISyntaxException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .toList()
            );
            listLinkResponseDto.setSize(listLinkResponseDto.getLinks().size());
            log.info("Links has been received");
            return new ResponseEntity<>(
                listLinkResponseDto,
                HttpStatus.OK
            );
        } else {
            throw new TooManyRequestsException();
        }
    }

    @PostMapping("/links")
    public ResponseEntity<LinkResponseDto> addLink(
        @RequestHeader("Tg-Chat-Id") Long tgChatId,
        HttpServletRequest request,
        @RequestBody AddLinkRequestDto addLinkRequestDto
    ) {
        String clientIP = Optional.ofNullable(request.getHeader(X_FORWARDED_FOR))
            .orElseGet(request::getRemoteAddr);
        Bucket bucket = bucketGrabber.grabBucket(String.valueOf(clientIP));
        if (bucket.tryConsume(1)) {
            //TODO date logic
            if (!LinkResolver.isSuitableLink(addLinkRequestDto.getLink())) {
                throw new BadLinkException();
            }
            linkService.add(tgChatId, addLinkRequestDto.getLink());
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
        throw new TooManyRequestsException();
    }

    @DeleteMapping("/links")
    public ResponseEntity<LinkResponseDto> deleteLink(
        @RequestHeader("Tg-Chat-Id") Long tgChatId,
        HttpServletRequest request,
        @RequestBody RemoveLinkRequestDto removeLinkRequestDto
    ) {
        String clientIP = Optional.ofNullable(request.getHeader(X_FORWARDED_FOR))
            .orElseGet(request::getRemoteAddr);
        Bucket bucket = bucketGrabber.grabBucket(String.valueOf(clientIP));
        if (bucket.tryConsume(1)) {
            linkService.remove(tgChatId, removeLinkRequestDto.getLink());
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
        throw new TooManyRequestsException();
    }
}
