package com.zzq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.metrics.buffering.BufferingApplicationStartup;

/**
 * 启动程序
 */

@SpringBootApplication
public class FreshMallShopApplication {

    public static void main(String[] args) {
        System.setProperty("spring.devtools.restart.enabled" , "false");
        SpringApplication application = new SpringApplication(FreshMallShopApplication.class);
        application.setApplicationStartup(new BufferingApplicationStartup(2048));
        application.run(args);
        System.out.println("(♥◠‿◠)ﾉﾞ  ============   ლ(´ڡ`ლ)ﾞ");
        System.out.println("(♥◠‿◠)ﾉﾞ  ============   ლ(´ڡ`ლ)ﾞ");
        System.out.println("(♥◠‿◠)ﾉﾞ  fresh-mall启动成功   ლ(´ڡ`ლ)ﾞ");
        System.out.println("(♥◠‿◠)ﾉﾞ  ============   ლ(´ڡ`ლ)ﾞ");
        System.out.println("(♥◠‿◠)ﾉﾞ  ============   ლ(´ڡ`ლ)ﾞ");
    }

}
