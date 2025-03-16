package pl.sg.servicode;

import org.junit.jupiter.api.Test;
import org.springdoc.core.configuration.SpringDocConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@ImportAutoConfiguration(exclude = SpringDocConfiguration.class)
class ServiCodeApplicationTests {

	@Test
	void contextLoads() {
	}

}
