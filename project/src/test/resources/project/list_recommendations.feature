Feature: As a user I can see all the recommendations

    Scenario: user can see newly added recommendation
        Given user with username "vieras" and password "password1234" is created
        And username "vieras" with password "password1234" is logged in
        And post is selected
        And blogpost is selected
        When given valid blog headline "Fullstack", writer "Mluukai" and url "helsinki.fi"
        Then recommendation with headline "Fullstack" is on the list

