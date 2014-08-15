Once you launch the Tomcat server, navigate to localhost:8080/assignment5-webapplication to begin. However,  
you first need to do the following:   
In order to use the user authentication, you need to configure your server.xml and tomcat-users.xml files located in your eclipse workspace/Servers/Tomcat v7.0 Server folder.  
In server.xml, you need to replace the provided LockOutRealm with this code after enclosing  
each of the following lines with triangle brackets:  
      Realm className="org.apache.catalina.realm.LockOutRealm"  
  	  Realm className="org.apache.catalina.realm.JDBCRealm" connectionURL="jdbc:mysql://localhost/disasterevacuationdb?user=root&amp;password=" debug="99" driverName="com.mysql.jdbc.Driver" roleNameCol="rolename" userCredCol="password\_hash" userNameCol="username" userRoleTable="user\_roles" userTable="users"/  
      /Realm  
In tomcat-users.xml, you need to add the following 2 lines after  
enclosing them each in triangle brackets:  
  role rolename="normal"/  
  user password="tomcat" roles="normal" username="tomcat"/  
Then, you need to uncomment (if necessary) and run the mysql  
script a5\_database\_creation.sql found in our app's folder.

