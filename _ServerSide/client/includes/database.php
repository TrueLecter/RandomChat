<?

	class CONFIG{
		static $DBH;
	}

	$host = 'localhost';
	$user = 'root';
	$password = '';
	$db = 'test';
	
	if (!isset(CONFIG::$DBH)){
		try {  
  			$DBH = new PDO("mysql:host=$host;dbname=$db", $user, $password);  
  			$DBH->setAttribute( PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION );  
			CONFIG::$DBH = $DBH;
		} catch(PDOException $e) {  
    		die( json_encode(array('error'=>'-1','errorMsg'=>'Хьюстон, у нас проблемы.')) );  
    		file_put_contents('PDOErrors.txt', $e->getMessage(), FILE_APPEND);  
		}
	}
	$DBH = CONFIG::$DBH;


?>