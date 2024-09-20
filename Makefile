JAR=/usr/local/jdk1.8.0_131/bin/jar
JAVA=/usr/local/jdk1.8.0_131/bin/java
JAVAC=/usr/local/jdk1.8.0_131/bin/javac
CP=.:/usr/local/rabbitmq-jar/amqp-client-4.0.2.jar:/usr/local/rabbitmq-jar/slf4j-api-1.7.21.jar:/usr/local/rabbitmq-jar/slf4j-simple-1.7.22.jar:target

JFLAGS = -g -cp $(CP)
.SUFFIXES: .java .class
.java.class:
	$(JAVAC) $(JFLAGS) -d target $*.java
	
CLASSES = \
	src/Booth.java\
	src/MessageFactory.java\
	src/Server.java\
	src/Logger.java\
	src/Service.java

classes: $(CLASSES)
	$(JAVAC) $(JFLAGS) -d target $(CLASSES)

clean:
	rm -f target/*.class
