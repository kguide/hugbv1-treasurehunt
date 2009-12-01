<?php
require('tconfig.php');
$TConfig = new TConfig(); // heldur utan um allar statiskar breytur
mysql_connect( $TConfig->dbHost , $TConfig->dbUsername , $TConfig->dbPassword ) or die (mysql_error());
mysql_select_db( $TConfig->dbName) or die (mysql_error());
?>