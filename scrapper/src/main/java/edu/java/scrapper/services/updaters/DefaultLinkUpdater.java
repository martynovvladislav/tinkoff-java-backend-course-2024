package edu.java.scrapper.services.updaters;

import edu.java.scrapper.domain.dtos.LinkDto;
import java.net.URI;
import java.net.URISyntaxException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DefaultLinkUpdater implements LinkUpdater {
    @Override
    public boolean update(LinkDto linkDto) throws URISyntaxException {
        return false;
    }

    //TODO default updater
    @Override
    public boolean supports(URI url) {
        return !url.getHost().equals("github.com") && !url.getHost().equals("stackoverflow.com");
    }
}
