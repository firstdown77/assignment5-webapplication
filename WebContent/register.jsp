<%@ page language="java" contentType="text/html; charset=US-ASCII"
	pageEncoding="US-ASCII"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" href="css/bootstrap.min.css" type="text/css" />
<link rel="stylesheet" href="css/header.css" type="text/css"/>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII" />
<title>Register</title>
</head>
<body>
	<%@ page import="javax.crypto.Cipher"%>
	<%@ page import="javax.crypto.KeyGenerator"%>
	<%@ page import="javax.crypto.SecretKey"%>
	<%@ page import="testPackage.*"%>
	<%@ page import="dbObjects.*"%>
	<%@ page import="java.sql.Date"%>
	<%@ page import="java.util.Arrays"%>
	<%
		if (request.getParameter("btnSubmit") != null) {
			DatabaseMethods db = new DatabaseMethods();
			// create a key generator based upon the Blowfish cipher
			KeyGenerator keygenerator = KeyGenerator
					.getInstance("Blowfish");
			// create a key
			SecretKey secretkey = keygenerator.generateKey();
			// create a cipher based upon Blowfish
			byte[] keyToStoreInDB = secretkey.getEncoded();
			Cipher cipher = Cipher.getInstance("Blowfish");
			// initialise cipher to with secret key
			cipher.init(Cipher.ENCRYPT_MODE, secretkey);
			// get the text to encrypt
			String inputText = request.getParameter("password");
			// encrypt message
			byte[] encrypted = cipher.doFinal(inputText.getBytes());
			// re-initialise the cipher to be in decrypt mode
			//SecretKey secretkey = new SecretKeySpec(keyToStoreInDB, 0, keyToStoreInDB.length, "Blowfish");
			cipher.init(Cipher.DECRYPT_MODE, secretkey);
			// decrypt message
			byte[] decrypted = cipher.doFinal(encrypted);
			// and display the results
			User u = new User(null, request.getParameter("username"), encrypted,
					keyToStoreInDB, request.getParameter("firstname"),
					request.getParameter("lastname"), new Date(System.currentTimeMillis()));
			boolean result = db.insertUser(u);
			if (result == true) {
				response.sendRedirect("my_account.jsp");
				%><script>alert("Insert succeeded");</script>
	<%
			}
			else {
				%>
				<h2 class="text-center">Register</h2>
				<br />
				<form name="input" class="text-center">
					<input type="text" name="firstname" onfocus="if (this.value==='First Name') this.value=''"
						value="First Name"
						onblur="if (this.value=='') this.value='First Name'" /><br /> <br />
					<input type="text" name="lastname" onfocus="if (this.value==='Last Name') this.value=''"
						value="Last Name" onblur="if (this.value=='') this.value='Last Name'" />
					<br /> <br /> <input type="text" name="username"
						onfocus="if (this.value==='Username') this.value=''" value="Username"
						onblur="if (this.value=='') this.value='Username'" /><br /> <br />
					<input type="password" name="password" onfocus="if (this.value==='Password') this.value=''"
						value="Password" onblur="if (this.value=='') this.value='Password'" /><br />
					<br /> <input type="submit" id="btnSubmit" name="btnSubmit"
						value="Register" />
				</form>
				<script>alert("Insert failed - choose another username please.");</script><%
			}
		} else {
	%>
	<h2 class="text-center">Register</h2>
	<br />
	<form name="input" class="text-center">
		<input type="text" name="firstname" onfocus="this.value=''"
			value="First Name"
			onblur="if (this.value=='') this.value='First Name'" /><br /> <br />
		<input type="text" name="lastname" onfocus="this.value=''"
			value="Last Name" onblur="if (this.value=='') this.value='Last Name'" />
		<br /> <br /> <input type="text" name="username"
			onfocus="this.value=''" value="Username"
			onblur="if (this.value=='') this.value='Username'" /><br /> <br />
		<input type="password" name="password" onfocus="this.value=''"
			value="Password" onblur="if (this.value=='') this.value='Password'" /><br />
		<br /> <input type="submit" id="btnSubmit" name="btnSubmit"
			value="Register" />
	</form>
	<%
		}
	%>
</body>
</html>