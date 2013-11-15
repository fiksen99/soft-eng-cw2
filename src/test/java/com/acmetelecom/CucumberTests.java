package com.acmetelecom;

import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@Cucumber.Options(features = "src/test/resources",
                  format = {"progress", "html:reports/cucumber"})
public class CucumberTests {
}
