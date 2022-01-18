# JAVAMTG
--Protokoll
--Lukas Aichhorn
--BIF-DUA-3-WS2021-SWEN1/115701 

-------------------------------
1. Technical steps

1. Started with implementing the server since it felt the most challenging for me.
2. I wanted to implement one complete Curl work flow to figure out how the different elements Database->Server<-Request work together.
3. Implemented Curl decoding. => Request Builder Object
4. Implemented Request-type(POS, GET .....) switching as well as exceuting Routes in regards to found Rout String (/foo/bar...)
5. Created static query object to host all the sql interaction methods.
6. Created DB and tables via PGADMIN4, and created a db singelton for the ServerThreds to use.
7. Now i basically reiterate the proccess of
	- reading the Route String
	- created a corresponding Method to handle the route
	- check for Authorisation
	- create all DB query actions
	- return message

-------------------------------
2. failures

In hindsight I would choose a more modern implementation of reading the incomming string since it is very confusing at the moment.
basically every route handling methode needs to check for Authentification level. Those code blocks can be refactored into an Authentification Handler of some sort.
I didnt implemeted the last two trading request since my Database structure was already set up on unique card ids, and the change would have created an imense rebuild
of the Table contrains.

-------------------------------
3. Testing
All test focus on easy input output methods and are crutial for the Battle logic.
The most important one is testing the decoding of an incoming request since this information handles all other actions done by the server.


-------------------------------
4. Time Tracking

crating the server ----------------------------->	10h 
designing the basic workflow of Curl decoding --> 	5h
creating Database singelton and test Connection->	1h
creating queries ------------------------------->	16h
implemeningt syncronised Battle-Queue ---------->	12h
Battle logic ----------------------------------->	4h
writing tests ---------------------------------->	4h
overall testing curls -------------------------->	3h
protocoll -------------------------------------->	2h
------------------------------------------------------------
sum -------------------------------------------->	57h

-------------------------------
5. Git

