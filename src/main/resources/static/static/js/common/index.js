require.config({
	paths:{
		'jquery'				:'/static/js/static/jquery',
		'require'				:'/static/js/static/require',
		'common-util'			:'/static/js/common/common-util',
		'httprequest'			:'/static/js/common/httprequest',
		'bootstrap' 			:'/static/bootstrap/js/bootstrap',
		'bootstrap-dialog' 		:'/static/bootstrap-dialog/bootstrap-dialog',
		'bootstrap-treeview' 	:'/static/bootstrap-treeview/bootstrap-treeview',
		'mock-project' 			:'/static/js/common/mock-project',
		'mock-uri' 				:'/static/js/common/mock-uri',
		'mock-request' 			:'/static/js/common/mock-request',
		'mock-conf' 			:'/static/js/common/mock-conf',
		'mock-test' 			:'/static/js/common/mock-test',
		'bootstrap-switch'      :'/static/bootstrap-switch/js/bootstrap-switch',
		'tool'      			:'/static/js/common/tool',
		'js-beautify'			:'/static/js-beautify/beautify'
		},		
		
	shim: {
	        'jquery': {
	            exports: 'jquery'
	        },
	        
	        'bootstrap': {
	            deps: ['jquery']
	        },
	        
	        'bootstrap-dialog': {
	        	deps: ['jquery','bootstrap']
	        },
	        
	        'bootstrap-treeview': {
	        	deps: ['jquery','bootstrap']
	        },
	        
	        'httprequest': {
	        	deps: ['jquery','common-util']
	        },
	        
	        'mock-request': {
	        	deps: ['jquery','bootstrap-dialog','httprequest']
	        },
	        
	        'mock-uri': {
	        	deps: ['jquery','bootstrap-dialog','httprequest','mock-request']
	        },
	        
	        'mock-project': {
	        	deps: ['jquery','bootstrap-dialog','httprequest','mock-uri']
	        },
			
			'mock-conf': {
	        	deps: ['jquery','tool','bootstrap-switch','bootstrap-dialog']
	        },

	        'mock-test': {
                deps: ['jquery','tool','bootstrap-switch','bootstrap-dialog']
            },
	        
	        'bootstrap-switch': {
	        	deps: ['jquery']
	        },
	        
			'tool': {
	        	deps: ['jquery']
	        },
			'js-beautify': {
	        	exports: 'js-beautify'
	        }
	      }
	});


require(['jquery','httprequest','bootstrap-dialog','mock-project','bootstrap-treeview','mock-conf','mock-test'],
function($,httprequest,BootstrapDialog,mockproject,bt,mockconf,mockTest){
	mockproject.init();
	mockconf.init();
	mockTest.init();
});

