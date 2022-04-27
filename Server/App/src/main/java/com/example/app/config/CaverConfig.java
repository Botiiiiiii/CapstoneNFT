package com.example.app.config;

import org.springframework.context.annotation.Bean;
import xyz.groundx.caver_ext_kas.CaverExtKAS;

public class CaverConfig {

    @Bean
    public CaverExtKAS caverConfig() {
        CaverExtKAS caver = new CaverExtKAS();
        caver.initKASAPI(1001, "KASKUDIAFAM8KVF03GMJFZ60", "T1lWLrLFa5jnvp2f25kTOngsGlCOGDifpw_em3QQ");
        return caver;
    }
}
