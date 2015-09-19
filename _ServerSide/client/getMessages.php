<?php
	header('Content-type: text/html');
    require_once 'includes/xen_api.php';
    include 'includes/database.php';
    include 'includes/config.php';
    include 'includes/utils.php';

    function getMsg($DBH, $date, $limit, $xenAPI, $apiKey){
        $dateS = ' WHERE date > '.$date;
        if (!isset($date) || !$date){
            $dateS = '';
        }

        $limitS = ' LIMIT '.$limit;
        
        if (!isset($limit) || !$limit){
            $limitS = '';
        }
        
        if (!isset($limit) && !isset($date)){
            $limitS = ' LIMIT 150';
        }
        $q = 'SELECT * FROM `dark_taigachat`' . $dateS . ' ORDER BY date DESC'.$limitS;
        $STH = $DBH->query($q);
        $STH->setFetchMode(PDO::FETCH_ASSOC);
        $table = array();
        $i = 0;
        while ($row = $STH->fetch()) {
			$q = 'SELECT username FROM `xf_user` WHERE user_id = ' . $row['user_id'] ;
			$STHU = $DBH->query($q);
            $STHU->setFetchMode(PDO::FETCH_OBJ);
			$r = $STHU->fetch();
			$table[] = array('id' => strip_tags($row['id']), 'uid' => $row['user_id'], 'rid' => $row['room_id'],'date' => $row['date'],'msg' => $row['message'],'uname'=>$r->username , 'avatar' =>'notImplmented');
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