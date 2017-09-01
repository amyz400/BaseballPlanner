# BaseballPlanner

Given a list of players either extracted from a database or supplied with the createGame request, a random roster is generated for the players using the following rules:

1. Players can not sit on the bench 2 times or more in a row
2. If there are outfield positions, the player's last inning's position wasn't in the outfield, and the player has never played in the next available outfield position, then pic the next outfield position.
3. If there are premium positions available, this player hasn't exceeded his times in a premium position, the player's last innning's position wasn't in a premium position, and the player hasn't played the next premium poistion, then pick the next premium position.
4. If there are infield positions, the player's last inning's position wasn't in the infield, and the player has never played in the next available infield position, then pick the next infield position.
5. If the player didn't meet any of the other rules, then put him in on the bench.

After all the players have been evaluated with the above rules, the software will clean up any discrepancies such as not all positions in an inning have a player and too many players have been benched.

The resulting roster is saved to the database.

Future enhancements:
1. Use data from the previous game to decide when and how often a player can be in a premium position.
2. Create a user interface
3. Create docker containers for the database, this application, and the user interface

## Installing
This project assumes a mysql database has been created called baseball_season using the initDb.sql file in src/main/resources, and the mysql is running.

Configure application.properties to point to the baseball_database with a valid username/password.  The application assumes that the database is running on mysql's default port of 3360 with a username/password of root/root.

Run mvn clean install to build the application and deploy the jar on to your web server of choice. It is also configured to run on an embedded jetty web server using maven.  mvn 
jetty:run will make it available at your_ip_address:9090.

## Running
The rest endpoints that are available are:

<ul>
<li>http://your_ip_address:9090/check - will return SUCCESS if the application is running</li>
<li>http://your_ip_address:9090/getPlayers - will return all of the players available in the database</li>
<li>http://your_ip_address:9090/createGame?datePlayed=xxxxx - will create a game for the data provided using the players in the database; datePlayed defaults to the current date</li>
<li>http://your_ip_address:9090/createGameWithPlayers?datePlayed=xxxxx&playerIds=1,2,3 - will create a game using the database ids of the players provided</li>
</ul>

## Running Tests

### GameServiceTest
This class of unit tests extensively tests the logic used to create a game.  If code changes are made and all the tests continue to run successfully, be confident that you didn't screw anything up!
