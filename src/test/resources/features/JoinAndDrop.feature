Feature: Validate join and Drop functionality

  @SmokeTest1
  Scenario Outline: Validate chat message <PrimaryMode> - <SecondaryMode>
    Given I launch application
    When I join the video chat with the following details:
      | Name           | Channel   | Mode            |
      | Primary User   | channel01 | <PrimaryMode>   |
      | Secondary User | channel01 | <SecondaryMode> |
    And I validate Chat Messages for "Primary User"
      | Send Message    |           |
      | Receive Message |           |
    And I close the application
    Examples:
      | PrimaryMode | SecondaryMode |
      | SFU         | SFU           |
      | SFU         | MCU           |
      | MCU         | SFU           |
      | MCU         | MCU           |

  @SmokeTest
  Scenario Outline: Validate screen share <PrimaryMode> - <SecondaryMode>
    Given I launch application
    When I join the video chat with the following details:
      | Name           | Channel   | Mode            |
      | Primary User   | channel01 | <PrimaryMode>   |
      | Secondary User | channel01 | <SecondaryMode> |
    And I validate screen sharing for "Primary User"
      | Start Screen Share |
      | Stop Screen Share  |
    And I close the application
    Examples:
      | PrimaryMode | SecondaryMode |
      | SFU         | SFU           |
      | SFU         | MCU           |
      | MCU         | SFU           |
      | MCU         | MCU           |