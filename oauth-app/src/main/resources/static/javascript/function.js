/* Javascript file for interface */


/* variable for uploading files */
var uploaded

/* variable for count */
var count


/* Error Handling*/
function error(e)
{
	bootbox.alert("Error uploading file");
}

/* variable for uploaded files  */
var uploadedTotal

/* variable for Total files  */
var fileTotal 

/* Uploader method*/
function uploader()
{
	/* Validation */
	if(!handler())
	{
		bootbox.alert("Select files");
	}
	else
	{

			uploaded = uploadedTotal = 0;
			/*Upload next file method*/
			fileQueue();

	}	
}

/* Upload Listener   */
function uploadSuccess(e) 
{
	/* Getting uploaded id */
	uploaded += document.getElementById('files').files[uploadedTotal].size;
	/* Increment ID*/
	uploadedTotal++;
	/* Debug */
	debug('complete ' + uploadedTotal + " of " + count);
	/* Debug */
	debug('uploaded: ' + uploaded);
	/* Validation */
	if (uploadedTotal < count)
	{
		fileQueue();
	} else
	{
		var bar = document.getElementById('bar');

	}
}

/* file Selection method */
function fileSelection(e) 
{
	/* select target files */
	var files = e.target.files; 
	/*array of output */
	var output = [];
	/* length */
	count = files.length;
	/* initialization */
	fileTotal = 0;
	/* loop for selection */
	for (var i = 0; i < count; i++) 
	{
		
		var file = files[i];
		/* file size output */
		output.push(file.name, ' (', file.size, ' bytes, ',
				file.lastModifiedDate.toLocaleDateString(), ')');
		output.push('<br/>');
		/* Debug  */
		debug('add ' + file.size);
		/* Total variable increment */
		fileTotal += file.size;
	}
	/* Get ID of element */
	document.getElementById('selectedFiles').innerHTML = output.join('');
}

/* Update UI */
function updateUi(e) 
{
	/* Condition*/
	if (e.lengthComputable)
	{
		/* variable for status*/
		var statusp = parseInt((e.loaded + totalFileUploaded) * 100 / fileTotal);
		/* Validation for status*/
		if(statusp>100)
			percentstatuspComplete = 100;
		var bar = document.getElementById('bar');
		/* style*/
		bar.style.width = statusp + '%';
		/* UI update*/
		bar.innerHTML = statusp + ' % complete';
	} else 
	{
		
		debug('error');
	}
}



/* File queue*/
function fileQueue() 
{
	/* request for XMLHTTPRequest */
	var req = new XMLHttpRequest();
	/* form data */
	var fd = new FormData();
	/* file element */
	var file = document.getElementById('files').files[uploadedTotal];
	/* multipart file*/
	fd.append("multipartFile", file);
	/* progress of the listener */
	req.upload.addEventListener("progress", updateUi, false);
	/*  load listener*/
	req.addEventListener("load", uploadSuccess, false);
	/*  error listener */
	req.addEventListener("error", error, false);
	/*  POST method */
	req.open("POST", "filesupload");
	/* log the form data */
    console.log("fd", file);
    /* send the form data */
    req.send(fd);
}

/* Checking null error*/
function handler()
{
	/*  file id */
	var file = document.getElementById("files").files;
	/* file length  */
	var length = file.length;
	/* length validation */
	if(length <= 0) {
		
		return false;
	}
	else 
	{
		return true;
	}			
}


/* Uploader and File onLoad */
window.onload = function() 
{
	/* Listener for upload*/
	document.getElementById('uploadButton').addEventListener('click', uploader, false);
	/* Listener for files*/
	document.getElementById('files').addEventListener('change', fileSelection, false);
	
		
}






