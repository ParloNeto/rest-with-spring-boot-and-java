package integrationtests.repositories;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

import br.com.paulo.Startup;
import br.com.paulo.model.Person;
import br.com.paulo.repositories.PersonRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import configs.TestConfigs;
import integrationtests.vo.PersonVO;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.io.IOException;

@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest(classes = Startup.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PersonRepositoryTest {

    @Autowired
    PersonRepository repository;

    private static Person person;
    @BeforeAll
    public static void setup() {
        person = new Person();
    }

    @Test
    @Order(1)
    public void testFindByName() throws JsonMappingException, JsonProcessingException {

        Pageable pageable = PageRequest.of(0, 6, Sort.by(Sort.Direction.ASC, "firstName"));
        person = repository.findPersonsByName("rap", pageable).getContent().get(0);

        assertNotNull(person.getId());
        assertNotNull(person.getFirstName());
        assertNotNull(person.getLastName());
        assertNotNull(person.getAddress());
        assertNotNull(person.getGender());

        assertEquals(1, person.getId());

        assertEquals("Raphael", person.getFirstName());
        assertEquals("Veiga", person.getLastName());
        assertEquals("São Paulo", person.getAddress());
        assertEquals("Male", person.getGender());

    }

//    @Test
//    @Order(2)
//    public void testDisablePersonById() throws IOException {
//
//        repository.disablePerson(person.getId());
//
//        Pageable pageable = PageRequest.of(0, 6, Sort.by(Sort.Direction.ASC, "firstName"));
//        person = repository.findPersonsByName("rap", pageable).getContent().get(0);
//
//        assertNotNull(person.getId());
//        assertNotNull(person.getFirstName());
//        assertNotNull(person.getLastName());
//        assertNotNull(person.getAddress());
//        assertNotNull(person.getGender());
//
//        assertFalse(person.getEnabled());
//
//        assertEquals(1, person.getId());
//
//        assertEquals("Raphael", person.getFirstName());
//        assertEquals("Veiga", person.getLastName());
//        assertEquals("São Paulo", person.getAddress());
//        assertEquals("Male", person.getGender());
//    }

}
