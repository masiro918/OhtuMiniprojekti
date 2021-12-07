Feature: As a user I can add a book as a reading recommendation

    Scenario: logged user can add a valid book
        Given user is logged in
        And post is selected
        And bookpost is selected
        When given valid book with headline "book headline", writer "tester" and ISBN "something"
        Then new book recommendation is added