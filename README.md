# AniMatch

AniMatch is a Tinder-inspired matching application, designed to bring you your new favourite anime show! Easily find your next match by swiping through the deck -- swipe right to add a show to your watchlist, swipe left, to remove this from your suggestions. Want more information on the show? Simply tap to read the synopsis! <br/>

This is the landing page of the app. As can be seen, instructions and certain marketing taglines are presented to entice the user to register. In registering, the user is required to input their username, email, password, and display name. Both email and the username should not be taken by another user in the database. In addition, the user must at least be 13 years old to successfully register. <br/>

![1](https://user-images.githubusercontent.com/69026502/130502773-f9fed989-9dbf-43bb-9e63-88494664c265.gif)
</br></br>

After a successful registration, the user is redirected to their main matching activity. The user is presented with various anime show recommendations with different genres and ratings. If the user is a minor, the deck filters out all anime shows rated R. Otherwise, the user is presented with every possible anime recommendation. The deck is only composed of 25 cards at a time. If the user runs out of cards, the deck is replenished again. More information is presented when the user taps on the card. Swiping right adds the show to their watchlist, while swiping left removes this from the current deck. It is worth noting that the action of swiping left does not permanently remove this from the deck. This show would reappear after some time, considering the possiblity that a user would have chhanged their minds by then. </br>


![2](https://user-images.githubusercontent.com/69026502/130503299-ce76358d-28b8-4ded-818b-3c376bd40c74.gif)
</br></br>

The matches button redirects the user to their current watchlist (shows they have swiped right on). Tapping on a show redirects them to more information about the show, as well as options to remove this show, or move them to their finished shows. If a user finishes a show, the app gives the user the option to rate the show, or not. The finished shows button redirects the user to their list of finished shows. The layout is similar to the matchlist layout, except that the block also indicates what the user wrote as their rating. <br/>


![rate (1)](https://user-images.githubusercontent.com/69026502/131833725-dbcce446-0f55-4302-b80c-59912d31b125.gif)
</br></br>

The account button redirects the user to their account information. This screen also gives the user the option to edit their account or to log out. The app only allows the user to edit their display name, username, email, and password. Taken emails and usernames will be restricted. Birthdays are not allowed to be edited in order to restrict the filtering feature for anime show ratings. The user is also given the option to delete their account if they wish to do so. <br/>


![edit](https://user-images.githubusercontent.com/69026502/131832778-46e12534-2f5c-41c3-8be4-5ed74db906d3.gif)
</br></br>

It is worth noting that destroying the app does not destroy the user session. This is only done when the user is consciously logging out of the application. <br/>

![5](https://user-images.githubusercontent.com/69026502/130504526-20c28c8e-285a-4fc2-8912-d07785344596.gif) </br></br>


The following clip shows extra features regarding error handling when it comes to registering. As can be seen, no user can register with an email or a username that is associated with an existing account. The register activity also finishes as soon as registration is successful, wherein clicking back does not return you to the register screen.

![7](https://user-images.githubusercontent.com/69026502/130557469-842849cf-a402-4b47-8f90-359ca00755d3.gif)
![8](https://user-images.githubusercontent.com/69026502/130557498-637c3278-218a-4db3-8708-62b84eb6fd43.gif)


</br></br>

# Links
- [Database](https://console.firebase.google.com/u/2/project/animatch-8e557/firestore)

# Dependencies
- Firebase
- [Glide](https://github.com/bumptech/glide) 
- [SwipeDeck](https://github.com/aaronbond/Swipe-Deck)

# Authors
- Ocampo, Andrea Nicole
- Pioquinto, Cherrie Luz
