
package oauth;

import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 
 * @author Rathnayaka 
 *  
 *  
 */

@Component
/* Property values of the application */
@PropertySource("classpath:application.properties")
/* Configuration*/
@ConfigurationProperties

public class Application
{

	/* Setting temporary Folder */
	public void setTemp(String tempFolder) 
	{
		this.tempFolder = tempFolder;
	}
	
	/* Upload Folder */
	@Value("${upload.path}")
	private String tempFolder;
	
	/* Getting temporary Folder */
	public String getTemp() 
	{
		return tempFolder;
	}

	/* Getting Callback */	
	public String getCALLBACK_URI()
	{
		return CALLBACK_URI;
	}

	/* Setting Callback */
	public void setCALLBACK_URI(String cALLBACK_URI)
	{
		CALLBACK_URI = cALLBACK_URI;
	}

	/* Getting Secret Key */
	public Resource getKey() 
	{
		return this.gdSecretKeys;
		
	}
	
	/* Google Credentials */
	@Value("${credentials.path}")
	private Resource credentialsFolder;
	
	
	/* Setting Credentials Folder */
	public void setFolder(Resource credentialsFolder)
	{
		this.credentialsFolder = credentialsFolder;
	}
	
	/* Google Secret path */
	@Value("${secret.path}")
	private Resource gdSecretKeys;

	/* Setting keys*/
	public void setKey(Resource gdSecretKeys)
	{
		this.gdSecretKeys = gdSecretKeys;
	}

	/* Getting credentials folder*/
	public Resource getFolder()
	{
		return credentialsFolder;
	}	
	
	/* OAuth Callback URL */
	@Value("${oauth.url}")
	private String CALLBACK_URI;	

}
