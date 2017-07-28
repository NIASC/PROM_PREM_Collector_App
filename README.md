# PROM_PREM_Collector
This is an application designed to collect PROM (patient reported outcome measures) and PREM (patient reported experience measures).

This application is currently being rewritten into a servlet and applet. The wiki page is likely outdated.

# Servlet prerequisits
Some tomcat libraries needs to be put in the ant library directory. These libs are `catalina-ant.jar` and `catalina-ant.jar`. This is a part of the tomcat configuration.
+ Apache Ant
+ Apache Tomcat 8
+ MySQL (or equivalent)
## Required libraries
The required libraries should be put in $CATALINA_HOME/lib.
+ MySQL Connector/J
+ GNU JavaMail
+ GNU JAF
+ GNU inetlib

# Setup

start mariadb with `sudo systemctl start mariadb.service`
start tomcat with `sudo /usr/share/tomcat8/bin/startup.sh`

stop tomcat with `sudo /usr/share/tomcat8/bin/shutdown.sh`
stop mariadb with `sudo systemctl stop mariadb.service`