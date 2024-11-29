Feature: Validate join and Drop functionality

  @reg
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

  @reg
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

  @reg
  Scenario Outline: Validate Audio and Video Stats  <PrimaryMode> - <SecondaryMode>
    Given I launch application
    When I join the video chat with the following details:
      | Name           | Channel   | Mode            |
      | Primary User   | channel01 | <PrimaryMode>   |
      | Secondary User | channel01 | <SecondaryMode> |
    Then I "mute" "Video"
    And I validate Video stats as below
      | Name           | Bitrate | FPS  | Height |
      | Primary User   | < 30    | > 15 | = 480  |
      | Secondary User | < 30    | > 15 | = 480  |
    Then I "unmute" "Video"
    And I close the application
    Examples:
      | PrimaryMode | SecondaryMode |
      | SFU         | SFU           |
      | SFU         | MCU           |
      | MCU         | SFU           |
      | MCU         | MCU           |
