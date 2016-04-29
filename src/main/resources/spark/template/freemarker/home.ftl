<#assign content>

<div class="container-fluid text-center">
	<h1>CS032 Catan</h1>
	<div class="row">
		<div class="col-xs-2"></div>
		<div class="panel panel-default col-xs-4">
			<div class="panel-body">
				<form action="/board" method="get" id="inputForm">
					<input id="nameEntry" class="form-control" placeholder="Username">
					<div class="form-group">
						<label for="numPlayersDesired">How many players to a game?</label>
						<select id="numPlayersDesired" class="form-control">
							<option value="4">4 players</option>
							<option value="3">3 players</option>
						</select>
					</div>
					<button id="startGameButton" class="btn btn-success" onclick="return startGamePressed()">Start!</button>
				</form>
			</div>
		</div>
		<div class="panel panel-default col-xs-4">
			<div class="panel-heading>
				<h3>Join a Game</h3>
			</div>
			<div class="panel-body">
				<ul class="list-group" id="games-list"></ul>
			</div>
		</div>
		<div class="col-xs-3"></div>
	</div>
</div>
</#assign>
<script src="js/home.js"></script>
<#include "main.ftl">
