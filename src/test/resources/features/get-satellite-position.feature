@allTests
Feature: This feature is to identify location of ISS using api services

  Scenario Outline: User calls the API with valid details to get satellite location "<description>"
    Given User is having a satellite NORMAD id as "<satelliteId>"
    When the user will call the "<endpoint>" with "<timestamp>", "<units>" and "<indents>"
    Then the status code is 200
    And receive a valid response

    @valid @test
    Examples:
      | description                                               | satelliteId | endpoint                 | timestamp                                 | units      | indents |
      | Unit miles                                                | 25544       | /satellites/id/positions | 1636029892568,1636029892568               | miles      | 4       |
      | Unit kilometers                                           | 25544       | /satellites/id/positions | 1636029892568,1636029892568,1736029892568 | kilometers | 4       |
      | without unit                                              | 25544       | /satellites/id/positions | 1636029892568,1636029892568,1736029892568 |            | 4       |
      | without unit and indent 100                               | 25544       | /satellites/id/positions | 1636029892568,1636029892568,1736029892568 |            | 100     |
      | without unit and indent                                   | 25544       | /satellites/id/positions | 1636029892568,1636029892568,1736029892568 |            |         |
      | unit with different value other than Miles and kilometers | 25544       | /satellites/id/positions | 1636029892568                             | yards      |         |

  Scenario Outline: User calls the API with invalid details to get satellite location "<description>"
    Given User is having a satellite NORMAD id as "<satelliteId>"
    When the user will call the "<endpoint>" with "<timestamp>", "<units>" and "<indents>"
    Then should return status code as "<errorCode>"
    And user should receive an error message as like "<errorMessage>"

    @exception @test
    Examples:
      | description                                | satelliteId  | endpoint                 | timestamp                      | units | indents | errorCode | errorMessage                                |
      | Unknown satellite no                       | 25644        | /satellites/id/positions | 1636029892568,1636029892568    | miles | 4       | 404       | satellite not found                         |
      | Invalid timestamp                          | 25544        | /satellites/id/positions | saturday29122020,1636029892568 | miles | 4       | 400       | invalid timestamp in list: saturday29122020 |
      | Alphanumeric Satellite id                  | 25A44        | /satellites/id/positions | 1636029892568,1636029892568    | miles | 4       | 404       | satellite not found                         |
      | With special char                          | 25%^%^&%&^44 | /satellites/id/positions | 1636029892568,1636029892568    | miles | 4       | 404       | satellite not found                         |
      | different set of special char              | 2@!£@$%^&*   | /satellites/id/positions | 1636029892568,1636029892568    | miles | 4       | 404       | satellite not found                         |
      #scenario 400 - better 400 but getting 404
      | without timestamp                          | 25544        | /satellites/id/positions |                                | miles | 4       | 400       | invalid timestamp in list:                  |
      #error message contain extra space
      | Alphanumeric satellite id and no timestamp | 25544        | /satellites/id/positions |                                | miles | 4       | 400       | invalid timestamp in list:                  |
      #Must throw error - getting 200
      #| wrong url                                  | 25544        | /satellites/id/          | 1636029892568,1636029892568                   |       | 4       | 404       | satellite not found        |
      #should return valid error code with error message
      #| Future timestamp                           | 25544        | /satellites/id/positions | 1936029892568671636213,1936029892568671636219 |       | 4       | 500       | satellite not found        |