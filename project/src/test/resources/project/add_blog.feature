Feature: As a user I can add blog as a reading recommendation

    Scenario: logged user can add a valid blog
        Given user is logged in
        And post is selected
        And blogpost is selected
        When given valid blog headline "Test", writer "tester" and url "something"
        Then new blog recommendation is added
        And added recommendation headline "Test" is on the list

    Scenario: cannot see post page if not logged in
        Given post is selected
        Then user is returned to the mainpage

    Scenario: logged user cannnot add blog missing headline
        Given user is logged in
        And post is selected
        And blogpost is selected
        When given invalid blog, missing the headline
        Then adding reading recommendation fails