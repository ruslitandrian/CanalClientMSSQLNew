package com.tutorabc.test;

import com.tutorabc.test.service.ITableSchemaChangeService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

import static java.lang.System.exit;


@SpringBootApplication
@MapperScan("com.tutorabc.test.mapper*")
@ImportResource({"classpath:disconf.xml"})
// public class Application {
public class Application implements CommandLineRunner {

    @Autowired
    private ITableSchemaChangeService service;

    public static void main(String[] args) {
        // SpringApplication.run(Application.class, args);
        SpringApplication app = new SpringApplication(Application.class);
        app.setBannerMode(Banner.Mode.LOG);
        app.run(args);
    }

    @Override
    public void run(String... args) throws Exception
    {
        service.Init();
        /*
        if (args.length > 0) {
            System.out.println(service.Main(args[0].toString()));
        } else {
            System.out.println(service.getMessage());
        }
*/
        exit(0);
    }
}
