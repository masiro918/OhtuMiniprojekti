Feature: As a user I can add blog as a reading recommendation

    Scenario: logged user can add a valid blog
        Given user is logged in
        And post is selected
        And blogpost is selected
        When given valid blog headline "Test", writer "tester" and url "something"
        Then new blog recommendation is added

    #Scenario: logged user cannnot add blog missing headline
        #Given user is logged in
        #And post is selected
        #And blogpost is selected
        #When given invalid blog, missing the headline