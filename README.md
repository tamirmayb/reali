# Reali Home Assignment

## A simple application server that prints a message at a given time in the future.

Author: Tamir Mayblat, tamirmayb@gmail.com

## Content

###Prerequisites :

* A working redis server with factory settings (localhost:6379)
* Jar file reali_test-1.0-SNAPSHOT-jar-with-dependencies.jar (can be found in the target directory).

###How to use :

* Start Redis server. 
* In terminal run: java -cp reali_test-1.0-SNAPSHOT-jar-with-dependencies.jar com.tamirm.reali.MessagesAppServer
* The server should be online and ready to receive new messages.
* In your browser or any API testing tool of your choice (postman etc.) use the api http://localhost:8000/echoAtTime with a message as described below.
* In order to print a message immediately (20 seconds delay) just type the message text without the date.
* In order to print a message at a scheduled time type a massage followed by " " (space) and the time for the message to be echoed from redis. (yyyy-mm-dd hh:mm)
* You can stop the server by using the http://localhost:8000/exit 
 
** Example :  http://localhost:8000/echoAtTime?message_text 2020-12-01-01 12:00
