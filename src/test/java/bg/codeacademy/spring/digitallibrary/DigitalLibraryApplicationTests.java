package bg.codeacademy.spring.digitallibrary;


import io.restassured.RestAssured;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

// This line will actually run the spring application on a random web port
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// Extend AbstractTestNGSpringContextTests to use estNG tests in Spring Boot

public class DigitalLibraryApplicationTests extends AbstractTestNGSpringContextTests
{
  // get the random port, used by the spring application
  @LocalServerPort
  int port;

  // BeforeClass is invoked by TestNG. But, we have to run
  // that after @LocalServerPort is invoked, so
  // it is important to use dependsOnMethods
  @BeforeClass(alwaysRun = true, dependsOnMethods = "springTestContextPrepareTestInstance")
  protected void setupRestAssured()
  {
    // initialize the rest assured port
    RestAssured.port = port;
//		BasicAuthScheme b = new BasicAuthScheme();
//		b.setUserName("admin");
//		b.setPassword("123456");
//		RestAssured.authentication = b;
  }

  @Test
  void contextLoads()
  {
  }

}
