package com.unsch.carnet_digital.util;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordTestRunner implements CommandLineRunner {

    private final PasswordEncoder encoder;

    public PasswordTestRunner(PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) {
        System.out.println("HASH = " + encoder.encode("123456"));
    }
}
