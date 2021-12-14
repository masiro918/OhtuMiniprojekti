Feature: As a user I can add a book as a reading recommendation

    Scenario: logged user can add a valid book
        Given user is logged in
        And post is selected
        And bookpost is selected
        When given valid book with headline "book headline", writer "tester" and ISBN "something"
        Then new book recommendation is added
        And added recommendation headline "book headline" is on the list

    Scenario: logged user cannot add book missing headline
        Given user is logged in
        And post is selected
        And bookpost is selected
        When given invalid book, missing the headline
        Then adding reading recommendation fails

    Scenario: logged user cannot add book missing writer
        Given user is logged in
        And post is selected
        And bookpost is selected
        When given invalid book, missing the writer
        Then adding reading recommendation fails