package integrationtests.controller.withjson;


import br.com.paulo.Startup;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import configs.TestConfigs;
import integrationtests.vo.AccountCredentialsVO;
import integrationtests.vo.BookVO;
import integrationtests.vo.PersonVO;
import integrationtests.vo.TokenVO;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = Startup.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookControllerJsonTest {

    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;

    private static BookVO book;

    @BeforeAll
    public static void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        book = new BookVO();
    }

    @Test
    @Order(0)
    public void authorization() throws JsonMappingException, JsonProcessingException {
        AccountCredentialsVO user = new AccountCredentialsVO("leandro", "admin123");

        var accessToken = given()
                .basePath("/auth/signin")
                .port(TestConfigs.SERVER_PORT)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(user)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(TokenVO.class)
                .getAccessToken();

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + accessToken)
                .setBasePath("/api/book/v1")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();
    }

    @Test
    @Order(1)
    public void testCreate() throws JsonMappingException, JsonProcessingException, IOException {
        mockBook();

        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(book)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        BookVO persistedBook = objectMapper.readValue(content, BookVO.class);
        book = persistedBook;

        assertNotNull(persistedBook);

        assertNotNull(persistedBook.getId());
        assertNotNull(persistedBook.getAuthor());
        assertNotNull(persistedBook.getPrice());
        assertNotNull(persistedBook.getTitle());

        assertTrue(persistedBook.getId() > 0);

        assertEquals("Robert C. Martin", persistedBook.getAuthor());
        assertEquals(77.00, persistedBook.getPrice());
        assertEquals("Clean Code", persistedBook.getTitle());
    }
    @Test
    @Order(2)
    public void testFindById() throws IOException {
        mockBook();

        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_PAULO)
                .pathParam("id", book.getId())
                .when()
                .get("{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        BookVO persistedBook = objectMapper.readValue(content, BookVO.class);
        book = persistedBook;

        assertNotNull(persistedBook);

        assertNotNull(persistedBook.getId());
        assertNotNull(persistedBook.getAuthor());
        assertNotNull(persistedBook.getPrice());
        assertNotNull(persistedBook.getTitle());

        assertTrue(persistedBook.getId() > 0);

        assertEquals("Robert C. Martin", persistedBook.getAuthor());
        assertEquals(77.00, persistedBook.getPrice());
        assertEquals("Clean Code", persistedBook.getTitle());
    }

    @Test
    @Order(3)
    public void testUpdate() throws IOException {
        book.setTitle("Código Limpo");

        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(book)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        BookVO persistedBook = objectMapper.readValue(content, BookVO.class);
        book = persistedBook;

        assertNotNull(persistedBook);

        assertNotNull(persistedBook.getId());
        assertNotNull(persistedBook.getAuthor());
        assertNotNull(persistedBook.getPrice());
        assertNotNull(persistedBook.getTitle());

        assertEquals(book.getId(), persistedBook.getId());

        assertEquals("Robert C. Martin", persistedBook.getAuthor());
        assertEquals(77.00, persistedBook.getPrice());
        assertEquals("Código Limpo", persistedBook.getTitle());
    }

    @Test
    @Order(4)
    public void testDelete() throws JsonMappingException, JsonProcessingException {
        mockBook();

        var content = given(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_PAULO)
                .pathParam("id", book.getId())
                .when()
                .delete("{id}")
                .then()
                .statusCode(204)
                .extract()
                .body()
                .asString();


        assertNotNull(content);
    }

    @Test
    @Order(5)
    public void testFindAll() throws JsonMappingException, JsonProcessingException  {

        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_PAULO)
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        List<BookVO> books = objectMapper.readValue(content, new TypeReference<List<BookVO>>() {});

        BookVO foundFourthBook = books.get(2);

        assertNotNull(foundFourthBook.getId());
        assertNotNull(foundFourthBook.getAuthor());
        assertNotNull(foundFourthBook.getPrice());
        assertNotNull(foundFourthBook.getTitle());

        assertEquals(3, foundFourthBook.getId());

        assertEquals("Robert C. Martin", foundFourthBook.getAuthor());
        assertEquals(77.00, foundFourthBook.getPrice());
        assertEquals("Clean Code", foundFourthBook.getTitle());

        BookVO foundSeventhBook = books.get(6);

        assertNotNull(foundSeventhBook.getId());
        assertNotNull(foundSeventhBook.getAuthor());
        assertNotNull(foundSeventhBook.getPrice());
        assertNotNull(foundSeventhBook.getTitle());

        assertEquals(7, foundSeventhBook.getId());

        assertEquals("Eric Freeman, Elisabeth Freeman, Kathy Sierra, Bert Bates", foundSeventhBook.getAuthor());
        assertEquals(110.00, foundSeventhBook.getPrice());
        assertEquals("Head First Design Patterns", foundSeventhBook.getTitle());

        BookVO foundTwelfthBook = books.get(10);

        assertNotNull(foundTwelfthBook.getId());
        assertNotNull(foundTwelfthBook.getAuthor());
        assertNotNull(foundTwelfthBook.getPrice());
        assertNotNull(foundTwelfthBook.getTitle());

        assertEquals(11, foundTwelfthBook.getId());

        assertEquals("Roger S. Pressman", foundTwelfthBook.getAuthor());
        assertEquals(56.00, foundTwelfthBook.getPrice());
        assertEquals("Engenharia de Software: uma abordagem profissional", foundTwelfthBook.getTitle());

    }

    @Test
    @Order(6)
    public void testFindAllWithoutToken() throws JsonMappingException, JsonProcessingException  {

        RequestSpecification specificationWithoutToken = new RequestSpecBuilder()
                .setBasePath("/api/book/v1")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content = given().spec(specificationWithoutToken)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_PAULO)
                .when()
                .get()
                .then()
                .statusCode(403);

    }


    private void mockBook() {
        book.setAuthor("Robert C. Martin");
        book.setLaunchDate(new Date());
        book.setPrice(77.00);
        book.setTitle("Clean Code");
    }

}
