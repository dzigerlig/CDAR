<%@page import="cdar.bll.entity.producer.TreeFull"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>



<jsp:useBean id="tree" class="cdar.bll.reporting.TreeBean" scope="request" />
<% tree.setTreeId(Integer.parseInt(request.getParameter("treeid"))); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
	<title>Producer Tree Export: <%=tree.getTreeTitle()%></title>
	<link href="../cdarclient/vendor/css/bootstrap.css" rel="stylesheet">
	<link rel="icon" href="../cdarclient/app/img/favicon.ico" type="image/x-icon">
</head>

<body>
	<div class="container">

		<div class="page-header">
			<h1>Producer Tree Report: <%=tree.getTreeTitle()%></h1>
			<p class="lead">Generated on: <%= tree.getCreationTime().toGMTString() %></p>
		</div>

		<h2>Properties</h2>
		<div class="row">
			<div class="col-md-2">
				<strong>Node Description:</strong><br />
				<strong>Subnode Description:</strong>
			</div>
			<div class="col-md-10">
				 <%= tree.getCdarDescriptions().getNodeDescription() %><br />
				 <%= tree.getCdarDescriptions().getSubnodeDescription() %>
			</div>
		</div>
		<hr>
		
		<c:forEach items="${tree.getNodes()}" var="node">
			<a name="${node.getWikititle()}"></a>
			<h2><%= tree.getCdarDescriptions().getNodeDescription() %>: ${node.getTitle()}</h2>
			<a href="<%=tree.getCdarDescriptions().getWikiUrl()%>${node.getWikititle()}" target="_blank"><%=tree.getCdarDescriptions().getWikiUrl()%>${node.getWikititle()}</a>
			<table class="table table-bordered table-striped table-condensed table-hover" style="margin-top: 20px">
				<thead>
					<tr>
						<th class="col-md-3">Description</th>
						<th class="col-md-9">Content</th>
					</tr>
				</thead>
					<tr>
						<td><strong>forthcomes</strong></td>
						<td>
							<ul>
								<c:forEach items="${tree.getNodeLinks(true, node.getId())}" var="nodeLink">
									<li><a href="#${tree.getNode(nodeLink.getSourceId()).getWikititle()}">${tree.getNode(nodeLink.getSourceId()).getTitle()}</a> <c:if test="${nodeLink.getSubnodeId()!=0}">(from ${tree.getCdarDescriptions().getSubnodeDescription()}: <a href="#${tree.getSubnode(nodeLink.getSubnodeId()).getWikititle()}">${tree.getSubnode(nodeLink.getSubnodeId()).getTitle()}</a>)</c:if></li>
								</c:forEach>
								<c:if test="${tree.getNodeLinks(true, node.getId()).size()==0}">
									<li>no forthcoming ${tree.getCdarDescriptions().getNodeDescription()}s</li>
								</c:if>
							</ul>
						</td>
					</tr>
					<tr>
						<td><strong>leads to</strong></td>
						<td>
							<ul>
								<c:forEach items="${tree.getNodeLinks(false, node.getId())}" var="nodeLink">
									<li><a href="#${tree.getNode(nodeLink.getTargetId()).getWikititle()}">${tree.getNode(nodeLink.getTargetId()).getTitle()}</a> <c:if test="${nodeLink.getSubnodeId()!=0}">(by ${tree.getCdarDescriptions().getSubnodeDescription()}: <a href="#${tree.getSubnode(nodeLink.getSubnodeId()).getWikititle()}">${tree.getSubnode(nodeLink.getSubnodeId()).getTitle()}</a>)</c:if>
								</c:forEach>
								<c:if test="${tree.getNodeLinks(false, node.getId()).size()==0}">
									<li>no upcoming ${tree.getCdarDescriptions().getNodeDescription()}s</li>
								</c:if>
							</ul>
						</td>
					</tr>
					<tr>
						<td><strong>Wiki Entry</strong>
						<td>
							${ tree.getNodeWikiEntryHtml(node.getId()) }
						</td>
					</tr>
				<tbody>
				
				</tbody>
			</table>
			
			
			<c:if test="${tree.getSubnodes(node.getId()).size()!=0}">
				<h3>${tree.getCdarDescriptions().getSubnodeDescription()}s:</h3>
				
				
				<table class="table table-bordered table-striped table-condensed table-hover">
					<thead>
						<tr>
							<th class="col-md-5">Title/Link</th>
							<th class="col-md-7">Content</th>
						</tr>
					</thead>
					<tbody>
					<c:forEach items="${tree.getSubnodes(node.getId())}" var="subnode">
						<tr>
						<td>
							<a name="${subnode.getWikititle()}"></a>
							<strong>${subnode.getTitle()}</strong><br />
							<a href="<%=tree.getCdarDescriptions().getWikiUrl()%>${subnode.getWikititle()}" target="_blank"><%=tree.getCdarDescriptions().getWikiUrl()%>${subnode.getWikititle()}</a>
						</td>
						<td>
							${tree.getSubnodeWikiEntryHtml(subnode.getId()) }
						</td>
					</c:forEach>
				</tbody>
				</table>
			</c:if>
			<hr>
		</c:forEach>
	</div>
</body>

</html>