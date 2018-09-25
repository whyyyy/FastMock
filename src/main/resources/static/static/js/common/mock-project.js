define(['jquery','httprequest','bootstrap-dialog','mock-uri'],
function($,httprequest,BootstrapDialog,mockUri){
	MockProject ={}
	
	MockProject.init = function(){ 
		$("#d-project").html("<ul id='projectUl' class='nav nav-tabs' role='tablist'></ul>");
		
		var editLi ="<li role=\"presentation\"><a title='Edit' id=\"editCurrentProject\", class=\"glyphicon glyphicon-cog\"></a></li>";
		var addLi ="<li role=\"presentation\"><a title='New' id=\"CreateProject\" class=\"glyphicon glyphicon-plus\"></a></li>";
		var delLi ="<li role=\"presentation\"><a title='Delete'id=\"delCurrentProject\"  class=\"glyphicon glyphicon-minus\"></a></li>";

		$("#projectUl").append(addLi);
		$("#projectUl").append(delLi);
		$("#projectUl").append(editLi);
		
		$("#editCurrentProject").on("click",function(){
			var proId=$(this).attr('currentProId');
			var proName=$(this).attr('currentProName');
			if(proId != undefined){
				MockProject.editProejctDialog(proId,proName);
			} else {
				BootstrapDialog.alert("Please choose the project first")
			}
		});
		
		$("#CreateProject").on("click",function(){
			MockProject.createProejctDialog();
		});
		
		$("#delCurrentProject").on("click",function(){
			var proId=$(this).attr('currentProId');
			if(proId != undefined){
				MockProject.deleteProejct(proId)
			} else {
				BootstrapDialog.alert("Please choose the project first")
			}
		});
		
		httprequest.send({
			type : "GET",
			url : "/mock/project",
			dataType:"json",
			success : function(obj,status) {
				if (obj.code == 0 && obj.data) {
					for (var i = 0; i < obj.data.length; i++) {
						var li = "<li role='presentation' id='lipro"+obj.data[i].id+"'>";
						li += "<a id='pro"+obj.data[i].id+"'  proId ='"+obj.data[i].id+"' proName ='"+obj.data[i].mockProjectName+"' href='javascript:void(0)' >"+obj.data[i].mockProjectName+"</a>";
						li += "</li>";
						$("#projectUl").append(li);
						
						
						$("#pro"+obj.data[i].id).on("click",function(){
							
							var proId=$(this).attr('proId');

							$("[role='presentation'][class='active']").attr("class","");
							$("#lipro"+proId).attr("class","active");
							
							var mockProjectName=$(this).attr('proName');
							$("#editCurrentProject").attr("currentProId",proId);
							$("#editCurrentProject").attr("currentProName",mockProjectName);
							$("#delCurrentProject").attr("currentProId",proId);
							mockUri.showUrlByProjectId(proId);
						});
					}
					
				}
			}
		});
	}
	
	
	MockProject.createProejctDialog = function(){
				
		 BootstrapDialog.show({
	            title:"New project",
	            message: tableDemo("",""),
	            closable: true,
	            closeByBackdrop: false,
	            closeByKeyboard: false,
	            buttons: [{
	                label: 'Save',
	                cssClass: 'btn-primary',
	                action: function(dialog){
	            		saveProject("insert",dialog)
	                }
	            }, {
	                label: 'Cancel',
	                action: function(dialog){
	            	dialog.close();
	                }
	            }],
	            onshown : function(){}
	        });
	}
	
	MockProject.editProejctDialog = function(proId,proName){
		
		 BootstrapDialog.show({
	            title:"Edit",
	            message: tableDemo(proId,proName),
	            closable: true,
	            closeByBackdrop: false,
	            closeByKeyboard: false,
	            buttons: [{
	                label: 'Save',
	                cssClass: 'btn-primary',
	                action: function(dialog){
	            		saveProject("update",dialog)
	                }
	            }, {
	                label: 'Cancel',
	                action: function(dialog){
	            	dialog.close();
	                }
	            }]
	        });
	}	
	
	
	MockProject.deleteProejct = function(proId){
		var a=confirm("Sure to delete?");
		 if(a==false){
			 return;
		 }
		httprequest.send({
			type : "DELETE",
			url : "/mock/project",		
			contentType:'json',
			dataType:'json',
			data : {id:proId},
			success : function(obj,status) {
				if (obj.code == 0) {
					MockProject.init();
					BootstrapDialog.alert("Success");

				} else {
					BootstrapDialog.alert(obj.msg);
				}
			}
		});	
	}
	
	function saveProject(SaveOrUpdate,dialog) {
		var type = SaveOrUpdate == "insert" ? "POST" : "PUT";
		var project = {};
		
		if (type == "PUT") {
			project.id = $('#pro_id').val();
		}

		project.mockProjectName = $('#pro_Name').val();
		httprequest.send({
			type : type,
			url : "/mock/project",
			contentType:'json',
			dataType:'json',
			data : project,
			success : function(obj,statud) {
				dialog.close(); 
				if (obj.code == 0) {
					MockProject.init();
					BootstrapDialog.alert("Success");

				}else {
					BootstrapDialog.alert(obj.msg)
				}
			}
		});
	}
	
	
	function tableDemo(proId,proName){
		var proTable = "";
		proTable += "<table class='table'>";
		proTable += "<tr>";
		proTable += "<td>Name:</td >";
		proTable += "<td >";
		proTable += "<input type='hidden' id='pro_id' value='" + proId
				+ "'  >"
		proTable += "<input type='text' id='pro_Name' value='"
				+ proName + "'  >";
		proTable += "</td>";
		proTable += "</tr>";
		proTable += "</table>";
		return proTable
	}
	
	
	return MockProject
	
	
});