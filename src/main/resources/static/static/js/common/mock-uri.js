define(['jquery','httprequest','bootstrap-dialog','mock-request'],
function($,httprequest,BootstrapDialog,mockRequest){
	MockUri ={
			currentUri:{},
			
	};
	
	MockUri.showUrlByProjectId = function(projectId){
		MockUri.projectId = projectId;
		
		var treehigh = $(window).height() -190;
		
		var div_content ="";
		div_content +="<div class='col-md-3' id='content-left'>";
		div_content +="	<div id ='d-mul' class='alert alert-warning' role='alert'></div>";
		div_content +="	<div id ='tree' class='fix-scoll-high'></div>";
		div_content +="</div>";
		div_content +="<div id='content-right'  class='col-md-9'></div>";
		$("#content").html(div_content)
		
		$('#tree').css('height',treehigh+'px');
		
		var tree = [];
		$('#tree').treeview({data:tree});
		httprequest.send({
			type : "GET",
			url : "/mock/uri/project/" + projectId,
			dataType:"json",
			success : function(obj,status) {
				if (obj.code == 0 && obj.data) {
					for (var i = 0; i < obj.data.length; i++) {
						tree.push({
							mockUriId		:	obj.data[i].id,
							text			:	obj.data[i].mockUriName,
							mockUri			:	obj.data[i].mockUri,
							mockMethod		:	obj.data[i].mockMethod,
							mockIsRun		:	obj.data[i].isRun,
						});
					}
					$('#tree').treeview({
						data: tree,
						onNodeSelected:function(event,node){
							if(event.type === "nodeSelected"){
								$("#btnEditMul").attr("currentMockUriId",node.mockUriId)
								
								MockUri.currentUri = node;
								mockRequest.showMockRequestByUri(projectId,node.mockUriId,node.text,node.mockUri,node.mockMethod,node.mockIsRun);
							}
						}
					});
				}
			}
		});
			
			var mops ="";
			mops +="<div class='row'>";
			mops +="	<div class='col-md-3'><a title='Add' id='a-createmul' href='javascript:void(0)' class='btn btn-info'><span class='glyphicon glyphicon-plus'>MURL</span></a></div>";
			mops +="</div>";
			
			$('#d-mul').html(mops );
		
			$("#a-createmul").on('click',function(){
				MockUri.createUri(projectId);
			});
	}
	
	MockUri.createUri=function(projectId){
		 BootstrapDialog.show({
	            title:"New uri",
	            message: uriDemo("",projectId,"","","",""),
	            closable: true,
	            closeByBackdrop: false,
	            closeByKeyboard: false,
	            buttons: [{
	                label: 'Save',
	                cssClass: 'btn-primary',
	                action: function(dialog){
	            		saveUri("insert",dialog);
	                }
	            }, {
	                label: 'Cancel',
	                action: function(dialog){
	            		dialog.close();
	                }
	            }],
	            
	        });
	}
	
	
	MockUri.editUri=function(projectId){
		uri = MockUri.currentUri;
				 BootstrapDialog.show({
			            title:"Edit",
			            message: uriDemo(uri.mockUriId,projectId,uri.text,uri.mockMethod,uri.mockIsRun,uri.mockUri),
			            closable: true,
			            closeByBackdrop: false,
			            closeByKeyboard: false,
			            buttons: [{
			                label: 'Save',
			                cssClass: 'btn-primary',
			                action: function(dialog){
			            		saveUri("update",dialog)
			                }
			            }, {
			                label: 'Cancel',
			                action: function(dialog){
			            	dialog.close();
			                }
			            }]
			        });
	}
	
	
	MockUri.deleteUri=function(uriId){
		var a=confirm("Sure to delete?");
		 if(a==false){
			 return;
		 }
		var URL = "/mock/uri";
		httprequest.send({
			type : "DELETE",
			url : URL,	
			contentType:'json',
			dataType:'json',
			data : {id:uriId},
			success : function(obj,status) {
				if (obj.code == 0) {
					MockUri.showUrlByProjectId(MockUri.projectId);
					BootstrapDialog.alert("Delete success!");

				} else {
					BootstrapDialog.alert(obj.msg)
				}
			}
		});	
	}
	
	function saveUri(SaveOrUpdate,dialog) {
		var type = SaveOrUpdate == "insert" ? "POST" : "PUT";
		var uri = {};
		if (type == "PUT") {
			uri.id = $('#tUriId').val();
		}
		uri.mockProjectId = $('#tUriProjectId').val();
		uri.mockUriName = $('#tUriName').val();
		uri.mockUri = $('#tUriPath').val();
		uri.mockMethod = $('#tUriMethod').val();
		uri.isRun = $('#tIsRun').val();
		
		dialog.close();
		httprequest.send({
			type : type,
			url : "/mock/uri",
			contentType:'json',
			dataType:'json',
			data : uri,		
			success : function(obj,status) {
				if (obj.code == 0) {
					MockUri.showUrlByProjectId(uri.mockProjectId);
					BootstrapDialog.alert("Success");
				} else {
					BootstrapDialog.alert("Failed");
				}
			}
		});
	}
	
	function uriDemo(uriId,uriPorjectId,uriName,uriMethod,uriStatus,uriPath){
		var runCss = uriStatus === "RUN" ? "selected" :"";
		var stopCss = uriStatus === "STOP" ? "selected" :"";
		var table = "";
		table += "<table class='table'>";
		table += "<tr>";
		table += "<td > Uri name：</td>";
		table += "<td colspan='3'> ";
		table += "<input type='hidden' id='tUriId' value='"+uriId+"'  >";
		table += "<input type='hidden' id='tUriProjectId' value='"+uriPorjectId+"'  >";
		table += "<input type='text' id='tUriName' value='"+uriName+"'  >";
		table += "</td>";
		table += "</tr>";
		table += "<tr>";
		table += "<td > Method： </td >";
		table += "<td >";
		table += "<input type='text' id='tUriMethod' value='"+uriMethod+"' >";
		table += "</td>";
		table += "<td > Status： </td >";
		table += "<td>";
		table += "<select id='tIsRun' class='wt100'>";	
		table += "<option id='IsRunRUN' value='RUN' "+runCss+">Run</option>";
		table += "<option id='IsRunSTOP' value='STOP' "+stopCss+">Stop</option>";
		table += "</select>";
		table += "</td></tr>";
		table += "<tr>";
		table += "<td > URI：</td>";
		table += "<td colspan='3'>";
		table += "<input type='text' id='tUriPath' value='"+uriPath+"' ></td>";
		table += "</tr>";
		table += "</table>";
		
		return table
	}

	
	return MockUri;
	
});