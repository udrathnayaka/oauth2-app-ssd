package oauth;

import java.io.IOException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import oauth.GoogleDriveService;
import oauth.Type;

/**
 *  
 * @author Rathnayaka 
 *  
 *  
 *  
 */

/* Class for the router of the applicaion */

@Controller
public class ApplicationRouter {

	@Autowired
	GoogleDriveService appservice;

	
	/* Maps HTTP GET requests for sign in */
	@GetMapping("/signin")
	public void signin(HttpServletResponse response) throws IOException 
	{
		/* Drive Service response */
		appservice.signIn(response);
	}

	/* Maps HTTP GET requests for sign out */
	@GetMapping("/signout")
	public String signout(HttpServletRequest request) throws IOException 
	{
		appservice.signOut(request);
		/* Returns to index.html */
		return "log.html/";
	}
	
	/* Maps HTTP GET requests for upload files */
	@PostMapping("/filesupload")
	public String file(HttpServletRequest servletRequest, @ModelAttribute Type file)
			throws IllegalStateException, IOException
	{
		/* Upload to drive */
		appservice.fileUpload(file.getMultipartFile());
		/* Returns to home.html */
		return "application.html";
	}


	/* Maps HTTP GET requests for / */
	@GetMapping("/")
	public String auth() throws IOException 
	{
		/* Returns to home.html : index.html */
		return appservice.authentication() ? "application.html" : "log.html";
	}

	/* Maps HTTP GET requests for /oauth */
	@GetMapping("/oauth")
	public String save(HttpServletRequest request) throws IOException
	{
		/* Returns to home.html : index.html */
		return appservice.storeCredentials(request) ? "application.html" : "log.html";
	}

	

}
