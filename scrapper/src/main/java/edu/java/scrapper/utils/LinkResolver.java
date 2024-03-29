package edu.java.scrapper.utils;

import edu.java.scrapper.services.LinkService;
import edu.java.scrapper.services.updaters.GithubLinkUpdater;
import edu.java.scrapper.services.updaters.StackOverflowLinkUpdater;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LinkResolver {
    private final GithubLinkUpdater githubLinkUpdater;
    private final StackOverflowLinkUpdater stackOverflowLinkUpdater;
    private final LinkService linkService;

    public static boolean isSuitableLink(URI url) {

        String urlString = url.toString();
        Pattern githubPattern = Pattern.compile("^https://github.com/[0-9-A-Za-z]+/[0-9-A-Za-z]+$");
        Pattern soPattern = Pattern.compile("^https://stackoverflow.com/questions/[0-9]+/[0-9-A-Za-z]+$");

        if (!githubPattern.matcher(urlString).matches() || soPattern.matcher(urlString).matches()) {
            return false;
        }

        try {
            HttpURLConnection huc = (HttpURLConnection) url.toURL().openConnection();
            huc.setInstanceFollowRedirects(false);
            int responseCode = huc.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                return true;
            }
            huc.disconnect();
        } catch (IOException e) {
            return false;
        }
        return false;
    }
}
