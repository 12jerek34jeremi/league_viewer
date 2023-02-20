# League Viewer
The League Viewer application was co-created by me as part of a project in Application Programming at the university. In this README.md file, you will find a [link to a video](https://drive.google.com/file/d/1v48XZgSmH-Su3g6PAoOSJAcw1wl3UKvi/view?usp=share_link) showing how the application looks and how it can be used (**video is in polish**), [instructions for compiling and running the application](#compiling-and-running), [a description of my part of the project](#my-part-in-the-project), a brief [description of what the application can do](#application-functions), and finally, a few [screenshots of the application](#few-screenshots). The application allows users to browse information related to football in various leagues and add new information.

Here is a [link](https://drive.google.com/file/d/1v48XZgSmH-Su3g6PAoOSJAcw1wl3UKvi/view?usp=share_link), to a video showing how the application looks and how it can be used. Video is in polish, because app's user interface is also in polish. If you don't speak polish, then you propably won't understand a lot.

### Compiling and running
To compile and run the application, you first need to clone the repository to your computer and navigate to the newly created directory.
~~~
git clone https://github.com/12jerek34jeremi/league_viewer.git
cd league_viewer
~~~
Then, use the `mvn package` command to compile the application. I assume that[Maven](https://maven.apache.org/) and [JDK](https://openjdk.org/) software are installed and added to the path, and therefore available from the terminal:
~~~
 mvn package;
~~~
After compiling the application, use the following command to run the application. (Maven has created a jar file, which now just needs to be run.)
~~~
java -jar target/football-1.0.0-jar-with-dependencies.jar
~~~
![compilation1](img/compilation1.png)<br/>
![compilation2](img/compilation2.png)
<br/><br/>
### My part in the project
In the project, I was responsible for:
* the application layer that allows to connet to a database and to retrieve data from it. (Implementation of classes [DataProvider](src/main/java/league/conectivity/DataProvider.java), [DataGetter](src/main/java/league/conectivity/DataGetter.java), [BaseConnector](src/main/java/league/conectivity/BaseConector.java))
* transferring the database from the university server to AMAZON RDS
* creating panels displaying lists of data (match list, team list, etc.)
  * implementation of classes [LeaguePanel](src/main/java/league/panels/LeaguePanel.java), [LeagueViewingPanel](src/main/java/league/panels/LeagueViewingPanel.java)
  * creating classess [MatchesPanel](), [PlayersPanel](), [TeamsPanel]() (These classes were later further developed by other members of the team.)
* creating a graphical interface that allows entering new data
  * implementation of classes [AddingPanel](src/main/java/league/panels/AddingPanel.java), [InputsPanel](src/main/java/league/panels/InputsPanel.java)
<br/><br/>
### Application Functions
The application allows, among others, for:
* displaying a list of teams playing in a given league
* displaying a list of all players playing in one of the teams of a given league
* displaying matches played in the league
* displaying information about a player - their name, surname, date of birth, height and weight, data about the player's team
* browsing matches in which a given player participated
* displaying information about a given team
* browsing matches in which a given team participated
* displaying a list of players playing in a given team
* displaying information about a given match
* displaying players in the team and information about the team
* displaying a league table (number of matches won and lost)
* creating a new league
* adding a new team to the league
* adding a new player to the team
* adding a new match to the league
<br/><br/>
### Authors
 - Paweł Rogóż
 - Zuzanna Damszel
 - Jędrzej Chmiel
<br/><br/>
### Libraries and languages that were used
In the course of the project, we used Java language and Swing, JDBC libraries.
<br/><br/>
### Few screenshots
![screen](img/screen1.png)
![screen](img/screen2.png)
![screen](img/screen4.png)
![screen](img/screen5.png)

