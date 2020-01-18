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
* Send a curl POST request to use the api http://localhost:8000/echoAtTime with a message as described below.
* In order to print a message immediately (20 seconds delay) just type the message text in the text parameter without the echo parameter.
* In order to print a message at a scheduled time fill the text field and the echo field in 'yyyy-mm-dd hh:mm' format for the message to be echoed from redis.
 
** Example :  curl -d "text=value1&echo=2020-01-18 15:20" -X POST http://localhost:8000/echoAtTime

### Thanks