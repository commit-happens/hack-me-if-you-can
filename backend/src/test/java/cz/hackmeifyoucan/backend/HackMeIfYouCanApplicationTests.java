package cz.hackmeifyoucan.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class ApplicationBootstrapTest {

    @Test
    void given_application_when_context_loads_then_should_start_successfully() {
    }

}
