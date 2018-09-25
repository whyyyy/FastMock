define(['jquery',"common-util"],function($,util){
		
		 httprequest = {};
		 
    	 httprequest.settings = {
    			 url : '',
    			 type : 'GET',
    			 dataType : 'json',
    			 //data : {},
    			 async : true,
    			 success : function(data, textStatus){},
    			 error : function(XMLHttpRequest, textStatus, errorThrown){},
    			 complete : function(XMLHttpRequest, textStatus){},
    			 setHeader : function(){},  			 
    	 };
    	 
    	 httprequest.init=function(){
    			 var s = httprequest.settings;
    			 s.type='GET';
    			 s.dataType='json';
    			 s.data ={};
    			 s.async=true;  					
    					
    	 };
    	 
    	 httprequest.send = function(options){
    		 var settings,options = options || {},contentType,data;
    		 $.extend(httprequest.settings,options);
    		 settings = httprequest.settings;
    		 
    		 if(util.isNull(settings.contentType) || settings.contentType =='form' ){
    			 contentType =  'application/x-www-form-urlencoded; charset=UTF-8';
    			 data = settings.data;
    		 } else if( settings.contentType =='json'){
    			 contentType =  'application/json; charset=UTF-8';
    			 data = JSON.stringify(settings.data);
    		 } 
    		 
    		 reqsetting ={
 	            url : settings.url,
 	            type : settings.type,
 	            dataType : settings.dataType,
 	            async : settings.async,
 	            beforeSend :  httprequest.settings.beforeSend,
 	            success : httprequest.settings.success,
 	            error : httprequest.settings.error,
 	            complete : httprequest.settings.complete,
    		 }
    		 
	            
	          if(settings.type != "GET"){
	        	  reqsetting.contentType =contentType;
	        	  reqsetting.data =data;
	        	  
	          }
    		 
    		 $.ajax(reqsetting);
    	 };
    	 
    	 return httprequest
    	 
});