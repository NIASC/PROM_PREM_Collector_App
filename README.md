web/META-INF/context.xml.sample# PROM_PREM_Collector
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
+ JSON Simple

# Setup

start mariadb with `sudo systemctl start mariadb.service`
start tomcat with `sudo /usr/share/tomcat8/bin/startup.sh`

stop tomcat with `sudo /usr/share/tomcat8/bin/shutdown.sh`
stop mariadb with `sudo systemctl stop mariadb.service`

# Deploy servlet
Currently the application is designed to be accessed via the url `http://localhost:8080/PROM_PREM_Collector/`. This is hard-coded in some places in the code and you may need to change this to make it work. In the future this will be fixed.
+ Rename `build.properties.sample` to `build.properties` and edit the file to match your MySQL and tomcat installation.<br>
+ Rename `web/META-INF/context.xml.sample` to `web/META-INF/context.xml` and edit the file to match your MySQL configuration. You should only need to modify `username`, `password` and `url`.
+ run `ant compile` to compile the project
+ run `ant jar` to create the jar file that will be launched on the website. Optionally remove the `servlet` directory from the jar file you created as it is not needed by the users.
+ run `ant install` to deploy the application (i.e. put it with your tomcat webapps).
