Name: Griffin Nye 
Date: 9/24/20
Documentation Site: https://kuvapcsitrd01.kutztown.edu/~gnye929/SkipBo/index.html [DEPRECATED]

Rules added or removed: Implemented scoring feature as mandatory as outlined in 
                                      the Scoring and Winning section of the rules
Platform: Google Web Toolkit
Language: Java

I have provided the UML class diagrams utilized in designing my project, 
and screenshots of major focal points of the user interface. These documents 
can be found under "Design Documents & Page Views".

***The Model Design***

Card:
        The Card class handles all operations pertaining to each individual card. 
        It consists of a single attribute, it’s rank represented by an enumerated 
        type, consisting of “ONE” through “TWELVE” along with a “WILD” element. 
        The Card class contains a constructor with the parameter of a single 
        enumerated element. It's other methods handle setting and getting the Rank,
        getting the next enumerated Rank value and converting to String.


Hand:
        The Hand class handles all operations pertaining to a Player’s hand. 
        It contains a single attribute as well, an array of size 5 consisting 
        of Card objects, which stores the contents of the Hand. I decided to use 
        an array to store the contents of the hand because its size can be 
        allocated at runtime and cannot be modified, unlike ArrayLists or vectors, 
        making it perfect for storing the Hand, which can only contain up to 5 cards. 
        The Hand class contains a constructor, which will simply declare and 
        allocate the size of the array attribute mentioned above. it’s other 
        operations include clearHand(), which clears the hand of all cards, addCard(), 
        which adds a card object to the hand, removeCard(), which removes the specified 
        Card Object  from the hand, getSize(), which returns the number of Cards 
        contained in the hand, getCard(), which returns a Card object with the provided 
        Rank parameter, Search(), which returns a boolean value based on whether 
        the Hand contains an instance of a Card with the provided Rank parameter, 
        Sort(), which sorts the cards in the Hand by order of Rank, and toString() 
        which stores the contents of the Hand in string format and returns it for 
        displaying to the user.


Deck:
        The Deck class handles all operations on the deck and subsequently 
        acts as the drawpile upon dealing the cards to set up the game. It 
        contains a single attribute, an arrayList of type Card, which contains 
        all the Cards that compose the deck. I chose to implement this attribute 
        as an arrayList, due to its ability to dynamically shrink to its contents, 
        as the deck is rather large and a large portion of it is removed upon 
        dealing the cards, which would leave many unused elements in the case of
        a normal array. Its operations include getTop(), which returns the top 
        Card from the deck, draw() which draws the number of cards provided by 
        the argument and returns an ArrayList of said size containing the cards 
        drawn from the deck, shuffle(), which shuffles the cards,  getCount(), 
        which returns the number of cards in the deck, and toString(), which 
        will convert the contents of the deck to string format for displaying 
        the deck to the user. ToString() is not needed logically within the 
        semantics of the game, but I have decided to keep it anyway for 
        debugging purposes, as one of the togglable cheats. 


DiscardPile:
        The DiscardPile class handles all operations needed to maintain a single 
        discard pile. Its contents are stored within its discard attribute, an 
        ArrayList of Card objects. I chose to use an ArrayList for the DiscardPile 
        object for a few reasons. First, as each discard pile can hold anywhere 
        from zero to half of the deck’s cards (in the worst case), an ArrayList 
        implementation saves a somewhat significant amount of memory in comparison 
        to a standard array implementation, due to its modifiable and ever-changing 
        size. Second, the ArrayList object pushes all subsequent elements forward 
        upon removal of an element, which in this case, the only removable element 
        is the top Card. Third, unlike the StockPile or BuildPile, all cards below 
        the top card are visible, therefore by utilizing an ArrayList, I am able 
        to simply iterate from the first element to the size of the ArrayList in 
        order to display all the contents, again, due to its modifiable size. Some
        of its operations include: addCard(), which adds the provided Card object 
        to the DiscardPile, getTop(), which returns the Card object located on 
        top of the pile, removeTop(), which removes the top Card from the pile, 
        and toString(), which converts the contents of the discard pile to a 
        string format for displaying its contents to the user. 


StockPile:
        The StockPile class handles all operations needed to maintain a single 
        stockpile. Its contents are stored within a Deque of Cards. I chose to 
        implement a Deque for two reasons. First, because the stockpile only ever
        has one card showing at all times, the top Card, making a Stack the best 
        data structure to use for its implementation, but because the standard 
        Java Stack implementation utilizes vectors, it defeats the purpose of 
        utilizing a stack in the first place. My solution is to use a Deque, as 
        it’s implementation more consistently follows LIFO behaviour. The 
        constructor for StockPile creates a stockpile of the provided size with 
        the cards contained within the provided ArrayList of Cards. Its other 
        operations include getTop(), which returns the top card, removeTop(), 
        which removes the top card, and getCount(), which returns the number of 
        cards in the stockpile.


BuildPile:
        The BuildPile class handles all operations needed to maintain a single 
        build pile. Much like the StockPile object, the BuildPile object stores 
        its contents within a Deque of Card objects, as it also would be best 
        implemented as a stack. The main difference between the BuildPile and 
        Stockpile objects though, is that while each StockPile object belongs to 
        each Player, the BuildPiles are shared between players. Because they are 
        shared between players, BuildPile is contained within the Game object, 
        rather than within the Player object, much like the StockPile and 
        DiscardPile objects. The BuildPile object contains operations for 
        clearing the build pile (when a build pile reaches 12 or during round reset), 
        retrieving the top Card from the pile, and playing a card to the top of the pile.


Player: 
        The Player object handles all operations performed by a single human player. 
        Its attributes include the player’s hand, the player’s 4 discard piles 
        contained in an ArrayList, the player’s stockpile, the player’s provided name, 
        and the player’s earned points. Its minor operations include: its constructor,
        which accepts the player’s name; getName(), an inspector for the name attribute; 
        setPoints(), for setting the player’s points at the beginning of the game; 
        getPoints(), an inspector for the points attribute; addPoints, which adds 
        the provided number of points to the points attribute, and toString(), which 
        will provide a string containing the formatted output for the current Player 
        objects interface. The remaining operations will be discussed in conjunction 
        with the Game object.


Game:
        The Game object first handles all interaction with human users through a 
        text-based interface. Beginning in main, the displayMainMenu() function 
        is called, providing the user with options to play, view the rules, or 
        close the game. Upon selecting the play option, the user will be presented 
        with a few settings, such as: number of human players, number of computer 
        players, number of cards in the stockpile, and the point goal for the game. 
        Upon submitting this request, the selected settings are passed to the 
        constructor of the Game object and an instance of the game is created. 
        From here, we will employ the Input package to read the contents 
        of the log.txt file and store the values within the various corresponding 
        attributes. From here, the Game object begins to setup the game by 
        passing cards dealt from the deck through function calls contained 
        within the Player and possibly Computer objects. Once setup is complete and 
        the table is displayed, the current player is set and the Game object 
        displays a menu prompting the user from which pile he/she wants to play 
        from: Discard pile, Build Pile, Hand, or simply discard and end his/her 
        turn. Upon receiving the selection, the user is prompted for either the 
        rank he/she wishes to play (playFromHand case), the discard pile he/she 
        wishes to play from (playFromDiscard case), the card rank and discard pile 
        that he/she wants to discard to (Discard case) or he/she is not prompted 
        any further (playFromBuild case). The user’s responses are recorded and
        validated, then passed to the corresponding function(s) within the 
        Player class: playFromHand(), playFromDiscard(), and playFromBuild() and Discard(). 
        The idea behind this is that the Game object is modifying as little 
        game objects as possible, leaving the majority of the game object 
        manipulation to be done through the Player and Computer objects. 
        Each of the three operations mentioned previously also provide the 
        error checking for the user input, making sure that a move is valid 
        before performing it (this comes in extra handy later). In between 
        each player’s move, the Game object will display the table, reflecting 
        any changes made by the player’s move. Once a player decides to discard 
        and end his/her turn, the Game object switches the current player to the
        next player in the playerList, increasing currentTurn, if applicable. 


Computer:
        The Computer object handles all operations made by the computer player. 
        Unlike the human Player object, the Computer object does not require any 
        input from the user to make his/her decisions. Seeing as though this is 
        the only difference between a Computer player and a human Player, the 
        Computer player is implemented as a subclass of Player, implementing 
        only one additional function, decideMove(). This operation will act 
        similarly to the Game object for the player, but rather than prompting 
        for user input, it will determine what move should be made and call its 
        own methods for playFromHand(), playFromStockPile, playFromDiscardPile(), 
        and discard(). The full algorithm for the Computer logic has not been 
        fully developed yet, at this stage of the project, but the order of 
        precedence for which the Computer will try to find possible moves for 
        this iteration of the project is as follows: playing from the stockpile, 
        playing from the hand, playing from one of the discard piles. 
        If no possible moves exist after exhausting these three options, the 
        computer player will opt to discard a card and end their turn. The 
        computer will first try discarding a card whose rank is one below that 
        of one of the cards on top of the discard piles. If this is not possible, 
        the Computer will select the card from its hand that is the furthest from 
        the average of the cards atop the build piles (empty build piles obviously 
        receiving a value of 0, Wilds being replaced with the value they took on 
        on the build pile) It’s also worth noting, the Computer will not discard 
        a wild card, unless no other option is available). Unfortunately, I was
        unable to implement the Computer player this Phase due to time constraints.
        The Computer class will be implemented and tested in the subsequent phase.
                
***Model Design Patterns***
        
        Template Pattern- I employed the Template Design pattern through my use 
        of the abstract class pile, and its extended classes: Deck, BuildPile, 
        StockPile, and DiscardPile.
        
        Facade Pattern- The Facade Pattern was deployed through my design of 
        the Game, Player, and Computer objects, with the Game object acting as 
        the interface, being responsible for as little game objects as possible, 
        passing responsibility of manipulating game objects to the Player and/or 
        Computer objects.


        Chain of Responsibility Pattern- This pattern was loosely implemented 
        most noticeably by the toString functions within the Player, Hand, 
        DiscardPile, StockPile, and Card functions, as Player’s toString() can 
        not handle the request to convert the discard piles, stockpiles, and 
        hand to string, so it must pass the request down to each of these. Each 
        of these can then not handle the request to convert themselves to a 
        string without help from an outside class, so they pass the request down 
        to the Card object, which in turn, converts and passes each individual 
        result back to the DiscardPile, StockPile, and Hand objects, which 
        subsequently pass their result up to Player, which passes its result 
        back up to the Game object.

***Overall Design Notes***

Summary Statement:
        In designing the user interface of my client-server Web Application, I 
        have fully understood and applied the design principles of simplicity, 
        affordance, feedback, and the Composite design pattern. Additionally, I 
        have learned how to suitably synchronize communication between clients and 
        the server with minimal data transmission, while also maintaining modularity 
        and separation of concerns. 
       
        For this project, I created a client-server Web Application using the Google
        Web Toolkit framework that properly implemented a board or card game of 
        my choice. Accomplishing this task was quite challenging, as it required 
        me to design and implement the backend code for the game’s objects and 
        its mechanics, a graphical user interface with which the user can specify 
        his/her preferred settings and play the game, a basic Artificial Intelligence 
        algorithm that can make gameplay decisions based on the contents of the 
        play area, and a method of transmitting game and turn data between the 
        server and its clients utilizing RPC and Java Servlets.
        
UI Design Principles:
        
        In designing the user interface, I strived to create something simple, 
        yet aesthetic, with easy to use and straightforward controls, so as to 
        allow users of all ages to enjoy my application. The interface was to 
        include: a settings page allowing the host client to specify the game’s 
        settings, a waiting page informing all non-host clients that the host
        client is specifying the game’s settings, a page for displaying and 
        interacting with the game layout, a method of tracking game progress, 
        some means of providing game instructions to the user, and a terminal for 
        entering cheat commands to allow for smoother testing.  The following 
        design principles were taken into account when creating
        this user interface:
        
        1. The user interface should not be cluttered and maintain simplicity.
        2. The user interface should utilize the principle of affordance, so as 
        to promote ease of use.
        3. The user interface should provide proper feedback to the user when a 
        control is used.
        4. Interface elements should be modularized as much as possible so as to
        improve code organization and prevent unwarranted changes to the positioning 
        of these elements from outside sources.
        5. The Composite design pattern should be utilized to simplify the creation 
        of a complex game layout by creating a hierarchical structure of interface 
        objects.
        
Backend Design Principles Overview:

        In designing the backend code for the game objects and its mechanics, I 
        wanted to be able to reuse as much code as possible, so as to minimize 
        resources on the server side. Additionally, I aimed to make the application 
        as efficient and lightweight as possible by applying the following design 
        principles:
        
        1. Inheritance and Polymorphism should be utilized for Model elements 
        improving code readability and reuse.
        2. Client and Server elements should be properly synchronized.
        3. Game data should be stored on the server side leaving the client as 
        lightweight as possible.
        4. Game and turn data should be retrieved from the server on a need-to-know 
        basis, so as to keep data transmission to a minimum.
        5. The Model-View-Presenter design pattern should be utilized to separate 
        game and interface data.
        
Backend Design Principles (con.):

        In implementing this application, I demonstrated a in-depth knowledge of 
        Web Design and Development through my polymorphic implementation of the 
        Model classes, the utilization of  the Composite Pattern in creating, 
        positioning, styling, and updating the interface elements; and separating 
        the application data from the interface data using the Model-View-Presenter
        Design Pattern.
        
        Additionally, I demonstrated proper synchronization techniques by ensuring 
        all connected clients update in real-time by requesting data from the server. 
        I ensured data transmission was minimal, by only requesting the necessary 
        data for each turn or occurring event. For example, for a player playing 
        from their hand, I only retrieved the current player’s hand count, the 
        index of that player, and the build pile played onto, rather than retrieving 
        all data about the game layout at once. By doing this, I was able to create 
        the most efficient and lightweight client-side code possible for such a 
        complex interface
        
       