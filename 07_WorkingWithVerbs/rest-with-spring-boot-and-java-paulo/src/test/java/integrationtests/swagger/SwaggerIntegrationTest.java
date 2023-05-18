package integrationtests.swagger;

import static io.restassured.RestAssured.given;

import br.com.paulo.Startup;
import configs.TestConfigs;
import integrationtests.testcontainers.AbstractIntegrationTest;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = Startup.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SwaggerIntegrationTest {

	@Test
	public void shouldDisplaySwaggerUiPage() {
		var content = given()
						.basePath("/swagger-ui/index.html")
						.port(TestConfigs.SERVER_PORT)
						.when()
							.get()
						.then()
							.statusCode(200)
						.extract()
							.body()
								.asString();
		assertTrue(content.contains("Swagger UI"));

	}

}
