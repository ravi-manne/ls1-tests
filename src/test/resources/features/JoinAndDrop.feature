Feature: Validate join and Drop functionality

  @reg
  Scenario Outline: Validate chat message <PrimaryMode> - <SecondaryMode>
    Given I launch application
    When I join the video chat with the following details:
      | Name           | Channel   | WebOptions | Mode            |
      | Primary User   | channel01 | N/A        | <PrimaryMode>   |
      | Secondary User | channel01 | N/A        | <SecondaryMode> |
    And I validate Chat Messages for "Primary User"
      | Send Message    |  |
      | Receive Message |  |
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
      | Name           | Channel   | WebOptions  | Mode            |
      | Primary User   | channel01 | PublishOnly | <PrimaryMode>   |
      | Secondary User | channel01 | N/A         | <SecondaryMode> |
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
      | Name           | Channel   | WebOptions | Mode            |
      | Primary User   | channel01 | N/A        | <PrimaryMode>   |
      | Secondary User | channel01 | N/A        | <SecondaryMode> |
    Then I "mute" "Video"
    And I validate Video stats as below
      | Bitrate | FPS  | Height | Width |
      | < 400   | < 35 | = 480  | = 640 |

    Then I "unmute" "Video"
    And I validate Video stats as below
      | Bitrate | FPS  | Height | Width |
      | > 200   | < 35 | = 480  | = 640 |

    And I close the application
    Examples:
      | PrimaryMode | SecondaryMode |
      | SFU         | SFU           |
      | SFU         | MCU           |
      | MCU         | SFU           |
      | MCU         | MCU           |


  @reg @tcp
  Scenario Outline: Validate TCP - TCP Connectivity <PrimaryMode> - <SecondaryMode>
    Given I launch application
    When I join the video chat with the following details:
      | Name           | Channel   | WebOptions | Mode            |
      | Primary User   | channel01 | TCP        | <PrimaryMode>   |
      | Secondary User | channel01 | TCP        | <SecondaryMode> |
    Then I "mute" "Video"
    And I validate Video stats as below
      | Bitrate | FPS  | Height | Width |
      | < 400   | < 35 | = 480  | = 640 |

    Then I "unmute" "Video"
    And I validate Video stats as below
      | Bitrate | FPS  | Height | Width |
      | > 200   | < 35 | = 480  | = 640 |
    And I validate Chat Messages for "Primary User"
      | Send Message    |  |
      | Receive Message |  |
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

  @reg @tcp
  Scenario Outline: Validate TCP - UDP Connectivity <PrimaryMode> - <SecondaryMode>
    Given I launch application
    When I join the video chat with the following details:
      | Name           | Channel   | WebOptions | Mode            |
      | Primary User   | channel01 | TCP        | <PrimaryMode>   |
      | Secondary User | channel01 | N/A        | <SecondaryMode> |
    Then I "mute" "Video"
    And I validate Video stats as below
      | Bitrate | FPS  | Height | Width |
      | < 400   | < 35 | = 480  | = 640 |

    Then I "unmute" "Video"
    And I validate Video stats as below
      | Bitrate | FPS  | Height | Width |
      | > 200   | < 35 | = 480  | = 640 |
    And I validate Chat Messages for "Primary User"
      | Send Message    |  |
      | Receive Message |  |
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


  @reg @publishonly
  Scenario Outline: Validate Publish Only Connectivity <PrimaryMode> - <SecondaryMode>
    Given I launch application
    When I join the video chat with the following details:
      | Name           | Channel   | WebOptions  | Mode            |
      | Primary User   | channel01 | PublishOnly | <PrimaryMode>   |
      | Secondary User | channel01 | N/A         | <SecondaryMode> |
    Then I "mute" "Video"
    And I validate Video stats as below
      | Bitrate | FPS  | Height | Width |
      | < 400   | < 35 | = 480  | = 640 |

    Then I "unmute" "Video"
    And I validate Video stats as below
      | Bitrate | FPS  | Height | Width |
      | > 200   | < 35 | = 480  | = 640 |
    And I validate Chat Messages for "Primary User"
      | Send Message    |  |
      | Receive Message |  |
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


  @reg @receivehonly
  Scenario Outline: Validate Receive Only Connectivity <PrimaryMode> - <SecondaryMode>
    Given I launch application
    When I join the video chat with the following details:
      | Name           | Channel   | WebOptions  | Mode            |
      | Primary User   | channel01 | N/A         | <PrimaryMode>   |
      | Secondary User | channel01 | ReceiveOnly | <SecondaryMode> |
    Then I "mute" "Video"
    And I validate Video stats as below
      | Bitrate | FPS  | Height | Width |
      | < 400   | < 35 | = 480  | = 640 |

    Then I "unmute" "Video"
    And I validate Video stats as below
      | Bitrate | FPS  | Height | Width |
      | > 200   | < 35 | = 480  | = 640 |
    And I validate Chat Messages for "Primary User"
      | Send Message    |  |
      | Receive Message |  |
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

  @reg1
  Scenario: Calculate Joining Time
    Given I launch application
    Then Primary User initiated the meeting
    And  Secondary User Joined and dropped the call
    And  Secondary User Joined and dropped the call
    And  Secondary User Joined and dropped the call
    And  Secondary User Joined and dropped the call
    And  Secondary User Joined and dropped the call
    And Print average connection time taken
    Then I close the application