# SkipBo

A Web App for playing the game of Skip-Bo. Features include: Real-Time Multiplayer, AI Players, & a Cheats menu for testing. Unfortunately, the game is not actively hosted anymore, but can be run and tested in Eclipse. Still needs some polishing, mainly on the front-end and some concurrency on the server side, but is one of my largest projects to date, so while she's still a work in progress, I'm proud of her.

|                           |                                                                                                    |
|---------------------------:|---------------------------------------------------------------------------------------------------|
|**Last Modified Date:**| 9/24/20                                                                                                |
|**Last Modifications:**| Implemented scoring feature as mandatory as outlined in the Scoring and Winning section                |
|     **Documentation:**| [Doc Site]() [DEPRECATED] (See doc folder for doc site HTML files)  
|                       | [Design Docs](https://github.com/GriffinNye22/SkipBo/tree/main/Design%20Documents%20%26%20Page%20Views)|
|          **Platform:**| Google Web Toolkit                                                                                     |
|          **Language:**| Java w/ GWT Framework                                                                                  |

I have provided the UML class diagrams utilized in designing my project, and screenshots of major focal points of the user interface. These documents can be found under "Design Documents & Page Views".

## Design Details

### The Model Design

**Card:**

The Card class handles all operations pertaining to each individual card. It consists of a single attribute, its rank represented by an enumerated type, consisting of “ONE” through “TWELVE” along with a “WILD” element. The Card class contains a constructor with the parameter of a single enumerated element. Its other methods handle setting and getting the Rank, getting the next enumerated Rank value, and converting to String.

**Hand:**

The Hand class handles all operations pertaining to a Player’s hand. It contains a single attribute as well, an array of size 5 consisting of Card objects, which stores the contents of the Hand. I decided to use an array to store the contents of the hand because its size can be allocated at runtime and cannot be modified, unlike ArrayLists or vectors, making it perfect for storing the Hand, which can only contain up to 5 cards. The Hand class contains a constructor, which will simply declare and allocate the size of the array attribute mentioned above. Its other operations include clearHand(), which clears the hand of all cards; addCard(), which adds a card object to the hand; removeCard(), which removes the specified Card Object from the hand; getSize(), which returns the number of Cards contained in the hand; getCard(), which returns a Card object with the provided Rank parameter; search(), which returns a boolean value based on whether the Hand contains an instance of a Card with the provided Rank parameter; sort(), which sorts the cards in the Hand by order of Rank; and toString() which stores the contents of the Hand in string format and returns it for displaying to the user.

**Deck:**

The Deck class handles all operations on the deck and subsequently acts as the draw pile upon dealing the cards to set up the game. It contains a single attribute, an ArrayList of type Card, which contains all the Cards that compose the deck. I chose to implement this attribute as an ArrayList, due to its ability to dynamically shrink to its contents, as the deck is rather large and a large portion of it is removed upon dealing the cards, which would leave many unused elements in the case of a normal array. Its operations include getTop(), which returns the top Card from the deck; draw(), which draws the number of cards provided by the argument and returns an ArrayList of said size containing the cards drawn from the deck; shuffle(), which shuffles the cards; getCount(), which returns the number of cards in the deck; and toString(), which will convert the contents of the deck to string format for displaying the deck to the user. ToString() is not needed logically within the semantics of the game, but I have decided to keep it anyway for debugging purposes, as one of the togglable cheats.

**DiscardPile:**

The DiscardPile class handles all operations needed to maintain a single discard pile. Its contents are stored within its discard attribute, an ArrayList of Card objects. I chose to use an ArrayList for the DiscardPile object for a few reasons. First, as each discard pile can hold anywhere from zero to half of the deck’s cards (in the worst case), an ArrayList implementation saves a somewhat significant amount of memory in comparison to a standard array implementation, due to its modifiable and ever-changing size. Second, the ArrayList object pushes all subsequent elements forward upon removal of an element, which in this case, the only removable element is the top Card. Third, unlike the StockPile or BuildPile, all cards below the top card are visible. Therefore by utilizing an ArrayList, I am able to simply iterate from the first element to the size of the ArrayList in order to display all the contents, again, due to its modifiable size. Some of its operations include: addCard(), which adds the provided Card object to the DiscardPile; getTop(), which returns the Card object located on top of the pile; removeTop(), which removes the top Card from the pile; and toString(), which converts the contents of the discard pile to a string format for displaying its contents to the user.

**StockPile:**

The StockPile class handles all operations needed to maintain a single stockpile. Its contents are stored within a Deque of Cards. I chose to implement a Deque for two reasons. First, because the stockpile only ever has one card showing at all times, the top Card, making a Stack the best data structure to use for its implementation, but because the standard Java Stack implementation utilizes vectors, it defeats the purpose of utilizing a stack in the first place. My solution is to use a Deque, as its implementation more consistently follows LIFO behaviour. The constructor for StockPile creates a stockpile of the provided size with the cards contained within the provided ArrayList of Cards. Its other operations include getTop(), which returns the top card; removeTop(), which removes the top card; and getCount(), which returns the number of cards in the stockpile.

**BuildPile:**

The BuildPile class handles all operations needed to maintain a single build pile. Much like the StockPile object, the BuildPile object stores its contents within a Deque of Card objects, as it also would be best implemented as a stack. The main difference between the BuildPile and Stockpile objects however, is that while each StockPile object belongs to each Player, the BuildPiles are shared between players. Because they are shared between players, BuildPile is contained within the Game object, rather than within the Player object, much like the StockPile and DiscardPile objects. The BuildPile object contains operations for clearing the build pile (when a build pile reaches 12 or during round reset), retrieving the top Card from the pile, and playing a card to the top of the pile.

**Player:**

The Player class handles all operations performed by a single human player. Its attributes include the player’s hand, the player’s 4 discard piles contained in an ArrayList, the player’s stockpile, the player’s provided name, and the player’s earned points. Its minor operations include: its constructor, which accepts the player’s name; getName(), an inspector for the name attribute; setPoints(), for setting the player’s points at the beginning of the game; getPoints(), an inspector for the points attribute; addPoints, which adds the provided number of points to the points attribute; and toString(), which will provide a string containing the formatted output for the current Player object. The remaining operations will be discussed in conjunction with the Game class.

**Game:**  

The Game class handles managing Player actions, executing cheats, and maintaining all shared elements of the board, such as the Deck and BuildPiles. Its attributes include: the number of human players, the number of computer players, the number of starting cards in the Stockpile, the point goal for the game, an array of 4 BuildPile objects, an instance of Deck, an array of Players forming the playerList, the index of the current Player in the playerList, and the current turn number.

Upon startup of the application, the Host client is presented with a main menu prompting the user to configure the settings for the game, such as: the number of human players, the number of computer players, the starting number of cards in the Stockpile, and the point goal for the game. Upon the Host client's submission of this request, the selected settings are passed to the constructor of the Game object and an instance of the Game is created. From here, the Game's play() method is called which sets the current turn number to 1 and calls newRound(). NewRound() then creates the Deck, populates the BuildPile List, and deals the cards to all Player hands and stockpiles through function calls contained within the Player and possibly Computer objects (if Computer player's were enabled for this session).

Once the setup is complete and the table is displayed, the current Player is free to drag and drop cards to make moves. The Game object handles these moves through the functions: discard(), makeDiscardMove(), makeHandMove(), and makeStockMove(); each of which passes the appropriate indexes for the Hand, BuildPile and/or DiscardPile (depending on the desired move) to the Player-contained methods: discard(), playFromDiscard(), playFromHand(), and playFromStock(), respectively. The idea behind this is that the Game object is modifying as few game structures as possible, leaving the majority of the manipulation of said structures to the Player and Computer objects. This allows the Game object to not worry itself with lower-level game logic, such as determining if moves are valid, instead focusing on higher-level game logic, such as: setting up and ending rounds, tallying scores, tracking turns, and executing cheats entered through the cheats menu.

Once a player has decided to discard and end his/her turn, the Game object switches the currentPlayer to the next player in the playerList, increasing currentTurn if applicable, via its endTurn() method. Once a player has depleted all cards in their stockpile, the Game object tallies the score and applies points via its updateScore() method,  checks if a player hit the point cap, via its checkPlayerWon() method, clears the table via its endRound() method, and shuffles and redeals the cards via its newRound() method (if applicable).

Inspector methods include: getBuildPile(), which returns the BuildPile found at the specified index in the BuildPile list; getCurrentPlayer(), which returns the Player from the playerList whose turn it is; getCurrentTurn(), which returns the turn number; getPlayerNames(), which returns an array of player names; getCurrentPlayerIndex(), which returns the index of the current Player in the playerList; getNumCPUS(), which returns the number of Computer players setup for the game; getNumPlayers(), which returns the number of human players setup for the game; getNumStock(), which returns the starting number of cards in the StockPile setup at the beginning of the game; getPointGoal(), which returns the point goal setup for the game;  and getPlayer(), which returns the Player found at the specified index in the playerList. The purpose of most of these methods are for simply propagating information to the clients for updating their UI's accordingly.

Facilitator methods include: checkCompletedBuildPiles(), which clears any completed BuildPiles in the BuildPile List, if any; handleCheats(), which parses, validates, and executes cheat commands entered through the Cheat Menu console; and refillEmptyHand(), which refills the current player's hand to 5 cards when the player has exhausted all the cards in his/her hand.

**Computer:**

The Computer class handles all operations made by the computer player. 
Unlike the human Player class, the Computer class does not require any 
input from the user to make his/her decisions. Seeing as though this is 
the only difference between a Computer player and a human Player, the 
Computer player is implemented as a subclass of Player, implementing 
only one additional function, decideMove(). This operation will act 
similarly to the Game object for the player, but rather than prompting 
for user input, it will determine what move should be made and call its 
own methods for playFromHand(), playFromStockPile(), playFromDiscardPile(), 
and discard().  The order of precedence for which the Computer will try to 
find possible moves is as follows: playing from the stockpile, playing 
from the hand, playing from one of the discard piles. If no possible moves 
exist after exhausting these three options, the computer player will opt 
to discard a card and end their turn. The 
computer will first try discarding a card whose rank is one below that 
of one of the cards on top of the discard piles. If this is not possible, 
the Computer will select the card from its hand that is the furthest from 
the average of the cards atop the build piles (empty build piles obviously 
receiving a value of 0, Wilds being replaced with the value they took on 
on the build pile). It’s also worth noting, the Computer will not discard 
a wild card, unless no other option is available. 
                
### Model Design Patterns
        
  **Template Pattern-** I employed the Template Design pattern through my use 
  of the abstract class Pile, and its extended classes: Deck, BuildPile, 
  StockPile, and DiscardPile.
  
  **Facade Pattern-** The Facade Pattern was deployed through my design of 
  the Game, Player, and Computer objects, with the Game object acting as 
  the interface, being responsible for as little game structures as possible, 
  passing the responsibility of manipulating these structures to the Player and/or 
  Computer objects.
  
  **Chain of Responsibility Pattern-** This pattern was loosely implemented 
  most noticeably by the toString() functions within the Player, Hand, 
  DiscardPile, StockPile, and Card functions, as Player’s toString() method 
  cannot handle the request to convert the DiscardPiles, StockPiles, and 
  Hand to a string, so it must pass the request down to each of these. Each 
  of these also cannot handle the request to convert themselves to a 
  string without help from an outside class, so they pass the request down 
  to the Card object. The Card object, in turn, converts and passes each individual 
  result back to the DiscardPile, StockPile, and Hand objects; which 
  subsequently pass their result up to the Player object, which propagates its result 
  back up to the Game object.

  This pattern was also employed in handling Player Moves. As the game's Model is stored on the server side, the client's CardDropHandler is responsible for identifying the source and destination objects for which the Player is attempting to 
  make a move and passes this information, along with the responsibility of performing the move to the GameService. The GameService is then responsible for transmitting the required move information from the client to the server, which then
  passes this responsibility to the Game object. The Game object then utilizes this information to retrieve any objects necessary for the move (ie in the case of plays to a BuildPile) and passes these objects, the original move information,
  and the responsibility of updating these objects to the Player object associated with the current player. 

### Overall Design Notes

**Summary Statement:**  

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

**UI Design Principles:**

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

**Backend Design Principles Overview:**

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

**Backend Design Principles (con.):**

  In implementing this application, I demonstrated an in-depth knowledge of 
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
  complex interface.

### RPC Design Notes

**Connection Service:**  
*NOTE: Works in conjunction with GameController*

  To begin my design, I created a RemoteServiceServlet implemented by the ConnectionServiceImpl
  class in the server package. Upon launching the GameController, the client will
  poll the connectHostClient method of the ConnectionService. The ConnectionService
  will return a Boolean value, based on whether the requesting client was the first
  client to connect. If a client is the first client to connect, ConnectionService 
  will return True, designating this client as the host client. The GameController 
  will then direct the host client to the settings page to allow them to select the 
  game's settings. All other clients connecting to the ConnectionService at this time 
  will be issued a False result, causing the GameController to redirect them to the 
  WaitingForHost page.

  Once the Host client has selected the desired game settings and clicks the Play
  button, the Host client will then poll the startGame method of the ConnectionService,
  passing it the selected settings. The ConnectionService will create the Instance of
  the Game using the provided settings, set the Player name for the host and return a 
  null result, as a way of indicating the success of the call. After the callback is returned,
  The Host will continuously poll the checkGameFull method of the Connection Service,
  returning a Boolean value indicating all clients have connected. Once all clients have
  connected, the GamePresenter is created passing it the number of players and a thisPlayer 
  value of 0 (the player index), as the host is always the first player in the game.

  While the non-host clients are on the WaitingForHost page, they will continuously
  poll the checkGameStart method of the ConnectionService. The ConnectionService will
  continue to return a 0 flag until the Game has been created, eventually returning
  the number of players in the Game, upon its creation. Upon receiving this callback,
  the client will be prompted for their playerName and will call the connectClient
  method of the ConnectionService passing the client's name upon submission.
  The connection service will accept the client connection, as long as the game
  has not yet reached its capacity. In doing so, the ConnectionService increments
  the number of clients, sets the Client's player name in the Game object, and returns
  the client's associated player index. If the game has already reached capacity,
  the ConnectionService will refuse the connection by returning a value of -1, and a
  message stating the game has reached capacity will be displayed to the user. Upon 
  acceptance to the game, the client will continuously poll the checkGameFull method
  of the ConnectionService, in the same way the Host client does after submitting the
  Game settings. Once the last client has joined, the GamePresenter is created for all
  clients passing it the number of players and each client's associated player index.

**GameService:**  
*NOTE: Works in conjunction with GamePresenter*

  Upon creation of the GamePresenter, the getScoreboardData() method of the GameService
  is polled by all clients. The GameService will return an array containing all scoreboard
  data, which will be passed to the GamePresenter's updateScoreboard() method. All clients
  will then poll the getPlayerHandStock() method, passing it the playerIdx for the client. 
  The GameService will respond with an Array containing the PlayerData, which contains 
  the contents of the hand and the top card of the stock. This data will be parsed and
  then passed to the updateHandStock() method of GamePresenter to update their own
  hand and stock pile in the UI. The getPlayerNames() method of the GameService will 
  then be polled by all clients, receiving a response of the list of player names. 
  This list is passed to the updatePlayers() method and all player labels are appropriately
  updated and a list of the otherPlayer's player indexes will be generated. This list
  is passed to the appropriate get<#>PlayerData() method, based on the number of players.
  From this method, the getOtherPlayer() method of GameService is polled, returning a response
  containing the other player data, which contains the hand count, the top card of the stock,
  and contents of the discard piles. This data will be separated, converted, and parsed
  appropriately and passed as methods to the appropriate update<Direction>Player() methods,
  which updates the UI for said players.

  Game events are handled on the client side through the CardDropHandler. The Handler
  determines the type of move event that is being performed and polls the corresponding 
  move call in the GameService (discard(), makeStockMove(), makeHandMove(), makeDiscardMove() )
  passing its respective parameters. The GameService then performs the move in the Game 
  object. Upon receiving the response from the server, the CardDropHandler then fires 
  the corresponding MoveEvent from the event bus, which updates the UI for the event.
  UI updates are handled in the GamePresenter using get<Object>Data() methods, which 
  build the RPC call and retrieve the data. Upon success, the data is passed to the
  update method for the corresponding Object, which updates the UI. 

  For making moves, my design plan was to store the move made by the currentPlayer
  in the session within the void methods for performing moves in the Game object in GameService
  (discard, makeHandMove etc). Information for the move made would include: the
  type of move (discard, play from discard, play from hand, play from stock, round won, 
  game won, & cheat), the player involved with the move, the source object and its value,
  and the destination object. Clients continuously not taking their turn would then poll the
  server continuously every second, checking whether a move was made. Upon a move being made,
  they would retrieve the information about the move from the server, and update their UIs
  accordingly. Upon receiving the discard move from the server, the clients will poll 
  the server for the new current player. They will compare this value with their thisPlayer
  data member to determine whether they should remove the DragHandlers from their 
  layout section and continuously poll for made Moves, or whether they should re-add their
  DragHandlers and send move data to the server. Won Rounds and won Games are handled similarly,
  as the currentPlayer polls the server to check if they won the game or round, passing
  their playerIndex. If they did, the server stores this information as a made Move
  and players not taking their turn will receive this information when retrieving the move
  made, displaying appropriate messages.

### Known Bugs
  Unfortunately, the UI updates are not fully functional as of yet, as I'm running into a lot of bugs
  with the exception handling. It seems SkipBoExceptions are being thrown where they shouldn't be. 
  This may be due to some concurrency issues on the server side. While this is still a work in progress,
  the general implementation is coded for almost all events, as well as all the required functions for sending 
  and retrieving data between the client and the server. CardDropEvent was a monster to try to implement, 
  which I now suppose could've been broken up into some sub-events for the types of Drops, 
  which might have made it a little more organized and reduced some of the issues experienced.

 

