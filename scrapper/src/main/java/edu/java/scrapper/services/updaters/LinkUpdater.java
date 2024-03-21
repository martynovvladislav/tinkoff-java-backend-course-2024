package edu.java.scrapper.services.updaters;

import edu.java.scrapper.domain.jdbc.dtos.LinkDto;
import java.net.URI;
import java.net.URISyntaxException;

public interface LinkUpdater {
    String update(LinkDto linkDto) throws URISyntaxException;

    boolean supports(URI url);
}
