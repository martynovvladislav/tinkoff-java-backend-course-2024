package edu.java.scrapper.services.updaters;

import edu.java.scrapper.domain.dtos.LinkDto;
import java.net.URI;
import java.net.URISyntaxException;

public interface LinkUpdater {
    boolean update(LinkDto linkDto) throws URISyntaxException;

    boolean supports(URI url);
}
