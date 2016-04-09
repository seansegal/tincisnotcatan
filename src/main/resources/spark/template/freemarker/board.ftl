<#assign content>

<div id="board-viewport"></div>

<div class="row above-board">
	<div class="col-xs-9"></div>
	<div class="col-xs-3">
		<ul class="nav nav-pills nav-stacked col-xs-3" role="tablist">
	    	<li role="presentation" class="active right-tab"><a href="#build-tab" aria-controls="build-tab" role="tab" data-toggle="tab">Build</a></li>
	    	<li role="presentation" class="right-tab"><a href="#trade-tab" aria-controls="trade-tab" role="tab" data-toggle="tab">Trade</a></li>
	    	<li role="presentation" class="right-tab"><a href="#stats-tab" aria-controls="stats-tab" role="tab" data-toggle="tab">Stats</a></li>
		</ul>
		<div class="tab-content right-tab-content col-xs-9">
		    <div role="tabpanel" class="tab-pane active right-tab-pane" id="build-tab">The Build Tab</div>
		    <div role="tabpanel" class="tab-pane right-tab-pane" id="trade-tab">The Trade Tab</div>
		    <div role="tabpanel" class="tab-pane right-tab-pane" id="stats-tab">The Stats Tab</div>
	    </div>
  	</div>
</div>

<div class="navbar navbar-fixed-bottom above-board">
	<div class="col-xs-3"></div>
	<div class="col-xs-6 panel panel-default text-center">
		<ul class="nav navbar-nav navbar-left">
			<li class="navbar-btn">
				<div class="circle"></div>
			</li>
			<li class="navbar-btn">
				<div class="circle"></div>
			</li>
			<li class="navbar-btn">
				<div class="circle"></div>
			</li>
			<li class="navbar-btn">
				<div class="circle"></div>
			</li>
			<li class="navbar-btn">
				<div class="circle"></div>
			</li>
		</ul>
		<ul class="nav navbar-nav navbar-right">
			<li class="navbar-btn">
				<div class="circle"></div>
			</li>
			<li class="navbar-btn">
				<div class="circle"></div>
			</li>
			<li class="navbar-btn">
				<div class="circle"></div>
			</li>
			<li class="navbar-btn">
				<div class="circle"></div>
			</li>
			<li class="navbar-btn">
				<div class="circle"></div>
			</li>
		</ul>
	</div>
	<div class="col-xs-3"></div>
</div>

</#assign>
<#include "main.ftl">
