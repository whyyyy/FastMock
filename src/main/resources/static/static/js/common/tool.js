define(['jquery'],function(){
	request = {};
	request.exec = function(opt){
		switch(opt.reqDataType){
		    case 'form':
                opt.contentType =  'application/x-www-form-urlencoded; charset=UTF-8';
                break;
            case 'json':
                opt.contentType =  'application/json; charset=UTF-8';
                break;
            case 'xml':
                opt.contentType =  'application/xml; charset=UTF-8';
                break;
             default:
                opt.contentType =  'application/x-www-form-urlencoded; charset=UTF-8';
                break;
		}
		var successFn = opt.success;
		opt.success = function(msg){
			 if(typeof msg=='string'?msg.indexOf('SessionOut') > -1:msg.SessionOut){
				 alert("Timeout, please log in.");
				 top.window.location.href = '';
			 }else{
				 successFn(msg);
			 }
		};
		$.ajax(opt);
	};
	
	request.middle = function(obj){
		var height = ($(window).height()-$('#'+obj).height())/2;
		var width = ($(window).width()+$('#'+obj).width())/2;
		
		var position = {
			h : height,
			w : width
		};
		
		return position;
	}
	
	return {request:request};
});