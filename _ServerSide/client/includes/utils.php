<?php
	include 'config.php';

	function checkAuthHash($user, $hash, $xenAPI){
    	$xenAPI -> setAction('testauth');
		$xenAPI -> setParameters(array('hash' => $user.':'.$hash));
		try {
			$resp = $xenAPI -> execute();
		} catch (Exception $e) {
			$error = json_decode($e->getMessage(), TRUE);
	        return $error['error'] === 11;
    	}
	}

	function getUserAvatar($value, $xenAPI, $apiKey){
    	$xenAPI -> setAction('getAvatar');
		$xenAPI -> setParameters(array('value' => $value, 'hash'=> $apiKey, 'size' => 's'));
		try {
			$resp = $xenAPI -> execute();
		} catch (Exception $e) {
			return false;
		}
		//$resp = json_decode($resp);
		return $resp['avatar'];
	}

	function getUserName($value, $xenAPI, $apiKey){
    	$xenAPI -> setAction('getUser');
		$xenAPI -> setParameters(array('value' => $value, 'hash'=> $apiKey));
		try {
			$resp = $xenAPI -> execute();
		} catch (Exception $e) {
			return false;
		}
		//var_dump($resp);
		//$resp = json_decode($resp);
		return $resp['username'];
	}

	function getUserID($link, $name){
		$q = "SELECT `user_id` as uid FROM `xf_user` WHERE `username` = '$name'";
		//print($q);
		$r = mysqli_query($link, $q);
		$r = mysqli_fetch_array($r);
		return $r['uid'];
	}

?>