<H3> Played Games </H3>
<p>
A list of all games you have played on your mobile, if it's not marked as active other users won't be able to join you in that particular game. 
</p>
<table id="myGames" cellpadding="0" cellspacing="0">
	<tr>
		<td class="myGameName"> Game name </td>
		<td class="paddedColumn"> Finished </td>
		<td class="paddedColumn"> Active </td>
	</tr>
<?
foreach($playedGames['queryResult'] as $row) {
	echo "<tr class='highlightRow'>";
		echo "<td class='column1'>".$row['gameName']."</td>";

		// finished column
		echo "<td class='paddedColumn'>";
		if($row['finished']) {
			echo '<img src="system/application/views/images/update.png" border="0" title="You have finished this game">';
			}
		else {
			echo '<img src="system/application/views/images/delete.png" border="0" title="This game is unfinished">';
			}
		echo "</td>";		
		
		// active column
		echo "<td class='paddedColumn'>";
		if($row['active']) {
			echo '<img src="system/application/views/images/update.png" border="0" title="Game is active">';
			}
		else {
			echo '<img src="system/application/views/images/delete.png" border="0" title="Game is inactive">';
			}
		echo "</td>";


	echo "</tr>";
	}
?>
</table>