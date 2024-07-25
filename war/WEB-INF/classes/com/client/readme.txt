Griffin Nye
Project 3 Readme
Phase 2 Link: http://gnye929.kutztown.edu:8080/SkipBo/
Phase 3 Link: 
Javadoc Link Phase 3: https://kuvapcsitrd01.kutztown.edu/~gnye929/doc2/index.html

Connection Service:

NOTE: Works in conjunction with GameController

  To begin my design, I created a RemoteServiceServlet implemented by the ConnectionServiceImpl
class in the server package. Upon launching the game controller, the client will
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
clients passing it the number of players and each clients associated player index.

GameService:

NOTE: Works in conjunction with GamePresenter

  Upon creation of the GamePresenter, the getScoreboardData method of the GameService
is polled by all clients. The GameService will return an array containing all scoreboard
data, which will be passed to the GamePresenter's updateScoreboard method. All clients
will then poll the getPlayerHandStock method, passing it the playerIdx for the client. 
The GameService will respond with an Array containing the PlayerData, which contains 
the contents of the hand and the top card of the stock. This data will be parsed and
then passed to the updateHandStock method of GamePresenter to update their own
hand and stock pile in the UI. The getPlayerNames method of the GameService will 
then be polled by all clients, receiving a response of the list of player names. 
This list is passed to the updatePlayers method and all player labels are appropriately
updated and list of the otherPlayer's player indexes will be generated. This list
is passed to the appropriate get<#>PlayerData method, based on the number of players.
From this method, the getOtherPlayer method of GameService is polled, returning a response
containing the other player data, which contains the hand count, top card of stock,
and contents of discard piles. This data will be separated, converted, and parsed
appropriately and passed as methods to the appropriate update<Direction>Player methods
which updates the UI for said players.

  Game events are handled on the client side through the CardDropHandler. The Handler
determines the type of move event that is being performed and polls the corresponding 
move call in the GameService (discard, makeStockMove, makeHandMove, makeDiscardMove)
passing its respective parameters. The GameService then performs the move in the Game 
object. Upon receiving the response from the server, the CardDropHandler then fires 
the corresponding MoveEvent from the event bus, which updates the UI for the event
UI updates are handled in the GamePresenter using get<Object>Data methods, which 
build the RPC call and retrieve the data. Upon success, the data is passed to the
update method for the corresponding Object, which updates the UI. Unfortunately, 
the UI updates are not functional as of yet, as I'm running into a lot of bugs
with the exception handling, as it seems SkipBoExceptions are being thrown where they
shouldn't be, but I know I could figure it out with more time. The general implementation 
is coded for almost all events, and all the required functions for sending and retrieving d
ata between the client and the server. CardDropEvent was a monster to try to implement, 
which I suppose now could've been broken up into some sub-events for the Drops, which 
might have made it a little less overwhelming.

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

  Unfortunately, this was as far as I was able to get with the implementation of my 
game. I definitely struggled in the beginning with figuring out the client-server
communication, but once I figured it out, it was pretty easy to implement, just
rather time consuming having to restructure the majority of my code to bounce data 
back and forth between the client and server, as well as the inner classes for the 
RPC calls for events where multiple RPC calls in a row are required. The design is all mapped 
out in my head, as I've tried my best to include as many details as possible for anything that
was not implemented. I wills say that I did not meet my expectations for the project,
 as I was unable to implement the part that mattered most due to time constraints, 
especially with my delayed start on this phase due to last Phase taking me so long
and an extremely packed finals week. I was unable to implement: fully handling server-side 
events for Dragging and dropping cards and updating the clients screens for these events, 
games with more than two players, implementing cheats for all players, the Stat logging service (although 
I know how to do it now), polishing the UI, adding sounds, and logging the time 
of the game. As I stated before, I really did not meet my vision for how I intended this
phase to turn out and I feel as though the most frustrating part is that I could very easily do
it, but time was ultimately my downfall. Moving into the break, however, I do plan on continuing
this project, and continuing to build on it, as I really enjoyed working on this project,
it was very good experience for going through the SDLC as an individual rather than a team,
and I would really like to see myself complete a nice polished final product for this.