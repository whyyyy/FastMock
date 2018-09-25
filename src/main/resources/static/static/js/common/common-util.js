define(function(){
    	 function isType(type) {
    	        return function (obj) {
    	            return {}.toString.call(obj) == "[object " + type + "]";
    	        };
    	    }
    	    function isNull(obj) {
    	        for (var i in obj) {
    	            return false;
    	        }
    	        return true;
    	    }
    	    
    	    function show(obj){
    	    	alert(obj);
    	    }
    	    
    	    function startWith(obj,s){
	    	    	if(s==null||s==""||obj.length==0||s.length>obj.length)
	    	    		   return false;
	    	    		  if(obj.substr(0,s.length)==s)
	    	    		     return true;
	    	    		  else
	    	    		     return false;
	    	    		  return true;
    	    }
    	    
    	    function endWith(obj,s){
	    	    	if(s==null||s==""||obj.length==0||s.length>obj.length)
	    	    	     return false;
	    	    	  if(obj.substring(obj.length-s.length)==s)
	    	    	     return true;
	    	    	  else
	    	    	     return false;
	    	    	  return true;
    	    }
    	    
    	    return {
    	    	isObject : isType("Object"),
        	    isString : isType("String"),
        	    isArray : Array.isArray || isType("Array"),
        	    isFunction : isType("Function"),
        	    isNumber : isType("Number"),
        	    isNull : isNull,
        	    startWith : startWith,
        	    endWith : endWith,
        	    show :show
    	    }
    	    
})