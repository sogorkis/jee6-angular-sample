WCSP
====
 
Build and Deploy
----------------

1. Make sure you have started the JBoss Server.
2. Open a command line and navigate to the root directory of this project.
3. Type this command to build and deploy the archive:

        mvn clean package jboss-as:deploy

4. This will deploy `target/wcsp.war` to the running instance of the server.
 

Access the application 
----------------------

The application will be running at the following URL: <http://localhost:8080/wcsp/>.


Undeploy the Archive
--------------------

1. Make sure you have started the JBoss Server.
2. Open a command line and navigate to the root directory of this project.
3. When you are finished testing, type this command to undeploy the archive:

        mvn jboss-as:undeploy
