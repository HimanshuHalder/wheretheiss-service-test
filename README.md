# Wheretheiss-service tests - Cucumber Rest Assured API tests Framework

## Introduction

The `Cucumber Rest Assured API tests Framework` allows to easily validate any endpoints examples defined in the README file.

##Software Prerequisite
- JDK 8 recommended or above(Version supports lambda, annotation etc.)
- Maven - apache-maven-3.8.1
- IntelliJ idea latest version recommended or any idea supports maven, cucumber-java, gherkins.

After installation of software make sure all system path has been set correctly.
- to check java  - open terminal and check this command returning the version `java --version`
- to check maven  - open terminal and check this command returning the version `mvn -v`
- check all cucumber-java, gherkins plugins are installed(please check compatible version with your editor)

## Installation
Once you have all above prerequisite, clone the project in your preferred location. Then import the project in your editor as maven project and run below command.
`mvn clean install -DskipTests`

## Command line

The validation can be triggered by executing the following command in the target project's main directory:
```
mvn clean test -Dcucumber.filter.tags="@allTests" -Dprofile=e1
```

`-Dprofile` is to set the environment variable which will help to load required config to execute tests in that environment. It is defaulted to `e1` if that is not passed.

`cucumber.filter.tags` this is being used to run specific tests. there are few available tags that will allow you to run specific set of test with required test data.
Available tags are 
``@allTests, @valid and @exception``
-`@allTests` is to execute all tests available
-`@valid` is to execute only valid tests available
-`@exception` is to execute only valid tests available

##Execution using Editor
Run `CucumberTests` file as JUnit. It will trigger test case execution based on tags mentioned in that class. If you want to run against different environment and different tags without changing anything in CucumberTests class, then just pass `-Dcucumber.filter.tags="@allTests" -Dprofile=e1` as jvm argument and modify these value as needed.

## Reporting
Execution reports will be avaiable in `target/reports` folder as `cucumber-html-report.html` which you can open in any browser(chrome recommended).
This report will hold all the record that you need to analyze in case you need to. It will provide in details execution report.



##Example
In this project we have used an open api to demonstrate the framework functionality

API doc for the example service [![Wheretheiss service](https://wheretheiss.at/w/developer)](https://wheretheiss.at/w/developer)

####Few observations about this service

API endpoint observations for `satellites/[id]/positions` and `satellites/[id]/tles`:
- No authentication - at least a basic authentication recomended for open api
- Response content type not mentioned
- Parameter validation missing
- API is having only one satellite id response.
- NORAD Catalog id validation recommended and must be restricted
- Parameters format validation and missing parameters could have been handled better way.
- Unhandled exception which is leading to 5XX error code which is not recommended
-

API doc should hold more information:

- not mentioned which type of timestamp to use(above apis are using epoch timestamp)
- header information missing 
- API json schema missing
- Error code and necessary response payload missing
- Return content-type missing
- API versioning could have been better in a format like `major.minor.defectpatch` ex. `1.0.0`
- `X-Rate-Limit` need more descriptive information about this header(actually we receive three different X-Rate-Limit headers)
