
/* Type model for the application */

package oauth;

import org.springframework.web.multipart.MultipartFile;

public class Type {
	
	/* Multipart file instance */
	private MultipartFile multipartFile;	
	/*  Getter for Multipart*/
	public MultipartFile getMultipartFile() 
	{
		return multipartFile;
	}
	/* Setter for Multipart*/
	public void setMultipartFile(MultipartFile multipartFile)
	{
		this.multipartFile = multipartFile;
	}
	
	private static final long serialVersionUID = 1L;
	
	/* Serial Getter */
	public static long getSerialversionuid()
	{
		return serialVersionUID;
	}
}
