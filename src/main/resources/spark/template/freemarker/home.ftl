<#assign content>
<br>

<form action="/board" method="get" id="inputForm">
	<input id="nameEntry" placeholder="Username">
	<br>
	How many players to a game? 
	<select id="numPlayersDesired">
	  <option value="4">4 players</option>
	  <option value="3">3 players</option>
	</select>
	<br>
	<button id="startGameButton" onclick="return startGamePressed()">Start!</button>
</form>
</#assign>
<script src="js/home.js"></script>
<#include "main.ftl">
