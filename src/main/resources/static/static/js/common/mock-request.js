define(['jquery','require','httprequest','bootstrap-dialog','js-beautify','tool'],
function($,require,httprequest,BootstrapDialog,jsbeautify,tool){
	
	var MockRequest = {
			uri			:	{},
			data		:	[],
		
	};
	MockRequest.showMockRequestByUri = function (projectId,uriId,uriName,uriUrl,uriMethod,uriStatus){
		
		MockRequest.uri.projectId =projectId;
		MockRequest.uri.uriId =uriId;
		MockRequest.uri.uriName =uriName;
		MockRequest.uri.uriUrl =uriUrl;
		MockRequest.uri.uriMethod =uriMethod;
		MockRequest.uri.uriStatus =uriStatus;
		
		var div_content ="";
		div_content +="		<div class='row'>";
		div_content +="			<div class='alert alert-success col-md-12' role='alert'>";
		div_content +="				<div class='col-md-4' ><span class='glyphicon glyphicon-record'></span><span class='glyphicon glyphicon-chevron-right'></span><span id ='uriName'  >uriName</span></div>";
		div_content +="				<div class='col-md-3' ><span id ='uriUrl' >uriUrl</span></div>";
		div_content +="				<div class='col-md-1' ><strong><span id ='uriMethod' >uriMethod</span></strong></div>";
		div_content +="				<div class='col-md-1' ><input id='uriswitch' type='checkbox' projectid='"+projectId+"' uriid='"+uriId+"' uriname='"+uriName+"' class='switch' data-on-text='On' data-off-text='Off' data-on-color='success' data-size='mini' checked /></div>";
		div_content +="				<div class='col-md-3' >";
		div_content +="					<a title ='Add strategy' id='createRequest' class='btn btn-primary'><span class='glyphicon glyphicon-plus'></span></a>";
		div_content +="					<a title ='Delete'id='a-delete"+uriId+"' reid ='"+uriId+"' class='btn btn-primary' ><span class='glyphicon glyphicon-minus'></span></a>";
		div_content +="					<a title ='Edit' id='a-edit"+uriId+"' reid ='"+uriId+"'class='btn btn-primary' ><span class='glyphicon glyphicon-cog'></span></a>";
		div_content +="				</div>";
		div_content +="			</div>";
		div_content +="		</div>";
		div_content +="		<div class='row' ><ul id='requestsUl' class='list-group' ></ul></div>";
		
		$("#content-right").html(div_content);
		
		$('#uriswitch').bootstrapSwitch('state','RUN'==uriStatus?true:false);
		$('.bootstrap-switch-id-uriswitch').attr('title','status');
		$('#uriswitch').bind('switchChange.bootstrapSwitch',function(event){
			var state = $("#uriswitch").is(":checked");
			$('#uriswitch').bootstrapSwitch('state', !state, true);
			updateStatus($(this),'uri',$(this).attr('uriid'),state);
		});
			
		$("#uriName").html(uriName );
		$("#uriUrl").html(uriUrl );
		$("#uriMethod").html(uriMethod );
		$("#uriStatus").html(uriStatus);
		
		$("#a-edit"+uriId).on("click",function(){
			require('mock-uri').editUri(MockRequest.uri.projectId);
			});
		
		$("#a-delete"+uriId).on("click",function(){
			var id = $(this).attr("reid");
			require('mock-uri').deleteUri(id);
		});
		
		$("#createRequest").on("click",function(){MockRequest.createRequest();});
		
		httprequest.send({
			type : "GET",
			url : "/mock/request/uri/" + uriId,
			dataType:"json",
			success : function(obj,status) {
				$("#requestsUl").html(""); 
				var entity = obj;
				if (entity.code == 0 && entity.data) {
					MockRequest.data = entity.data;
					for (var i = 0; i < entity.data.length; i++) {
						var request = entity.data[i];
						var li = "<li class='list-group-item'>";
						li += "	<div class='row'>";
						li += "		<div class='col-md-4'>";
						li += "			<span class='glyphicon glyphicon-share-alt'>"+request.mockRequestName+"</span>";
						li += "		</div>";
						li += "		<div class='col-md-2'>";
						li += "			<input id='reqswitch"+i+"' type='checkbox' reqid='"+request.id+"' projectid='"+projectId+"' uriname='"+uriName+"' class='switch' data-on-text='On' data-off-text='Off' data-on-color='success' data-size='mini' checked />";
						li += "		</div>";
						li += "		<div class='col-md-4'>";
						li += "			<p>"+request.updateTime+"</p>";
						li += "		</div>";
						li += "		<div class='col-md-2'>";
						li += "			<div class='row'>";
						li += "				<a title='Edit' id='a-edit"+request.id+"' reid ='"+request.id+"'class='btn btn-primary' ><span class='glyphicon glyphicon-cog'></span></a>";
						li += "				<a title='Delete' id='a-delete"+request.id+"' reid ='"+request.id+"' class='btn btn-primary' ><span class='glyphicon glyphicon-minus'></span></a>";
						li += "			</div>";
						li += "		</div>";
						li += "	</div>";
						li += "</li>";
						$("#requestsUl").append(li);
						
						$('#reqswitch'+i).bootstrapSwitch('state','RUN'==request.isRun?true:false);
						$('.bootstrap-switch-id-reqswitch'+i).attr('title','status');
						$('#reqswitch'+i).bind('switchChange.bootstrapSwitch',function(event){
							var state = $(this).is(":checked");
							$(this).bootstrapSwitch('state', !state, true);
							updateStatus($(this),'req',$(this).attr('reqid'),state);
						});
		
						$("#a-cbl"+request.id).on('click',function(){
							var reid = $(this).attr('reid')
							if($("#d-cbl"+reid).css("display") === "block"){
								$("#d-cbl"+reid).hide();
								$("#Ul-cb"+reid).html("");
							}
						});
						
						$('#a-edit'+request.id).on("click",function(){
							var id = $(this).attr("reid");
							var redata = getRequest(id);
							if(redata.id != undefined){
								MockRequest.editRequest(redata);
							}
						});
						
						$('#a-delete'+request.id).on("click",function(){
							var id = $(this).attr("reid");
							MockRequest.deleteRequest(id);
						});
					}
				}
			}
		});
	};
	
	
	MockRequest.createRequest = function(){
		 var jsExample = "function exec(obj){return 'xxx';}";
		 var respExample = "function exec(obj){var obj=JSON.parse(obj);var resData={status:200,type:'json',content:{obj:obj}};return JSON.stringify(resData);}"
		 BootstrapDialog.show({
	            size: BootstrapDialog.SIZE_WIDE,
	            title:"New strategy",
	            message: requestDemo("",MockRequest.uri.uriId,"","0","","","0","0",jsExample,""),
	            closable: true,
	            closeByBackdrop: false,
	            closeByKeyboard: false,
	            buttons: [{
	                label: 'Save',
	                cssClass: 'btn-primary',
	                action: function(dialog){
		            	saveRequest("insert",dialog)
	            		
	                }
	            }, {
	                label: 'Cancel',
	                action: function(dialog){
	            		dialog.close();
	                }
	            }],
	            onshown : function(){
                	$("#verifyExpect").val(jsbeautify.js_beautify(jsExample));
                	$("#responseExpect").val(jsbeautify.js_beautify(respExample));
                }
	        });
	}
	
	
	MockRequest.editRequest = function(data){
		 BootstrapDialog.show({
	            size: BootstrapDialog.SIZE_WIDE,
	            title:"Edit strategy",
	            message: requestDemo(data.id,data.mockUriId,data.mockRequestName,data.orderNum,data.type,data.isRun,data.requestWait,data.verifyExpect,data.responseExpect),
	            closable: true,
	            closeByBackdrop: false,
	            closeByKeyboard: false,
	            buttons: [{
	                label: 'Save',
	                cssClass: 'btn-primary',
	                action: function(dialog){
		            	saveRequest("update",dialog)
	            		
	                }
	            }, {
	                label: 'Cancel',
	                action: function(dialog){
	            		dialog.close();
	                }
	            }],
	            onshown : function(){
					if(data.verifyExpect){
						$("#verifyExpect").val(jsbeautify.js_beautify(data.verifyExpect));
					}
					if(data.responseExpect){
						$("#responseExpect").val(jsbeautify.js_beautify(data.responseExpect));
					}
		 		}
	        });
	}
	
	MockRequest.deleteRequest = function(id){
		var a=confirm("Sure to delete?");
		 if(a==false){
			 return;
		 }
		httprequest.send({
			type : "DELETE",
			url : "/mock/request",		
			contentType:'json',
			dataType:'json',
			data : {id:id},
			success : function(obj,status) {
				if (obj.code == 0) {
					var uri = MockRequest.uri;
					BootstrapDialog.alert("Success");
					MockRequest.showMockRequestByUri(uri.projectId,uri.uriId,uri.uriName,uri.uriUrl,uri.uriMethod,uri.uriStatus);

				} else {
					BootstrapDialog.alert("Failed");
				}
			}
		});	
	}
	
	
	function getRequest(id){
		for(var i=0; i<MockRequest.data.length ;i++){
			var data = MockRequest.data[i];
			if(data.id === id){
				return data;
			}
		}
		return {};
	}
	
	function saveRequest(SaveOrUpdate,dialog) {
		var type = SaveOrUpdate == "insert" ? "POST" : "PUT";
		
		var request = {};
		if (type == "PUT") {
			request.id = $('#ReqId').val();
		}
		request.mockUriId = $('#MockUriId').val();
		request.mockRequestName = $('#MockReqName').val();
		request.responseWait = $('#ReqWait').val();
		request.verifyExpect = $('#verifyExpect').val();
		request.responseExpect = $('#responseExpect').val();
		request.type = $('#type').val();
		request.orderNum = $('#OrderNum').val();
		request.isRun = $('#isRun').val();
		request.requestWait = $('#ReqWait').val();
		request.responseWait = $('#ResWait').val();
		
		dialog.close();	
		
		httprequest.send({
			type : type,
			url :  "/mock/request",
			contentType:'json',
			dataType:'json',
			data : request,
			success : function(obj,status) {
				if (obj.code == 0) {
					var uri = MockRequest.uri;
					BootstrapDialog.alert("Success");
					MockRequest.showMockRequestByUri(uri.projectId,uri.uriId,uri.uriName,uri.uriUrl,uri.uriMethod,uri.uriStatus);

				} else {
					BootstrapDialog.alert("Failed");
				}
			}
		});
	}
	
	function requestDemo(ReqId,MockUriId,MockReqName,orderNum,type,isRun,ReqWait,verifyExpect,responseExpect){
		var runStatus =  isRun === "RUN" ? "selected" : "";
		var stopStatus =  isRun === "STOP" ? "selected" : "";
		
		var table = "";
		table += "<table class='table'>";
		table += "<tr>";
		table += "<td colspan='4'>";
		table += "Strategy name:";
		table += "<input type='hidden' id='ReqId' value='"+ReqId+"'  >";
		table += "<input type='hidden' id='MockUriId' value='"+MockUriId+"'  >";
		table += "<input type='text' id='MockReqName' value='"+MockReqName+"'  class='wt300'>";
		table += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Order: &nbsp;&nbsp;&nbsp;&nbsp;<input type='text' id='OrderNum' value='"+orderNum+"'  class='wt50'>"
		table += "</td >";
		table += "</tr>";
		table += "<tr>";
		table += "<td>";
		table += "Response wait: ";
		table += "</td >";
		table += "<td>";
		table += "<input type='text' id='ReqWait' value='"+ReqWait+"' class='wt50'>";
		table += "</td >";
		table += "</tr>";
		table += "<tr>";
		table += "<td colspan='4'>";
		table += "";
		table += "Strategy JS:";
		table += "</td >";
		table += "</tr>";
		table += "<tr>";
		table += "<td colspan='4'>";
		table += "<textarea id='verifyExpect' spellcheck='false' style='margin: 0px; width: 850px; height: 200px;'></textarea>";
		table += "</td>";
		table += "</tr>";
		table += "<tr>";
		table += "<td colspan='4'>";
		table += "";
		table += "Response JS:";
		table += "</td >";
		table += "</tr>";
		table += "<tr>";
		table += "<td  colspan='4'>";
		table += "<textarea id='responseExpect' spellcheck='false' style='margin: 0px; width: 850px; height: 200px;'></textarea>";
		table += "</td>";
		table += "</tr>";
		table += "</table>";
		return table;
	}
	
	function updateStatus(obj,type,id,state){
		var opt={};
		opt.url = "/mock/uri/updateStatus";
		opt.type="POST";
		opt.dataType="json";
		opt.data=JSON.stringify({
			type : type,
			id : id,
			state : state
		});
		opt.success = function(data) {
			if(data.code == 0){
				var uriname = obj.attr('uriname');
				obj.bootstrapSwitch('toggleState',true);
				refreshTree(obj.attr('projectid'));
				setTimeout(function(){
						$('li:contains('+uriname+')').click();
				},300);
			}else{
				BootstrapDialog.alert('update status failed');
			}
		};
		opt.error = function(data){
			BootstrapDialog.alert('update status failed');
		};
		tool.request.exec(opt);
	}
	
	function refreshTree(projectId){
		var tree = [];
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
								
								require('mock-uri').currentUri = node;
								MockRequest.showMockRequestByUri(projectId,node.mockUriId,node.text,node.mockUri,node.mockMethod,node.mockIsRun);
							}
						}
					});
				}
			}
		});
	}
	
	return MockRequest;
});