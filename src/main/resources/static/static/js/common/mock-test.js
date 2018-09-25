define(['jquery','require','httprequest','bootstrap-dialog','js-beautify','tool'],
function($,require,httprequest,BootstrapDialog,jsbeautify,tool){

    mockTest = {};

    mockTest.init = function(){
        $("#testmock").on("click",function(){
            mockTest.openDialog();
            $("#testmock").hide();
        });
    };

    mockTest.openDialog = function(){
        BootstrapDialog.show({
            size: BootstrapDialog.SIZE_WIDE,
            title:"Test mock",
            message: testDialog(),
            closable: false,
            closeByBackdrop: false,
            closeByKeyboard: false,
            buttons: [{
                label: 'Test',
                cssClass: 'btn-primary',
                action: function(dialog){
            	mockTest.startTest();
                }
            }, {
                label: 'Close',
                action: function(dialog){
            		dialog.close();
            		$("#testmock").show();
                }
            }]
        });
    }

    mockTest.startTest = function(){
        var url = $('#testMockUri').val();
        var method = $('#testRequestMethod').val();
        var dataType = $('#testDataType').val();
        var params = eval('('+ $('#testParam').val()+')');

        var opt={};
        opt.url = "/mock/test";
        opt.type=method;
        opt.headers=params.header;
        opt.headers["mockuri"]=url;
        opt.reqDataType=dataType
        opt.dataType="json"
        switch(dataType){
            case 'form':
                opt.data=eval('({'+params.param.replace(/=/g,':\'').replace(/&/g,'\',')+'\'})');
                break;
            case 'json':
                opt.data=JSON.stringify(params.param);
                break;
            case 'xml':
                opt.data=params.param;
                break;
        }
        opt.success = function(data) {
            if(data.code==0){
                $('#responseExpect').val(data.data);
            }else{
                $('#responseExpect').val(data.msg);
            }
        };
        opt.error = function(data) {
            BootstrapDialog.alert("Request error");
        };
        tool.request.exec(opt);
    }

    function testDialog(){

    		var table = "";
    		table += "<table class='table'>";
    		table += "<tr>";
    		table += "<td colspan='4'>";
    		table += "URI:";
    		table += "<input type='text' id='testMockUri' class='wt300' />";
    		table += "</td >";
    		table += "</tr>";
    		table += "<tr>";
            table += "<td>";
            table += "Request method:";
            table += "<select id='testRequestMethod' class='wt200' onclick='mockTest.dialogInit()'><option value='post'>post</option><option value='get'>get</option></select>";
            table += "</td >";
            table += "<td>";
            table += "Data type:";
            table += "<select id='testDataType' class='wt200'><option id='form' value='form'>form</option><option id='json' value='json'>json</option><option id='xml' value='xml'>xml</option></select>";
            table += "</td >";
            table += "</tr>";
    		table += "<tr>";
    		table += "<td colspan='4'>";
    		table += "";
    		table += "Request params";
    		table += "</td >";
    		table += "</tr>";
    		table += "<tr>";
    		table += "<td colspan='4'>";
    		table += "<textarea id='testParam' spellcheck='false' style='margin: 0px; width: 850px; height: 80px;'>{header:{}, param:}</textarea>";
    		table += "</td>";
    		table += "</tr>";
    		table += "<tr>";
    		table += "<td colspan='4'>";
    		table += "";
    		table += "Result";
    		table += "</td >";
    		table += "</tr>";
    		table += "<tr>";
    		table += "<td  colspan='4'>";
    		table += "<textarea id='responseExpect' spellcheck='false' style='margin: 0px; width: 850px; height: 150px;'></textarea>";
    		table += "</td>";
    		table += "</tr>";
    		table += "</table>";

    		return table;
    	}

    mockTest.dialogInit = function(){
        $('#testRequestMethod').unbind();
        $('#testRequestMethod').change(function(){
            var selected=$(this).children('option:selected').val()
            if(selected == 'get'){
                $('#testDataType').val('form');
                $('#testDataType').attr('disabled','disabled')
            }else{
                $('#testDataType').removeAttr('disabled')
            }
        });
    }

    return mockTest;
})