<#assign content>

<div id="board-viewport"></div>

<div class="row above-board">
	<div class="col-xs-1">
		<input type="button" class="btn btn-primary" id="end-turn-btn" value="End Turn">
	</div>
	<div class="col-xs-8"></div>
	<div class="col-xs-3" id="right-menu-container">
		<ul class="nav nav-pills nav-stacked col-xs-3" role="tablist">
	    	<li role="presentation" class="active right-tab"><a href="#players-tab" aria-controls="players-tab" role="tab" data-toggle="tab">Players</a></li>
	    	<li role="presentation" class="right-tab"><a href="#build-tab" aria-controls="build-tab" role="tab" data-toggle="tab">Build</a></li>
	    	<li role="presentation" class="right-tab"><a href="#trade-tab" aria-controls="trade-tab" role="tab" data-toggle="tab">Trade</a></li>
		</ul>
		<div class="tab-content right-tab-content col-xs-9">
		    <div role="tabpanel" class="tab-pane active right-tab-pane" id="players-tab">
		    	 <ul class="nav nav-tabs right-inner-tabs" role="tablist">
				 	<li role="presentation" class="active"><a href="#p1-tab" aria-controls="home" role="tab" data-toggle="tab">P1</a></li>
				    <li role="presentation"><a href="#p2-tab" aria-controls="profile" role="tab" data-toggle="tab">P2</a></li>
				    <li role="presentation"><a href="#p3-tab" aria-controls="messages" role="tab" data-toggle="tab">P3</a></li>
				    <li role="presentation"><a href="#p4-tab" aria-controls="settings" role="tab" data-toggle="tab">P4</a></li>
				 </ul>
				 <div class="tab-content">
				 	<div role="tabpanel" class="tab-pane active" id="p1-tab"></div>
				    <div role="tabpanel" class="tab-pane" id="p2-tab"></div>
				    <div role="tabpanel" class="tab-pane" id="p3-tab"></div>
				    <div role="tabpanel" class="tab-pane" id="p4-tab"></div>
				 </div>
		    </div>
		    <div role="tabpanel" class="tab-pane right-tab-pane" id="build-tab">
		    	<ul class="list-group">
			    	<li class="list-group-item">
			    		<div class="row">
			    			<div class="col-xs-6">
				    			<h4>Settlement</h3>
				    			<p>1 Brick</p>
				    			<p>1 Wood</p>
				    			<p>1 Wheat</p>
				    			<p>1 Sheep</p>
			    			</div>
			    			<div class="col-xs-6">
		    					<input type="button" class="btn btn-primary" id="settlement-build-btn" value="Build">
		    				</div>
		    			</div>
			    	</li>
		    		<li class="list-group-item">
		    			<div class="row">
			    			<div class="col-xs-6">
			    				<h4>City</h3>
			    				<p>3 Ore</p>
			    				<p>2 Wheat</p>
		    				</div>
			    			<div class="col-xs-6">
		    					<input type="button" class="btn btn-primary" id="city-build-btn" value="Build">
		    				</div>
		    			</div>
		    		</li>
		    		<li class="list-group-item">
		    			<div class="row">
			    			<div class="col-xs-6">
				    			<h4>Road</h3>
				    			<p>1 Brick</p>
				    			<p>1 Wood</p>
				    		</div>
					    	<div class="col-xs-6">
				    			<input type="button" class="btn btn-primary" id="road-build-btn" value="Build">
				    		</div>
				    	</div>
		    		</li>
		    		<li class="list-group-item">
		    			<div class="row">
			    			<div class="col-xs-6">
				    			<h4>Development Card</h3>
				    			<p>1 Ore</p>
				    			<p>1 Wheat</p>
				    			<p>1 Sheep</p>
		    				</div>
			    			<div class="col-xs-6">
		    					<input type="button" class="btn btn-primary" id="dev-card-build-btn" value="Build">
		    				</div>
		    			</div>
		    		</li>
		    	</ul>
		    </div>
		    <div role="tabpanel" class="tab-pane right-tab-pane" id="trade-tab">
			    <ul class="nav nav-tabs right-inner-tabs" role="tablist">
					<li role="presentation" class="active"><a href="#player-trade-tab" aria-controls="home" role="tab" data-toggle="tab">Interplayer</a></li>
					<li role="presentation"><a href="#bank-trade-tab" aria-controls="profile" role="tab" data-toggle="tab">Bank</a></li>
				</ul>
		    	<div class="tab-content">
		    		<div role="tabpanel" class="tab-pane active" id="player-trade-tab">
		    			<div class="row trade-row">
		    				<div class="col-xs-5 text-center">
		    					<h5>Brick</h5>
		    				</div>
		    				<div class="col-xs-3 text-center">
		    					<input type="number" class="form-control">
		    				</div>
		    				<div class="col-xs-4 text-left">
		    					<h5>4:1</h5>
		    				</div>
		    			</div>
		    			<div class="row trade-row">
		    				<div class="col-xs-5 text-center">
		    					<h5>Wood</h5>
		    				</div>
		    				<div class="col-xs-3 text-center">
		    					<input type="number" class="form-control">
		    				</div>
		    				<div class="col-xs-4 text-left">
		    					<h5>4:1</h5>
		    				</div>
		    			</div>
		    			<div class="row trade-row">
		    				<div class="col-xs-5 text-center">
		    					<h5>Ore</h5>
		    				</div>
		    				<div class="col-xs-3 text-center">
		    					<input type="number" class="form-control">
		    				</div>
		    				<div class="col-xs-4 text-left">
		    					<h5>4:1</h5>
		    				</div>
		    			</div>
		    			<div class="row trade-row">
		    				<div class="col-xs-5 text-center">
		    					<h5>Wheat</h5>
		    				</div>
		    				<div class="col-xs-3 text-center">
		    					<input type="number" class="form-control">
		    				</div>
		    				<div class="col-xs-4 text-left">
		    					<h5>4:1</h5>
		    				</div>
		    			</div>
		    			<div class="row trade-row">
		    				<div class="col-xs-5 text-center">
		    					<h5>Sheep</h5>
		    				</div>
		    				<div class="col-xs-3 text-center">
		    					<input type="number" class="form-control">
		    				</div>
		    				<div class="col-xs-4 text-left">
		    					<h5>4:1</h5>
		    				</div>
		    			</div>
		    			<div class="row trade-row">
		    				<div class="col-xs-5 text-center">
		    					<h5>Anything</h5>
		    				</div>
		    				<div class="col-xs-3 text-center">
		    					<input type="number" class="form-control">
		    				</div>
		    				<div class="col-xs-4 text-left">
		    					<h5>4:1</h5>
		    				</div>
		    			</div>
		    			<div class="row trade-row">
		    				<div class="col-xs-12 text-center">
		    					<input type="button" class="btn btn-primary" value="Propose Trade">
		    				</div>
		    			</div>
		    		</div>
				    <div role="tabpanel" class="tab-pane" id="bank-trade-tab"></div>
		    	</div>
		    </div>
	    </div>
  	</div>
</div>

<div class="navbar navbar-fixed-bottom above-board">
	<div class="col-xs-3"></div>
	<div class="col-xs-6 panel panel-default text-center">
		<ul class="nav navbar-nav navbar-left">
			<li class="navbar-btn">
				<div class="circle card-circle"></div>
			</li>
			<li class="navbar-btn">
				<div class="circle card-circle"></div>
			</li>
			<li class="navbar-btn">
				<div class="circle card-circle"></div>
			</li>
			<li class="navbar-btn">
				<div class="circle card-circle"></div>
			</li>
			<li class="navbar-btn">
				<div class="circle card-circle"></div>
			</li>
		</ul>
		<ul class="nav navbar-nav navbar-right">
			<li class="navbar-btn">
				<div class="circle card-circle"></div>
			</li>
			<li class="navbar-btn">
				<div class="circle card-circle"></div>
			</li>
			<li class="navbar-btn">
				<div class="circle card-circle"></div>
			</li>
			<li class="navbar-btn">
				<div class="circle card-circle"></div>
			</li>
			<li class="navbar-btn">
				<div class="circle card-circle"></div>
			</li>
		</ul>
	</div>
	<div class="col-xs-3"></div>
</div>

</#assign>
<#include "main.ftl">
