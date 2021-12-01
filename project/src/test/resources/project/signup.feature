Feature: As a user I can sign up

    Scenario: can sign up with valid username and valid password 
        Given signup is selected
        When given valid username "NewUsername" and valid password "password123"
        Then signup is successful

    Scenario: signup fails with valid username and too short password
        Given signup is selected
        When given valid username "NewUsername" and invalid password "passwo8"
        Then signup fails

    Scenario: signup fails with too short username and valid password
        Given signup is selected
        When given invalid username "te" and valid password "password123"
        Then signup fails

    Scenario: signup fails with already taken username
        Given signup is selected
        When given invalid username "tester" and valid password "password123"
        Then signup fails