package com.tl.toc.runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
		plugin = {"pretty", "json:target/reports/cucumber.json", "html:./target/reports/cucumber-html-report.html"},
		features = {"src/test/resources/features"},
		glue = {"com.tl.toc"},
		tags = "@allTests",
		publish = false //make it true if you need to publish report in internet. More information at https://reports.cucumber.io/docs/cucumber-jvm
)
public class CucumberTests {}
