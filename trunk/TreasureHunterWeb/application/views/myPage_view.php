<H2> Account information</H2>
<table class="mypage">
	<tr>
		<td>Username:</td>
		<td><? echo $myPage['username'];?></td>
	</tr>
	<tr>
		<td>Total score:</td>
		<td><? echo $myPage['score'];?></td>
	</tr>
	<tr>
		<td>Locations finished:</td>
		<td><? echo $myPage['numberOfLocations'];?></td>
	</tr>
</table>
<? if($myPage['scoreAbove'] > 0)
		echo 'Next user above you has '.$myPage['scoreAbove'].' points.';
	else
		echo 'You are the highest ranking player';
?>