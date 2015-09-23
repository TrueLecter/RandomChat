<?php
	header('Content-type: text/html');
    require_once 'includes/xen_api.php';
    include 'includes/database.php';
    include 'includes/config.php';
    include 'includes/utils.php';

    function getMsg($DBH, $dateB, $limit, $xenAPI, $apiKey){
        if (!isset($dateB) || !$dateB){
            $dateS = '0';
        } else {
            $dateS = $dateB;
        }
        $limitS = $limit;
        if (!isset($limit) || !$limit){
            $limitS = 150;
        }
        if (!isset($limit) && !isset($dateB)){
            $limitS = 150;
            $dateS = '0';
        }
        $q = 'SELECT dark_taigachat.id, dark_taigachat.user_id, dark_taigachat.room_id, dark_taigachat.date, dark_taigachat.message, xf_user.username  FROM `dark_taigachat`, `xf_user` WHERE xf_user.user_id = dark_taigachat.user_id AND dark_taigachat.date > :dateS ORDER BY dark_taigachat.date DESC LIMIT :limitS';
        $STH = $DBH->prepare($q);
        $STH -> setFetchMode(PDO::FETCH_ASSOC);
        $STH -> bindValue(':dateS', intval($dateS), PDO::PARAM_INT);
        $STH -> bindValue(':limitS', intval($limitS), PDO::PARAM_INT);
        $STH -> execute();
        $table = array();
        $i = 0;
        while ($row = $STH->fetch()) {
			$table[] = array('id' => $row['id'], 'uid' => $row['user_id'], 'rid' => $row['room_id'], 'date' => $row['date'], 'msg' => strip_tags($row['message']), 'uname' => $row['username'], 'avatar' =>'notImplmented');
            $i = $i + 1;
        }
        if ($i == 0) {
            $table = array('empty' => true);
        }

        $response = array('msg' => $table,'time' => time(),'dbg' => $q);
        return $response;
    }

	$xenAPI = new XenAPI($apiUrl, $apiKey);   
    $err = 0;
    try{
        $params = $_REQUEST;
        if ($canShowChatForNonLogged || checkAuthHash($params['u'],$params['hash'],$XenAPI)){
    	    die(json_encode(getMsg($DBH,$params['date'],$params['limit'],$xenAPI,$apiKey),128));
	    } else {
	    	$err = 322;
	    	$errMsg = 'You must be logged in.';
	    }
	}
    catch (Exception $e) { 
        $errMsg ='API call failed: HTTP RESPONSE=' . $e->getMessage() . ' & HTTP STATUS CODE=' . $e->getCode(); 
        $err = $e -> getCode();
    }

    die(json_encode(array('error' => $err, 'errorMsg' => $errMsg),128));
?>