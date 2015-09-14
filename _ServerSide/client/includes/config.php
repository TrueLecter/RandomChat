<? 
	$debug = false; 
	
	if($debug){
		error_reporting(E_ALL);
		ini_set('xdebug.var_display_max_depth', -1);
		ini_set('xdebug.var_display_max_children', -1);
		ini_set('xdebug.var_display_max_data', -1); 
	}
	else {
    	error_reporting(0); 
	}
	$apiUrl = 'http://taigachat:81/api.php';
	$apiKey = 'e7dcc54b006e35b8d2c587b4073a4fcc';
	$defaultMsgCount = 150;
	$canShowChatForNonLogged = true; //Not implemented yet
?>