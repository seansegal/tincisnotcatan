<#assign content>

<div class="background-viewport">
	<div class="hexagon-wrapper-home" id="animated-hex">
		<div class="hexagon-home"></div>
	</div>
</div>

<div class="container-fluid text-center">
	<h1>CS032 Catan</h1>
	<div class="row" id="pre-name-container">
		<div class="col-xs-4"></div>
		<div class="col-xs-4">
			<div class="input-group">
		    	<input type="text" id="nameEntry" class="form-control input-lg" placeholder="Enter username...">
		    	<span class="input-group-btn">
		    		<button class="btn btn-success input-lg" type="button" id="enter-name-begin-btn">Begin!</button>
		    	</span>
		    </div>
		</div>
		<div class="col-xs-4"></div>
	</div>
	<div class="row hidden" id="post-name-container">
		<div class="col-xs-2"></div>
		<div class="col-xs-4">
			<div class="panel panel-default">
				<div class="panel-heading">
					<h3 class="panel-title">Create a Game</h3>
				</div>
				<div id="game-options-panel" class="panel-body row">
					<form action="/board" method="get" id="inputForm">
						<input id="game-name-entry" class="form-control" placeholder="Lobby name">
							<div class="col-xs-6">
								<div class="form-group">
									<label for="numPlayersDesired">Number of Players</label>
									<select id="numPlayersDesired" class="form-control">
										<option value="4">4 players</option>
										<option value="3">3 players</option>
										<option value="2">2 players</option>
									</select>
								</div>
							</div>
							<div class="col-xs-6">
								<div class="form-group">
									<label for="victory-points-input">Victory Points</label>
									<input id="victory-points-input" class="form-control" type="number" min="5" max="15" value="10" step="1">
								</div>
							</div>
						<label>Board</label>
						<br>
						<div class="btn-group" data-toggle="buttons">
							<label class="btn btn-default active">
    							<input type="radio" autocomplete="off" checked>Random
  							</label>
  							<label class="btn btn-default" id="default-board-option">
    							<input type="radio" autocomplete="off">Standard
  							</label>
						</div>
						<br>
						<label id="decimal-resources-label">Decimal Resource Amounts</label>
						<br>
						<div id="decimal-container" class="btn-group" data-toggle="buttons">
							<label class="btn btn-default active" id="integer-option">
    							<input type="radio" autocomplete="off" checked>Integer
  							</label>
  							<label class="btn btn-default" id="decimal-option">
    							<input type="radio" autocomplete="off">Decimal
  							</label>
						</div>
						<br>
						<div id="dynamic-rates-container" class="hidden">
							<label>Exchange Rates</label>
							<br>
							<div class="btn-group" data-toggle="buttons">
								<label class="btn btn-default active">
	    							<input type="radio" autocomplete="off" checked>Standard
	  							</label>
	  							<label class="btn btn-default" id="dynamic-rates-option">
	    							<input type="radio" autocomplete="off">Dynamic
	  							</label>
							</div>
						</div>
						<br>
						<button id="startGameButton" class="btn btn-success" onclick="return startGamePressed()">Create Game!</button>
					</form>
				</div>
			</div>
		</div>
		<div class="col-xs-4">
			<div class="panel panel-default" id="join-game-panel">
				<div class="panel-heading">
					<h3 class="panel-title">Join a Game</h3>
				</div>
				<form action="/board" method="get" id="existingGameForm">
					<ul class="list-group" id="games-list">
						<li class="list-group-item">No available games. Create your own!</li>
					</ul>
				</form>
			</div>
		</div>
		<div class="col-xs-3"></div>
	</div>
</div>

<div class="modal fade" id="use-chrome-modal" tabindex="-1" role="dialog" aria-labelledby="useChromeLabel" data-backdrop="static">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
      		<div class="modal-header">
        		<h4 class="modal-title" id="useChromeLabel">We Noticed You Were Using a Browser Other Than Chrome...</h4>
      		</div>
      		<div class="modal-body">
       			<p>For the best user experience, please use Chrome as your web browser.</p>
      		</div>
      		<div class="modal-footer">
        		<button type="button" class="btn btn-success" data-dismiss="modal">Okay</button>
      		</div>
    	</div>
	</div>
</div>

</#assign>
<#include "main.ftl">
<script src="js/home.js"></script>

