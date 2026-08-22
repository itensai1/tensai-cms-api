package com.tensai.cms;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;

class ArchitectureTest {
    private final ApplicationModules modules = ApplicationModules.of(TensaiCmsApiApplication.class);

    @Test
    void verifyModuleBoundaries() {
        modules.verify();
    }

    @Test
    void generateDocumentation() {
        new Documenter(modules).writeDocumentation();
    }
}