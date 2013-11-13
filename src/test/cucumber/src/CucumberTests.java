package cucumber.src;

import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@Cucumber.Options(features = "tests/cucumber/docs",
                  format = {"progress", "html:reports/cucumber"})
public class CucumberTests {
}
