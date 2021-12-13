Feature: As a user I can add podcast as a reading recommendation

    Scenario: logged user can add a valid podcast
        Given user is logged in
        And post is selected
        And podcastpost is selected
        When given valid podcast with name "Podcast", description "desc" and headline "Pod headline"
        Then new podcast recommendation is added
        And added recommendation headline "Pod headline" is on the list

    Scenario: logged user cannot add podcast missing name
        Given user is logged in
        And post is selected
        And podcastpost is selected
        When given invalid podcast, missing the name
        Then adding reading recommendation fails

    Scenario: logged user cannot add podcast missing description
        Given user is logged in
        And post is selected
        And podcastpost is selected
        When given invalid podcast, missing the description
        Then adding reading recommendation fails

    Scenario: logged user cannot add podcast missing headline
        Given user is logged in
        And post is selected
        And podcastpost is selected
        When given invalid podcast, missing the headline
        Then adding reading recommendation fails