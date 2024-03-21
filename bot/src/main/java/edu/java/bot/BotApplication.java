package edu.java.bot;

import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.linklistenerbot.Bot;
import edu.java.bot.linklistenerbot.LinkListenerBot;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
@EnableConfigurationProperties(ApplicationConfig.class)
@ConfigurationPropertiesScan
public class BotApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(BotApplication.class, args);
        Bot bot = context.getBean(LinkListenerBot.class);
        bot.start();
    }
}
