define(['jquery','tool','bootstrap-switch','bootstrap-dialog'],
function($,tool,BootstrapSwitch,BootstrapDialog){
	Mockconf={}
	
	Mockconf.init = function(){
		var opt={};
		opt.url = "/mock/conf";
		opt.type="POST";
		opt.dataType="json";
		opt.data={
			method : "getMockStatus"
		};
		opt.success = function(data) {
			
			if(data.code==0){
			$('#mockswitchli').append('<input id="mockswitch" type="checkbox" class="switch" data-on-text="On" data-off-text="Off" data-on-color="success" data-size="mini" checked />');
			//$("#mockswitch").onSwitchChange(updateMockStatus());
			$('#mockswitch').bootstrapSwitch('state',1==data.data.value?true:false);
			$('.bootstrap-switch-id-mockswitch').attr('title','mock switch');
			$('#mockswitch').bind('switchChange.bootstrapSwitch',function(event){
				var state = $("#mockswitch").is(":checked");
				$('#mockswitch').bootstrapSwitch('state', !state, true);
				update($("#mockswitch"),state);
			});
			}else{	
				console.log("init mock status failed ",data);	
			}
		};
		opt.error = function(data){
			console.log("init mock status failed ",data);
		};
		tool.request.exec(opt);
		
	}
	
	function update(obj,value){
		var opt={};
		opt.url = "/mock/conf";
		opt.type="POST";
		opt.dataType="json";
		opt.data={
			method : "updateMockStatus",
			value : value
		};
		opt.success = function(data) {
			if(data.code==0){
				obj.bootstrapSwitch('toggleState',true);
			}else{
				BootstrapDialog.alert('Set mock status failed');
			}
		};
		opt.error = function(data){
			BootstrapDialog.alert('Set mock status failed');
		};
		tool.request.exec(opt);
	}
	
	function getMockStatus(){
		
		var opt={};
		opt.url = "/mock/conf";
		opt.type="POST";
		opt.dataType="json";
		opt.data={
			method : "getMockStatus"
		};
		opt.success = function(data) {
			$('#cfresult').val(data);
		};
		opt.error = function(data){
			$('#cfresult').val(data);	
		};
		tool.request.exec(opt);
	}
	
	function updateMockStatus(){
		var opt={};
		opt.url = "/mock/conf";
		opt.type="POST";
		opt.dataType="json";
		opt.data={
			method : "updateMockStatus",
			value : $("#mockswitch").is(":checked")
		};
		opt.success = function(data) {
			return data;
		};
		opt.error = function(data){
			return data;	
		};
		return tool.request.exec(opt);
	}
	
	return Mockconf
});