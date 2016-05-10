<#assign content>
<#assign open = openGroups?eval>
<#assign closed = closedGroups?eval>

<div class="container-fluid">
	<div class="text-center">
		<h2>Catan Statistics</h2>
		<#if open.atLimit>
		<h4>Game limit reached</h4>
		<#else>
		<h4>Not at game limit</h4>
		</#if>
	</div>
	<div class="row">
		<div class="col-xs-6">
			<h3>Open Games: ${open.groups?size}</h3>
			<ul class="list-group">
			<#list open.groups as group>
				<li class="list-group-item">
					<p><strong>Name: </strong> ${group.group.groupName}</p>
					<p><strong>ID: </strong>${group.group.id}</p>
					<p><strong>Number of Users: </strong>${group.group.currentSize}/${group.group.maxSize}</p>
					<p><strong>Users: </strong><#list group.group.connectedUsers as user>${user.userName}, </#list></p>
				</li>
			</#list>
			</ul>
		</div>
		<div class="col-xs-6">
			<h3>Games In Play: ${closed.closedGroups?size}</h3>
			<ul class="list-group">
			<#list closed.closedGroups as group>
				<li class="list-group-item">
					<p><strong>Name: </strong> ${group.group.groupName}</p>
					<p><strong>ID: </strong>${group.group.id}</p>
					<p><strong>Number of Users: </strong>${group.group.currentSize}/${group.group.maxSize}</p>
					<p><strong>Users: </strong><#list group.group.connectedUsers as user>${user.userName}, </#list></p>
				</li>
			</#list>
			</ul>
		</div>
	</div>
</div>

</#assign>
<#include "main.ftl">


