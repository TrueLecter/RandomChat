<?
	class CONFIG{
		static $link;
	} 
	$host = 'localhost';
	$user = 'root';
	$password = '';
	$db = 'test';
	if (!isset(CONFIG::$link)){
		CONFIG::$link = mysqli_connect($host, $user, $password, $db);
	}
	$link = CONFIG::$link;
?>