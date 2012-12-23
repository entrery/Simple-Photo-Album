<%@page import="entrery.photoalbum.resource.load.ImageUtil"%>
<%@page import="entrery.photoalbum.resource.load.ImageDAO"
        import="java.util.List"
%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Photo album</title>
<script type="text/javascript" src="resources/jquery/jquery-1.7.2.js"></script>
<script type="text/javascript">


function removeArtifact(imageId) {	
	var currentCategoryFullPath = document.getElementsByName('categoryPath')[0].value;
	
	$.ajax({
		type: "POST",
		url: "CommandExecutorServlet",
		data: "categoryPath=" + currentCategoryFullPath + "&" + "artifactId=" + imageId + "&action=DELETE&forward=TRUE",
		success: function(result){console.log("Success: deleted " + result); }
		});
}



function allowDrop(event) {
	event.preventDefault();
}

function drag(event) {
	event.dataTransfer.setData("Text", event.target.id);
}

function drop(event) {
	event.preventDefault();
	var imageId = event.dataTransfer.getData("Text");
	var element = document.getElementById(imageId);
	element.parentNode.removeChild(element);
	
	removeArtifact(imageId);
}

function createLinkChain() {
	currentCategoryFullPath = document.getElementsByName('categoryPath')[0].value;
	var parentCategories = currentCategoryFullPath.split('@');
	
	var createNewLinkElement = function(linkName, linkSuffix) {
		var link = document.createElement('a');
		link.title = linkName;
		link.innerHTML = link.title;
		link.href = 'PhotoAlbum.jsp?categoryPath=' + linkSuffix;
		
		return link;
	};
	
	var currentCategoryChain = parentCategories[0];
	var linkChainSpan = document.getElementById('categoryLinkChain');
	linkChainSpan.appendChild(createNewLinkElement(currentCategoryChain, currentCategoryChain));

	for(var i = 1; i < parentCategories.length; i++) {
		currentCategoryChain += '@' + parentCategories[i];
		linkChainSpan.appendChild(document.createTextNode("-->"));
		linkChainSpan.appendChild(createNewLinkElement(parentCategories[i], currentCategoryChain));
	}
}

window.onload = createLinkChain;

</script>


</head>
<body>
	<%  
		String categoryPath = request.getParameter("categoryPath");
	
		if(categoryPath == null) {
			categoryPath = "My Pictures"; 
		}
	
		String requestedCategory = ImageUtil.getLastCategory(categoryPath);
		
		ImageDAO imageDAO = new ImageDAO();
		List<String> images = imageDAO.getAllImageNamesForUserCategory("Entrery", requestedCategory);
		List<String> nestedCategories = imageDAO.getAllNestedCategories("Entrery", requestedCategory);
		
	%>
		
	<span id="categoryLinkChain"></span>
			 
	<table>
		 <tr>
	<% 
		for (String folder : nestedCategories) {
			String childCategoryPath = categoryPath + "@" + folder;
			%>
			<td>
				<a href="PhotoAlbum.jsp?categoryPath=<%=childCategoryPath %>">
					<img id="<%=folder%>" draggable="true" ondragstart="drag(event)" src="CommandExecutorServlet/<%=childCategoryPath + ".jpg" + "?action=LOAD&forward=FALSE"%>">
				</a>
			</td>
			<%
		}
				
		for (String image : images) {
			String imagePath = categoryPath + "@" + image;
			%>
			<td>
				<img id="<%=image%>" draggable="true" ondragstart="drag(event)" onclick="selectPicture(this);"src="CommandExecutorServlet/<%=imagePath + "?action=LOAD&forward=FALSE"%>">  
			</td>
			<%
		}
	%>
		</tr> 
	</table>		
		
	 <form action="CommandExecutorServlet" method="post" >
		<input type="text" name="categoryName"/>
		<input type="hidden" name="action" value="CREATE"/>
    	<input type="hidden" name="forward" value="TRUE"/>
    	<input type="hidden" name = "categoryPath" value="<%=categoryPath%>" />
    	<input type="submit" value="Create Category"/>
	</form>
		
	<form action="CommandExecutorServlet?action=UPLOAD&forward=TRUE&categoryPath=<%=categoryPath%>" method="post" enctype="multipart/form-data">
    	<input type="file" name="photo" />
    	<input type="hidden" name = "categoryPath" value="<%=categoryPath%>" />
    	<input type="submit" value="Add Photo"/>
	</form>
	
	<img src="resources/images/recycle_bin.png" ondrop="drop(event)" ondragover="allowDrop(event)"> 
	 	 
</body>
</html>