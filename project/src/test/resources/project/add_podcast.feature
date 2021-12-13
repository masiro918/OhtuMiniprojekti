Feature: As a user I can add podcast as a reading recommendation

    Scenario: logged user can add a valid podcast
        Given user is logged in
        And post is selected
        And podcastpost is selected
        When given valid podcast with name "Podcast", description "desc" and headline "Pod headline"
        Then new podcast recommendation is added

    Scenario: logged user cannot add podcast missing name
        Given user is logged in
        And post is selected
        And podcastpost is selected
        When given invalid podcast, missing the name
        Then adding reading recommendation fails