<H3> Top 20 list </H3>
<p>
Here you can see the highest ranking treasure hunters!
</p>
<table cellpadding="0" cellspacing="0">
	<tr>
		<td>User</td>
		<td class="paddedColumn">Score</td>
	</tr>
<?
foreach($highscores['result'] as $row){
	echo "<tr class='highlightRow'>";
			echo "<td class='column1'>".$row['username']."</td>";
			echo "<td class='paddedColumn'>".$row['score']."</td>";		
	echo "</tr>";
	}
?>

</table>