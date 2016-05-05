<#assign content>

<div id="board-viewport" class="sea-color unselectable"></div>

<div class="row above-board">
	<div class="col-xs-3">
		<div id="turn-display-container"></div>
		<input type="button" class="btn btn-primary" id="end-turn-btn" value="End Turn"><br>
		<input type="button" class="btn btn-danger" id="leave-game-btn" value="Leave Game">
		<div id="chat-container">
		    <div id="chat"></div>
			<div id="chatControls">
	        	<input id="message" placeholder="Type your message">
	    	</div>
		</div>
	</div>
	<div class="col-xs-6"></div>
	<div class="col-xs-3" id="right-menu-container">
		<ul class="nav nav-pills nav-stacked col-xs-3" id="right-tab-tabs" role="tablist">
	    	<li role="presentation" id="players-tab-toggle" class="active right-tab"><a href="#players-tab" aria-controls="players-tab" role="tab" data-toggle="tab">Players</a></li>
	    	<li role="presentation" id="build-tab-toggle" class="right-tab"><a href="#build-tab" aria-controls="build-tab" role="tab" data-toggle="tab">Build</a></li>
	    	<li role="presentation" id="trade-tab-toggle" class="right-tab"><a href="#trade-tab" aria-controls="trade-tab" role="tab" data-toggle="tab">Trade</a></li>
		</ul>
		<div class="tab-content right-tab-content col-xs-9">
		    <div role="tabpanel" class="tab-pane active right-tab-pane" id="players-tab">
		    	 <ul class="nav nav-tabs right-inner-tabs" role="tablist" id="player-tabs"></ul>
				 <div class="tab-content" id="player-tabs-content"></div>
		    </div>
		    <div role="tabpanel" class="tab-pane right-tab-pane" id="build-tab">
		    	<ul class="list-group">
			    	<li class="list-group-item">
		    			<input type="button" class="btn btn-default build-btn" id="settlement-build-btn" value="Build Settlement">
				    	<br>
				    	<span>1</span>
				    	<div class="circle build-circle brick-color">
							<img src="images/icon-brick.svg" alt="Brick">
						</div>
						<br>
						<span>1</span>
				    	<div class="circle build-circle wood-color">
							<img src="images/icon-wood.svg" alt="Wood">
						</div>
						<br>
						<span>1</span>
				    	<div class="circle build-circle wheat-color">
							<img src="images/icon-wheat.svg" alt="Wheat">
						</div>
						<br>
						<span>1</span>
				    	<div class="circle build-circle sheep-color">
							<img src="images/icon-sheep.svg" alt="Sheep">
						</div>
			    	</li>
		    		<li class="list-group-item">
		    			<input type="button" class="btn btn-default build-btn" id="city-build-btn" value="Build City">
			    		<div class="row">
				    		<span class="build-circle-list-number">3</span>
				    		<div class="circle build-circle ore-color build-circle-list">
								<img src="images/icon-ore.svg" alt="Ore">
							</div>
				    		<div class="circle build-circle ore-color build-circle-list">
								<img src="images/icon-ore.svg" alt="Ore">
							</div>
							<div class="circle build-circle ore-color build-circle-list">
								<img src="images/icon-ore.svg" alt="Ore">
							</div>
						</div>
						<div class="row">
							<span class="build-circle-list-number">2</span>
							<div class="circle build-circle wheat-color build-circle-list">
								<img src="images/icon-wheat.svg" alt="Wheat">
							</div>
							<div class="circle build-circle wheat-color build-circle-list">
								<img src="images/icon-wheat.svg" alt="Wheat">
							</div>
						</div>
		    		</li>
		    		<li class="list-group-item">
				    	<input type="button" class="btn btn-default build-btn" id="road-build-btn" value="Build Road">
				    	</br>
				    	<span>1</span>
				    	<div class="circle build-circle brick-color">
							<img src="images/icon-brick.svg" alt="Brick">
						</div>
						</br>
						<span>1</span>
				    	<div class="circle build-circle wood-color">
							<img src="images/icon-wood.svg" alt="Wood">
						</div>
		    		</li>
		    		<li class="list-group-item">
				    	<input type="button" id="buy-dev-card-modal-open" class="btn btn-default build-btn" value="Buy Development Card" data-toggle="modal" data-target="#buy-dev-card-modal">
				    	</br>
				    	<span>1</span>
				    	<div class="circle build-circle ore-color">
							<img src="images/icon-ore.svg" alt="Ore">
						</div>
						</br>
						<span>1</span>
				    	<div class="circle build-circle wheat-color">
							<img src="images/icon-wheat.svg" alt="Wheat">
						</div>
						</br>
						<span>1</span>
				    	<div class="circle build-circle sheep-color">
							<img src="images/icon-sheep.svg" alt="Sheep">
						</div>
		    		</li>
		    	</ul>
		    </div>
		    <div role="tabpanel" class="tab-pane right-tab-pane" id="trade-tab">
			    <ul class="nav nav-tabs right-inner-tabs" role="tablist">
					<li role="presentation" class="active" id="interplayer-trade-tab-toggle">
						<a href="#player-trade-tab" aria-controls="home" role="tab" data-toggle="tab">Interplayer</a>
					</li>
					<li role="presentation" id="bank-trade-tab-toggle">
						<a href="#bank-trade-tab" aria-controls="profile" role="tab" data-toggle="tab">Bank</a>
					</li>
				</ul>
		    	<div class="tab-content">
		    		<div role="tabpanel" class="tab-pane active" id="player-trade-tab">
		    			<div class="row trade-row">
		    				<div class="col-xs-4 text-right">
		    					<div class="circle trade-circle brick-color">
									<img src="images/icon-brick.svg" alt="Brick">
								</div>
		    				</div>
		    				<div class="col-xs-4 text-center">
		    					<input type="number" class="form-control interplayer-trade-input" res="brick">
		    				</div>
		    				<div class="col-xs-4 text-left">
		    					<h5> </h5>
		    				</div>
		    			</div>
		    			<div class="row trade-row">
		    				<div class="col-xs-4 text-right">
		    					<div class="circle trade-circle wood-color">
									<img src="images/icon-wood.svg" alt="Wood">
								</div>
		    				</div>
		    				<div class="col-xs-4 text-center">
		    					<input type="number" class="form-control interplayer-trade-input" res="wood">
		    				</div>
		    				<div class="col-xs-4 text-left">
		    					<h5> </h5>
		    				</div>
		    			</div>
		    			<div class="row trade-row text">
		    				<div class="col-xs-4 text-right">
		    					<div class="circle trade-circle ore-color">
									<img src="images/icon-ore.svg" alt="Ore">
								</div>
		    				</div>
		    				<div class="col-xs-4 text-center">
		    					<input type="number" class="form-control interplayer-trade-input" res="ore">
		    				</div>
		    				<div class="col-xs-4 text-left">
		    					<h5> </h5>
		    				</div>
		    			</div>
		    			<div class="row trade-row">
		    				<div class="col-xs-4 text-right">
		    					<div class="circle trade-circle wheat-color">
									<img src="images/icon-wheat.svg" alt="Wheat">
								</div>
		    				</div>
		    				<div class="col-xs-4 text-center">
		    					<input type="number" class="form-control interplayer-trade-input" res="wheat">
		    				</div>
		    				<div class="col-xs-4 text-left">
		    					<h5> </h5>
		    				</div>
		    			</div>
		    			<div class="row trade-row">
		    				<div class="col-xs-4 text-right">
		    					<div class="circle trade-circle sheep-color">
									<img src="images/icon-sheep.svg" alt="Sheep">
								</div>
		    				</div>
		    				<div class="col-xs-4 text-center">
		    					<input type="number" class="form-control interplayer-trade-input" res="sheep">
		    				</div>
		    				<div class="col-xs-4 text-left">
		    					<h5> </h5>
		    				</div>
		    			</div>
		    			<div class="row trade-row">
		    				<div class="col-xs-12 text-center">
		    					<input type="button" id="propose-interplayer-trade-btn" class="btn btn-primary" value="Propose Trade" disabled="disabled">
		    				</div>
		    			</div>
		    			<div class="panel panel-default interplayer-trade-panel" id="to-give-panel">
		    				<div class="panel-heading">
		    					<h5 class='panel-title-small'>Resources to Give</h5>
		    				</div>
		    				<div class="panel-body" id="to-give-container">
		    					<p class="to-give-list-item hidden" res="brick"><strong class="trade-number"></strong><span class="inline-trade-icon circle brick-color"><img src="images/icon-brick.svg" alt="Brick"></span></p>
		    					<p class="to-give-list-item hidden" res="wood"><strong class="trade-number"></strong><span class="inline-trade-icon circle wood-color"><img src="images/icon-wood.svg" alt="Wood"></span></p>
		    					<p class="to-give-list-item hidden" res="ore"><strong class="trade-number"></strong><span class="inline-trade-icon circle ore-color"><img src="images/icon-ore.svg" alt="Ore"></span></p>
		    					<p class="to-give-list-item hidden" res="wheat"><strong class="trade-number"></strong><span class="inline-trade-icon circle wheat-color"><img src="images/icon-wheat.svg" alt="Wheat"></span></p>
		    					<p class="to-give-list-item hidden" res="sheep"><strong class="trade-number"></strong><span class="inline-trade-icon circle sheep-color"><img src="images/icon-sheep.svg" alt="Sheep"></span></p>
		    				</div>
		    			</div>
		    			<div class="panel panel-default interplayer-trade-panel" id="to-get-panel">
		    				<div class="panel-heading">
		    					<h5 class='panel-title-small'>Resources to Receive</h5>
		    				</div>
		    				<div class="panel-body" id="to-get-container">
		    					<p class="to-get-list-item hidden" res="brick"><strong class="trade-number"></strong><span class="inline-trade-icon circle brick-color"><img src="images/icon-brick.svg" alt="Brick"></span></p>
		    					<p class="to-get-list-item hidden" res="wood"><strong class="trade-number"></strong><span class="inline-trade-icon circle wood-color"><img src="images/icon-wood.svg" alt="Wood"></span></p>
		    					<p class="to-get-list-item hidden" res="ore"><strong class="trade-number"></strong><span class="inline-trade-icon circle ore-color"><img src="images/icon-ore.svg" alt="Ore"></span></p>
		    					<p class="to-get-list-item hidden" res="wheat"><strong class="trade-number"></strong><span class="inline-trade-icon circle wheat-color"><img src="images/icon-wheat.svg" alt="Wheat"></span></p>
		    					<p class="to-get-list-item hidden" res="sheep"><strong class="trade-number"></strong><span class="inline-trade-icon circle sheep-color"><img src="images/icon-sheep.svg" alt="Sheep"></span></p>
		    				</div>
		    			</div>
		    		</div>
				    <div role="tabpanel" class="tab-pane text-center" id="bank-trade-tab">
				    	<h5>Choose a resource to give</h5>
				    	<div class="bank-to-give-container">
				    		<div class="circle to-give-circle-container" res="brick">
					    		<div class="circle trade-circle brick-color pointer">
									<img src="images/icon-brick.svg" alt="Brick">
								</div>
							</div>
							<h5 id="brick-trade-rate">1:1</h5>
				    	</div>
				    	<div class="bank-to-give-container">
				    		<div class="circle to-give-circle-container" res="wood">
					    		<div class="circle trade-circle wood-color pointer">
									<img src="images/icon-wood.svg" alt="Wood">
								</div>
							</div>
							<h5 id="wood-trade-rate">1:1</h5>
				    	</div>
				    	<div class="bank-to-give-container last-to-give-get-container">
				    		<div class="circle to-give-circle-container" res="ore">
					    		<div class="circle trade-circle ore-color pointer">
									<img src="images/icon-ore.svg" alt="Ore">
								</div>
							</div>
							<h5 id="ore-trade-rate">1:1</h5>
				    	</div>
				    	<br>
				    	<div class="bank-to-give-container">
				    		<div class="circle to-give-circle-container" res="wheat">
					    		<div class="circle trade-circle wheat-color pointer">
									<img src="images/icon-wheat.svg" alt="Wheat">
								</div>
							</div>
							<h5 id="wheat-trade-rate">1:1</h5>
				    	</div>
				    	<div class="bank-to-give-container last-to-give-get-container">
				    		<div class="circle to-give-circle-container" res="sheep">
					    		<div class="circle trade-circle sheep-color pointer">
									<img src="images/icon-sheep.svg" alt="Sheep">
								</div>
							</div>
							<h5 id="sheep-trade-rate">1:1</h5>
				    	</div>
				    	<h5>Choose a resource to receive</h5>
				    	<div class="bank-to-get-container">
				    		<div class="circle to-get-circle-container" res="brick">
					    		<div class="circle trade-circle brick-color pointer">
									<img src="images/icon-brick.svg" alt="Brick">
								</div>
							</div>
				    	</div>
				    	<div class="bank-to-get-container">
				    		<div class="circle to-get-circle-container" res="wood">
					    		<div class="circle trade-circle wood-color pointer">
									<img src="images/icon-wood.svg" alt="Wood">
								</div>
							</div>
				    	</div>
				    	<div class="bank-to-get-container last-to-give-get-container">
				    		<div class="circle to-get-circle-container" res="ore">
					    		<div class="circle trade-circle ore-color pointer">
									<img src="images/icon-ore.svg" alt="Ore">
								</div>
							</div>
				    	</div>
				    	<br>
				    	<div class="bank-to-get-container">
				    		<div class="circle to-get-circle-container" res="wheat">
					    		<div class="circle trade-circle wheat-color pointer">
									<img src="images/icon-wheat.svg" alt="Wheat">
								</div>
							</div>
				    	</div>
				    	<div class="bank-to-get-container last-to-give-get-container">
				    		<div class="circle to-get-circle-container" res="sheep">
					    		<div class="circle trade-circle sheep-color pointer">
									<img src="images/icon-sheep.svg" alt="Sheep">
								</div>
							</div>
				    	</div>
				    	
		    			<div class="row trade-row">
		    				<div class="col-xs-12 text-center">
		    					<input type="button" class="btn btn-primary" value="Trade With Bank" id="bank-trade-btn">
		    				</div>
		    			</div>
		    		</div>
		    	</div>
		    </div>
	    </div>
  	</div>
</div>

<div class="navbar navbar-fixed-bottom above-board">
	<div class="col-xs-3"></div>
	<div class="col-xs-6 text-center">
		<div id="message-container" class="text-center"></div>
		<div class="panel panel-default col-xs-12">
			<ul class="nav navbar-nav navbar-left" id="hand-resources">
				<li class="navbar-btn">
					<div class="circle card-circle brick-color" data-toggle="tooltip" data-placement="top" title="Brick">
						<img src="images/icon-brick.svg" alt="Brick">
					</div>
					<div class="card-number" id="brick-number">0</div>
				</li>
				<li class="navbar-btn">
					<div class="circle card-circle wood-color" data-toggle="tooltip" data-placement="top" title="Wood">
						<img src="images/icon-wood.svg" alt="Wood">
					</div>
					<div class="card-number" id="wood-number">0</div>
				</li>
				<li class="navbar-btn">
					<div class="circle card-circle ore-color" data-toggle="tooltip" data-placement="top" title="Ore">
						<img src="images/icon-ore.svg" alt="Ore">
					</div>
					<div class="card-number" id="ore-number">0</div>
				</li>
				<li class="navbar-btn">
					<div class="circle card-circle wheat-color" data-toggle="tooltip" data-placement="top" title="Wheat">
						<img src="images/icon-wheat.svg" alt="Wheat">
					</div>
					<div class="card-number" id="wheat-number">0</div>
				</li>
				<li class="navbar-btn">
					<div class="circle card-circle sheep-color" data-toggle="tooltip" data-placement="top" title="Sheep">
						<img src="images/icon-sheep.svg" alt="Sheep">
					</div>
					<div class="card-number" id="sheep-number">0</div>
				</li>
			</ul>
			<ul class="nav navbar-nav navbar-right" id="hand-dev-cards">
				<li class="navbar-btn">
					<div class="circle card-circle pointer" id="knight-btn">
						<div data-toggle="popover" data-trigger="hover" data-container="body" data-placement="top" title="Knight" data-content="When you play this card, you move the robber and steal a resource from the owner of an adjacent settlement or city.">
							<img src="images/icon-knight.svg" alt="Knight">
						</div>
					</div>
					<div class="card-number" id="knight-number">0</div>
				</li>
				<li class="navbar-btn">
					<div class="circle card-circle pointer" id="year-of-plenty-btn">
						<div data-toggle="popover" data-trigger="hover" data-container="body" data-placement="top" title="Year of Plenty" data-content="When you play this card, you can select 2 resources of your choice from the bank.">
							<img src="images/icon-year-of-plenty.svg" alt="Year of Plenty">
						</div>
					</div>
					<div class="card-number" id="year-of-plenty-number">0</div>
				</li>
				<li class="navbar-btn">
					<div class="circle card-circle pointer" id="monopoly-btn">
						<div data-toggle="popover" data-trigger="hover" data-container="body" data-placement="top" title="Monopoly" data-content="When you play this card, choose one type of resource. All other players must give you all their resource cards of that type.">
							<img src="images/icon-monopoly.svg" alt="Monopoly">
						</div>
					</div>
					<div class="card-number" id="monopoly-number">0</div>
				</li>
				<li class="navbar-btn">
					<div class="circle card-circle pointer" id="road-building-btn">
						<div data-toggle="popover" data-trigger="hover" data-container="body" data-placement="top" title="Road Building" data-content="When you play this card, you can build 2 roads free of charge.">
							<img src="images/icon-road-building.svg" alt="Road Building">
						</div>
					</div>
					<div class="card-number" id="road-building-number">0</div>
				</li>
				<li class="navbar-btn">
					<div class="circle card-circle" data-toggle="modal">
						<div data-toggle="popover" data-trigger="hover" data-container="body" data-placement="top" title="Victory Point" data-content="You obtain an extra Victory Point with this card, which will remain hidden to other players until the end of the game.">
							<img src="images/icon-victory-point.svg" alt="Victory Point">
						</div>
					</div>
					<div class="card-number" id="victory-point-number">0</div>
				</li>
			</ul>
		</div>
	</div>
	<div class="col-xs-3"></div>
</div>

<!-- Modals -->

<div class="modal fade" id="buy-dev-card-modal" tabindex="-1" role="dialog" aria-labelledby="buyDevCardLabel">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
      		<div class="modal-header">
        		<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        		<h4 class="modal-title" id="buyDevCardLabel">Buy Development Card</h4>
      		</div>
      		<div class="modal-body">
       			<p>Are you sure you would like to buy a Development Card?</p>
       			<span>The cost is </span>
       			<strong>1 </strong>
       			<div class="circle inline-circle ore-color">
					<img src="images/icon-ore.svg" alt="Ore">
				</div>
				<strong>1 </strong> 
				<div class="circle inline-circle wheat-color">
					<img src="images/icon-wheat.svg" alt="Wheat">
				</div>
				<strong>1 </strong>
				<div class="circle inline-circle sheep-color">
					<img src="images/icon-sheep.svg" alt="Sheep">
				</div>
      		</div>
      		<div class="modal-footer">
        		<button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
        		<button type="button" class="btn btn-primary" id="dev-card-buy-btn" data-dismiss="modal">Buy</button>
      		</div>
    	</div>
	</div>
</div>

<div class="modal fade" id="discard-modal" tabindex="-1" role="dialog" aria-labelledby="discardLabel" data-backdrop="static">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
      		<div class="modal-header">
        		<h4 class="modal-title" id="discardLabel">Discard Cards</h4>
      		</div>
      		<div class="modal-body">
       			<p>A 7 was rolled! You must select <span id="num-resources-to-discard"></span> resources to discard.</p>
       			<div class="text-center">
       				<div class="discard-resource-container">
		       			<div class="circle discard-circle brick-color">
							<img src="images/icon-brick.svg" alt="Brick">
						</div>
						<div class="text-center">
							<h5 class="discard-hand-number" id="discard-hand-number-brick"></h5>
				    		<input type="number" class="form-control discard-number" max="0" res="brick">
				    	</div>
				    </div>
				    <div class="discard-resource-container">
		       			<div class="circle discard-circle wood-color">
							<img src="images/icon-wood.svg" alt="Wood">
						</div>
						<div class="text-center">
							<h5 class="discard-hand-number" id="discard-hand-number-wood"></h5>
				    		<input type="number" class="form-control discard-number" max="0" res="wood">
				    	</div>
				    </div>
				    <div class="discard-resource-container">
		       			<div class="circle discard-circle ore-color">
							<img src="images/icon-ore.svg" alt="Ore">
						</div>
						<div class="text-center">
							<h5 class="discard-hand-number" id="discard-hand-number-ore"></h5>
				    		<input type="number" class="form-control discard-number" max="0" res="ore">
				    	</div>
				    </div>
				    <div class="discard-resource-container">
		       			<div class="circle discard-circle wheat-color">
							<img src="images/icon-wheat.svg" alt="Wheat">
						</div>
						<div class="text-center">
							<h5 class="discard-hand-number" id="discard-hand-number-wheat"></h5>
				    		<input type="number" class="form-control discard-number" max="0" res="wheat">
				    	</div>
				    </div>
				    <div class="discard-resource-container">
		       			<div class="circle discard-circle sheep-color">
							<img src="images/icon-sheep.svg" alt="Sheep">
						</div>
						<div class="text-center">
							<h5 class="discard-hand-number" id="discard-hand-number-sheep"></h5>
				    		<input type="number" class="form-control discard-number" max="0" res="sheep">
				    	</div>
				    </div>
			    </div>
      		</div>
      		<div class="modal-footer">
        		<button type="button" class="btn btn-primary" id="discard-btn" disabled="disabled">Discard</button>
      		</div>
    	</div>
	</div>
</div>

<div class="modal fade" id="year-of-plenty-modal" tabindex="-1" role="dialog" aria-labelledby="yearOfPlentyLabel">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
      		<div class="modal-header">
        		<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        		<h4 class="modal-title" id="yearOfPlentyLabel">Play Year of Plenty</h4>
      		</div>
      		<div class="modal-body">
       			<p>Choose any two resources to add to your hand. You can choose two of the same resource.</p>
       			<div class="text-center">
       				<div class="yop-resource-container">
		       			<div class="circle yop-circle brick-color">
							<img src="images/icon-brick.svg" alt="Brick">
						</div>
						<div class="text-center">
				    		<input type="number" class="form-control yop-number" min="0" max="2" res="brick">
				    	</div>
				    </div>
				    <div class="yop-resource-container">
		       			<div class="circle yop-circle wood-color">
							<img src="images/icon-wood.svg" alt="Wood">
						</div>
						<div class="text-center">
				    		<input type="number" class="form-control yop-number" min="0" max="2" res="wood">
				    	</div>
				    </div>
				    <div class="yop-resource-container">
		       			<div class="circle yop-circle ore-color">
							<img src="images/icon-ore.svg" alt="Ore">
						</div>
						<div class="text-center">
				    		<input type="number" class="form-control yop-number" min="0" max="2" res="ore">
				    	</div>
				    </div>
				    <div class="yop-resource-container">
		       			<div class="circle yop-circle wheat-color">
							<img src="images/icon-wheat.svg" alt="Wheat">
						</div>
						<div class="text-center">
				    		<input type="number" class="form-control yop-number" min="0" max="2" res="wheat">
				    	</div>
				    </div>
				    <div class="yop-resource-container">
		       			<div class="circle yop-circle sheep-color">
							<img src="images/icon-sheep.svg" alt="Sheep">
						</div>
						<div class="text-center">
				    		<input type="number" class="form-control yop-number" min="0" max="2" res="sheep">
				    	</div>
				    </div>
			    </div>
      		</div>
      		<div class="modal-footer">
        		<button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
        		<button type="button" class="btn btn-primary" id="play-yop-btn" disabled="disabled">Play Year of Plenty</button>
      		</div>
    	</div>
	</div>
</div>

<div class="modal fade" id="monopoly-modal" tabindex="-1" role="dialog" aria-labelledby="myMonopolyLabel">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
      		<div class="modal-header">
        		<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        		<h4 class="modal-title" id="myMonopolyLabel">Play Monopoly</h4>
      		</div>
      		<div class="modal-body">
       			<p>Choose a type of resource. All other players must give you all their resource cards of that type.</p>
       			<div class="text-center">
       				<div class="circle monopoly-circle-container" res="brick">
	      				<div class="circle monopoly-circle brick-color pointer">
							<img src="images/icon-brick.svg" alt="Brick">
						</div>
					</div>
					<div class="circle monopoly-circle-container" res="wood">
	      				<div class="circle monopoly-circle wood-color pointer">
							<img src="images/icon-wood.svg" alt="Wood">
						</div>
					</div>
					<div class="circle monopoly-circle-container" res="ore">
	      				<div class="circle monopoly-circle ore-color pointer">
							<img src="images/icon-ore.svg" alt="Ore">
						</div>
					</div>
					<div class="circle monopoly-circle-container" res="wheat">
	      				<div class="circle monopoly-circle wheat-color pointer">
							<img src="images/icon-wheat.svg" alt="Wheat">
						</div>
					</div>
					<div class="circle monopoly-circle-container" res="sheep">
	      				<div class="circle monopoly-circle sheep-color pointer">
							<img src="images/icon-sheep.svg" alt="Sheep">
						</div>
					</div>
      			</div>
      		</div>
      		<div class="modal-footer">
        		<button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
        		<button type="button" class="btn btn-primary" id="play-monopoly-btn">Play Monopoly</button>
      		</div>
    	</div>
	</div>
</div>

<div class="modal fade" id="take-card-modal" tabindex="-1" role="dialog" aria-labelledby="takeCardLabel" data-backdrop="static">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
      		<div class="modal-header">
        		<h4 class="modal-title" id="takeCardLabel">Steal a Resource</h4>
      		</div>
      		<div class="modal-body">
       			<p>Choose a player to steal a random resource from.</p>
       			<div class="text-center btn-group" id="take-card-players-list" data-toggle="buttons"></div>
      		</div>
      		<div class="modal-footer">
        		<button type="button" class="btn btn-primary" id="take-card-btn" disabled="disabled">Steal Card</button>
      		</div>
    	</div>
	</div>
</div>

<div class="modal fade" id="welcome-modal" tabindex="-1" role="dialog" aria-labelledby="welcomeLabel" data-backdrop="static">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
      		<div class="modal-header">
        		<h4 class="modal-title" id="welcomeLabel">Welcome to Settlers of Catan</h4>
      		</div>
      		<div class="modal-body">
       			<p>The game is about to begin. The turn order is shown below:</p>
       			<ol id="welcome-turn-order-container"></ol>
      		</div>
      		<div class="modal-footer">
        		<button type="button" id="welcome-start-btn" class="btn btn-success" data-dismiss="modal">Ready</button>
      		</div>
    	</div>
	</div>
</div>

<div class="modal fade" id="roll-dice-modal" tabindex="-1" role="dialog" aria-labelledby="rollDiceLabel" data-backdrop="static">
	<div class="modal-dialog modal-sm" role="document">
		<div class="modal-content">
      		<div class="modal-header text-center">
        		<h4 class="modal-title" id="rollDiceLabel">Please Roll the Dice</h4>
      		</div>
      		<div class="modal-footer modal-footer-center">
        		<button type="button" id="roll-dice-btn" class="btn btn-success" data-dismiss="modal">Roll Dice</button>
      		</div>
    	</div>
	</div>
</div>

<div class="modal fade" id="knight-or-dice-modal" tabindex="-1" role="dialog" aria-labelledby="knightOrDiceLabel" data-backdrop="static">
	<div class="modal-dialog modal-sm" role="document">
		<div class="modal-content">
      		<div class="modal-header text-center">
        		<h4 class="modal-title" id="knightOrDiceLabel">Choose How To Start Your Turn</h4>
      		</div>
      		<div class="modal-footer modal-footer-center">
        		<button type="button" id="knight-dice-roll-dice-btn" class="btn btn-success" data-dismiss="modal">Roll Dice</button>
        		<button type="button" id="knight-dice-play-knight-btn" class="btn btn-primary" data-dismiss="modal">Play Knight</button>
      		</div>
    	</div>
	</div>
</div>

<div class="modal fade" id="disconnected-user-modal" tabindex="-1" role="dialog" aria-labelledby="diconnectedUserLabel" data-backdrop="static">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
      		<div class="modal-header">
        		<h4 class="modal-title" id="diconnectedUserLabel">Disconnected User</h4>
      		</div>
      		<div class="modal-body">
       			<p><span id="disconnected-user-name"></span> has disconnected from the game. The game will exit in <span id="disconnected-user-time"></span> seconds unless this user reconnects.</p>
      		</div>
    	</div>
	</div>
</div>

<div class="modal fade" id="review-trade-modal" tabindex="-1" role="dialog" aria-labelledby="reviewTradeLabel" data-backdrop="static">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
      		<div class="modal-header">
        		<h4 class="modal-title" id="reviewTradeLabel">Review Trade Proposal</h4>
      		</div>
      		<div class="modal-body">
      			<div class="row">
      				<div class="col-xs-6">
		      			<h5>Resources to Give:</h5>
		       			<div id="review-to-give-container">
		       				<p class="review-to-give-list-item hidden" res="brick"><strong class="review-trade-number"></strong><span class="inline-trade-icon circle brick-color"><img src="images/icon-brick.svg" alt="Brick"></span></p>
				    		<p class="review-to-give-list-item hidden" res="wood"><strong class="review-trade-number"></strong><span class="inline-trade-icon circle wood-color"><img src="images/icon-wood.svg" alt="Wood"></span></p>
				    		<p class="review-to-give-list-item hidden" res="ore"><strong class="review-trade-number"></strong><span class="inline-trade-icon circle ore-color"><img src="images/icon-ore.svg" alt="Ore"></span></p>
				    		<p class="review-to-give-list-item hidden" res="wheat"><strong class="review-trade-number"></strong><span class="inline-trade-icon circle wheat-color"><img src="images/icon-wheat.svg" alt="Wheat"></span></p>
				    		<p class="review-to-give-list-item hidden" res="sheep"><strong class="review-trade-number"></strong><span class="inline-trade-icon circle sheep-color"><img src="images/icon-sheep.svg" alt="Sheep"></span></p>
		       			</div>
		       		</div>
		       		<div class="col-xs-6">
		       			<h5>Resources to Receive:</h5>
		       			<div id="review-to-get-container">
		       				<p class="review-to-get-list-item hidden" res="brick"><strong class="review-trade-number"></strong><span class="inline-trade-icon circle brick-color"><img src="images/icon-brick.svg" alt="Brick"></span></p>
				    		<p class="review-to-get-list-item hidden" res="wood"><strong class="review-trade-number"></strong><span class="inline-trade-icon circle wood-color"><img src="images/icon-wood.svg" alt="Wood"></span></p>
				    		<p class="review-to-get-list-item hidden" res="ore"><strong class="review-trade-number"></strong><span class="inline-trade-icon circle ore-color"><img src="images/icon-ore.svg" alt="Ore"></span></p>
				    		<p class="review-to-get-list-item hidden" res="wheat"><strong class="review-trade-number"></strong><span class="inline-trade-icon circle wheat-color"><img src="images/icon-wheat.svg" alt="Wheat"></span></p>
				    		<p class="review-to-get-list-item hidden" res="sheep"><strong class="review-trade-number"></strong><span class="inline-trade-icon circle sheep-color"><img src="images/icon-sheep.svg" alt="Sheep"></span></p>
		       			</div>
		       		</div>
		       	</div>
      		</div>
      		<div class="modal-footer">
        		<button type="button" id="review-trade-accept-btn" class="btn btn-success" data-dismiss="modal">Accept Trade</button>
        		<button type="button" id="review-trade-reject-btn" class="btn btn-danger" data-dismiss="modal">Decline Trade</button>
      		</div>
    	</div>
	</div>
</div>

<div class="modal fade" id="trade-responses-modal" tabindex="-1" role="dialog" aria-labelledby="tradeResponsesLabel" data-backdrop="static">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
      		<div class="modal-header">
        		<h4 class="modal-title" id="tradeResponsesLabel">Trade Responses</h4>
      		</div>
      		<div class="modal-body">
      			<div class="row">
      				<div class="col-xs-6">
      				    <h5>Resources to Give:</h5>
		       			<div id="trade-responses-to-give-container">
		       				<p class="trade-responses-to-give-list-item hidden" res="brick"><strong class="trade-responses-trade-number"></strong><span class="inline-trade-icon circle brick-color"><img src="images/icon-brick.svg" alt="Brick"></span></p>
				    		<p class="trade-responses-to-give-list-item hidden" res="wood"><strong class="trade-responses-trade-number"></strong><span class="inline-trade-icon circle wood-color"><img src="images/icon-wood.svg" alt="Wood"></span></p>
				    		<p class="trade-responses-to-give-list-item hidden" res="ore"><strong class="trade-responses-trade-number"></strong><span class="inline-trade-icon circle ore-color"><img src="images/icon-ore.svg" alt="Ore"></span></p>
				    		<p class="trade-responses-to-give-list-item hidden" res="wheat"><strong class="trade-responses-trade-number"></strong><span class="inline-trade-icon circle wheat-color"><img src="images/icon-wheat.svg" alt="Wheat"></span></p>
				    		<p class="trade-responses-to-give-list-item hidden" res="sheep"><strong class="trade-responses-trade-number"></strong><span class="inline-trade-icon circle sheep-color"><img src="images/icon-sheep.svg" alt="Sheep"></span></p>
		       			</div>
		       		</div>
	       			<div class="col-xs-6">
		       			<h5>Resources to Receive:</h5>
		       			<div id="trade-responses-to-get-container">
		       				<p class="trade-responses-to-get-list-item hidden" res="brick"><strong class="trade-responses-trade-number"></strong><span class="inline-trade-icon circle brick-color"><img src="images/icon-brick.svg" alt="Brick"></span></p>
				    		<p class="trade-responses-to-get-list-item hidden" res="wood"><strong class="trade-responses-trade-number"></strong><span class="inline-trade-icon circle wood-color"><img src="images/icon-wood.svg" alt="Wood"></span></p>
				    		<p class="trade-responses-to-get-list-item hidden" res="ore"><strong class="trade-responses-trade-number"></strong><span class="inline-trade-icon circle ore-color"><img src="images/icon-ore.svg" alt="Ore"></span></p>
				    		<p class="trade-responses-to-get-list-item hidden" res="wheat"><strong class="trade-responses-trade-number"></strong><span class="inline-trade-icon circle wheat-color"><img src="images/icon-wheat.svg" alt="Wheat"></span></p>
				    		<p class="trade-responses-to-get-list-item hidden" res="sheep"><strong class="trade-responses-trade-number"></strong><span class="inline-trade-icon circle sheep-color"><img src="images/icon-sheep.svg" alt="Sheep"></span></p>
		       			</div>
		       		</div>
	       		</div>
       			<h5>Player Responses:</h5>
       			<div id="trade-responses-players-container"></div>
      		</div>
      		<div class="modal-footer">
        		<button type="button" id="trade-responses-cancel-trade-btn" class="btn btn-danger">Cancel Trade</button>
      		</div>
    	</div>
	</div>
</div>

<div class="modal fade" id="winner-modal" tabindex="-1" role="dialog" aria-labelledby="winnerLabel" data-backdrop="static">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
      		<div class="modal-header">
        		<h4 class="modal-title" id="winnerLabel"></h4>
      		</div>
      		<div class="modal-body"></div>
      		<div class="modal-footer">
        		<button type="button" id="return-home-btn" class="btn btn-success" data-dismiss="modal">Return Home</button>
      		</div>
    	</div>
	</div>
</div>

</#assign>
<#include "main.ftl">
<script src="js/player.js"></script>
<script src="js/tile.js"></script>
<script src="js/intersection.js"></script>
<script src="js/path.js"></script>
<script src="js/board.js"></script>
<script src="js/websocket.js"></script>
<script src="js/main.js"></script>
