package edu.java.scrapper;

import edu.java.scrapper.clients.bot.BotClientImpl;
import edu.java.scrapper.domain.dtos.LinkDto;
import edu.java.scrapper.dtos.LinkUpdateDto;
import edu.java.scrapper.services.LinkService;
import edu.java.scrapper.services.updaters.LinkUpdater;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@EnableScheduling
@Component
@RequiredArgsConstructor
public class LinkUpdateScheduler {
    private final LinkService linkService;
    private final List<? extends LinkUpdater> linkUpdaters;
    private final BotClientImpl botClient;

    @Value("#{@scheduler.secondsThreshold()}")
    private long secondsThreshold;

    @Scheduled(fixedDelayString = "#{@scheduler.interval()}")
    public void update() throws URISyntaxException {
        List<LinkDto> linksToUpdate = linkService.findOld(secondsThreshold);
        log.info(linksToUpdate.toString());
        for (LinkDto linkDto : linksToUpdate) {
            URI url = new URI(linkDto.getUrl());
            LinkUpdater linkUpdater = linkUpdaters.stream()
                .filter(linkUpdater1 -> linkUpdater1.supports(url))
                .findFirst()
                .get();
            if (linkUpdater.update(linkDto)) {
                List<Long> chatIds = linkService.listAllByLinkId(linkDto.getId());
                botClient.sendMessage(
                    new LinkUpdateDto(
                        url,
                        url + " was updated!",
                        chatIds
                    )
                );
            }
        }
    }
}
