package edu.java.scrapper.services.updaters;

import edu.java.scrapper.domain.dtos.Link;
import java.net.URI;
import java.net.URISyntaxException;

public interface LinkUpdater {
    boolean update(Link link) throws URISyntaxException;

    boolean supports(URI url);
}
