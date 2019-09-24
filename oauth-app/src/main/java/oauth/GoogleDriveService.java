package oauth;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import oauth.GoogleDriveService;

import oauth.Application;

/**
 * 
 * @author Rathnayaka
 *
 *
 *
 */

/* Class Implementaion for Google Drive Service*/

@Service
public class GoogleDriveService {	
	
	
	/* Google Drive instance*/
	private Drive InstanceDrive;	
	
	@Autowired
	/* Application class instance */
	private Application InstanceApp;

	//Method for signing out from the application
	public void signOut(HttpServletRequest request)
	{
		/* Http Session status*/
		HttpSession se = request.getSession(false);
		/* Session status*/
		se = request.getSession(true);
		/* Session validation */
		if (se != null)
		{
			se.invalidate();
		}
	}
	
	
	/* Google Authorization Code Flow instance*/
	private GoogleAuthorizationCodeFlow authcode;
	
	
	//Method for signing in for the application
	public void signIn(HttpServletResponse response) throws IOException
	{
		/* Authorization url*/
		GoogleAuthorizationCodeRequestUrl url = authcode.newAuthorizationUrl();
		/* Redirection*/
		String backURL = url.setRedirectUri(InstanceApp.getCALLBACK_URI()).setAccessType("offline").build();
		
		response.sendRedirect(backURL);
	}
	
	/* API calls HttpTransport */
	private static HttpTransport HTTP_TRANSPORT = new NetHttpTransport();	
	

	/* Application Name */
	private static final String APP = "OAuth Application";
	
	
	/* JSON Factory Default Instance */
	private static JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();	

	/* Scope list*/
	private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE);
	
	//Initialization
	@PostConstruct
	public void init() throws IOException 
	{
		/* Load secret using GoogleClientSecrets*/
		GoogleClientSecrets pw = GoogleClientSecrets.load(JSON_FACTORY,
				new InputStreamReader(InstanceApp.getKey().getInputStream()));
		/* set credentials using GoogleAuthorizationCodeFlow */
		authcode = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, pw, SCOPES)
				.setDataStoreFactory(new FileDataStoreFactory(InstanceApp.getFolder().getFile())).build();
		/* Calling authcredential method*/
		InstanceDrive = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, authcredential()).setApplicationName(APP)
				.build();
	}

	/* Session user */
	private static final String USER = "USER";
	
	//Method for getting credentials
	public Credential authcredential() throws IOException 
	{
		return authcode.loadCredential(USER);
	}

	//Method for user authentication
	public boolean authentication() throws IOException
	{

		/* Credential instance*/
		Credential cred = authcredential();
		/* token status*/
		boolean status = false;
		/* credential validation */
		if (cred != null) 
		{
			status = cred.refreshToken();
			/* return status*/
			return status;
		}
		/* return status*/
		return status;
	}

	
	//Method to store credentials
	public boolean storeCredentials(HttpServletRequest request) throws IOException
	{
		/* Requesting code parameter*/
		String cd = request.getParameter("code");
		/* Code validation*/
		if (cd != null) 
		{
			/* save code */
			tokenStore(cd);
			return true;
		}
		return false;
	}

	//Method to save token
	private void tokenStore(String code) throws IOException 
	{
		/* Callback execution */
		GoogleTokenResponse response = authcode.newTokenRequest(code).setRedirectUri(InstanceApp.getCALLBACK_URI()).execute();
		/* Create and store */
		authcode.createAndStoreCredential(response, USER);
	}
	
	private static Logger logger = LoggerFactory.getLogger(GoogleDriveService.class);

	//Method to upload files to the drive using Google Drive Service
	public void fileUpload(MultipartFile multipartFile) throws IllegalStateException, IOException 
	{
		/* Type */
		String Ctype = multipartFile.getContentType();
		/* File Name */
		String OFile = multipartFile.getOriginalFilename();
		/* Temporary Path*/
		String temp = InstanceApp.getTemp();
		/* Temp to Original*/
		File CFile = new File(temp, OFile);

		multipartFile.transferTo(CFile);
		com.google.api.services.drive.model.File metaDataFile = new com.google.api.services.drive.model.File();
		metaDataFile.setName(OFile);
		FileContent contentF = new FileContent(Ctype, CFile);
		/* Updates a file's metadata and content */
		com.google.api.services.drive.model.File verifyFile = InstanceDrive.files().create(metaDataFile, contentF)
				.setFields("id").execute();
		
		
		logger.info("Created File: "+verifyFile.getId());
		
	}
	
	
	
	
}
