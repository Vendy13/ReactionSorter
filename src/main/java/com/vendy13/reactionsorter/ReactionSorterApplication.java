package com.vendy13.reactionsorter;

import com.vendy13.reactionsorter.services.FXApplicationService;
import com.vendy13.reactionsorter.utils.FxSpringContextBridge;
import javafx.application.Application;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class ReactionSorterApplication {
    public static void main(String[] args) {
	    ConfigurableApplicationContext context = SpringApplication.run(ReactionSorterApplication.class, args);
	    FxSpringContextBridge.setContext(context);
	    Application.launch(FXApplicationService.class, args);
    }
}
