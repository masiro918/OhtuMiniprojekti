Feature: As a user I can see all the recommendations

    Scenario: can see the recommendations page
        Given user is logged in
        Given list is selected
        Then recommendations page is shown

    Scenario: can see all the blogs
        Given user is logged in
        Given list is selected
        Then all recommendations are shown