@allTests
Feature: This feature is to gather TLE data of ISS satellite using api services

  Scenario Outline: User calls the API with valid details to get satellite TLE data "<description>"
    Given User is having a satellite NORMAD id as "<satelliteId>"
    When the user will call the TLE "<endpoint>" with "<format>"
    Then should receive the status code is 200
    And should receive a valid data for the satellite "<satelliteName>"

    @valid
    Examples:
      | description                                      | satelliteId | endpoint            | format   | satelliteName |
      | Response in text format for a valid satellite id | 25544       | /satellites/id/tles | text     | ISS (ZARYA)    |
      | Response in Json format for a valid satellite id | 25544       | /satellites/id/tles | json     | ISS (ZARYA)    |
      | without format                                   | 25544       | /satellites/id/tles |          | ISS (ZARYA)    |
      | special charater in format field                 | 25544       | /satellites/id/tles | *%@!{_=+ | ISS (ZARYA)    |

  Scenario Outline: User calls the API with invalid details to get satellite TLE data "<description>"
    Given User is having a satellite NORMAD id as "<satelliteId>"
    When the user will call the TLE "<endpoint>" with "<format>"
    Then should return status code as "<errorCode>"
    And user should receive an error message as like "<errorMessage>"

    @exception
    Examples:
      | description              | satelliteId | endpoint            | errorCode | errorMessage                                  |
      | invalid ulr              | 25544       | /satellites/id/tle  | 404       | Invalid controller specified (satellites_tle) |
      | invalid id               | 25547       | /satellites/id/tles | 404       | satellite not found                           |
      | alphanumeric and invalid | 255SSAS47   | /satellites/id/tles | 404       | satellite not found                           |