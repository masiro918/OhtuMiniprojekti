Feature: As a user I can login with the right credentials

    
    Scenario: can login with correct password
        Given login is selected
        When given correct credentials username "tester" and password "salasana123"
        Then user login is successful

    Scenario: login fails with incorrect password
        Given login is selected
        When given correct username "tester" and incorrect password "incorrect2"
        Then login fails
    
    Scenario: login fail with non existing username
        Given login is selected
        When given non existing username "Noup"
        Then login fails

    Scenario: can login with newly-created user
        Given user with username "user" and password "password231" is created
        And login is selected
        When given correct credentials username "user" and password "password231"
        Then user login is successful