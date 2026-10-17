package jp.gr.java_conf.stardiopside.jnotes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class NotesBatchApplication {

    public static void main(String[] args) {
        int exitCode = SpringApplication.exit(SpringApplication.run(NotesBatchApplication.class, args));
        if (exitCode != 0) {
            System.exit(exitCode);
        }
    }
}
