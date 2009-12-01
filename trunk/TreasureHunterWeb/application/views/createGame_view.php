<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" 

      "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<script type="text/javascript" src="http://www.google.com/jsapi?key=ABQIAAAADly3sU72O1MLzIITqgcgRRSGno-oF5LX4ZAUAedHnT2hX-BFkBRR0udFKNaVVaSs5YH8BJtf-9Z-0g"></script>
<script type="text/javascript" charset="utf-8">
    google.load("maps", "2.x");
    google.load("jquery", "1.3.1");
</script>

<script type="text/javascript" src="<?=base_url()?>/javascript/createGame.js"></script>

<link href="system/application/views/css/createGame.css" type="text/css" rel="stylesheet" />

</head>

<body bgcolor="#fff">
<H1>Create new game</H1>
<b>Directions</b><br />
<ul>
	<li>Double click to add a location</li>
	<li>Double click on a location to remove it from the game</li>
	<li>Single click on a location to add or change hint</li>
	<li>The blue circle is the coverage of the location, avoid having overlapping locations</li>
</ul>
If you are adding your last location, you can add some congratiulation note for the<br /> user who solves the quest.
<div id="wrapper"><p>
	<div id="mapWrap">
		<div style="color:#CCCCCC">
			<div id="map"></div>
		</div>
	</div>
	<div id="listWrap">
		<ul id="list"></ul>
	</div>
</div>



	<div id="inputFields">
		<p><div id="submitError" style="display:none;">Game name and all hints are needed before you create a game</div></p>
		<? echo form_open('game/addGame'); ?>
		<form id="gameForm">
		<p>
		<label> <b>Game name</b> </label><br />
		<input type="text" id="gameName" name="gameName" />
		
		<input type="hidden" id="jsonData" name="jsonData"/>
		
		<input type="submit" id="submit" name="submit" value="Create game"/>
		</p>
		<? echo form_close('');?>
	</div>

	
<!--<div id="message" style="display:none;">test text</div>-->
<div id="message" style="display:none;"><input type="text" size="8" /></div>

  </body>
</html>
