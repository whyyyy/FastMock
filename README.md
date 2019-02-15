# FastMock

This Mock service for third platform:
    Using java script for strategy and response;
    GET, POST is supported for request method, other method is not verified;
    Json, Form, Xml is supported for request data type. Multipart/form-data is currently not supported;
    Html, Json, Form, Xml is supported for response data type;
    Supporting call back function after mock response;
This server can be used as a proxy.
    If you configure your host(point domain to ip of this mock server) in terminal,
    all requests form that terminal when no strategy is hit will be forwarded to its original destination.
    Also there is a switch for mock function on top level. If it is switched off, this server will try to forward all requests no matter if there is strategy hit.
    
Screen shot
<img src="/screenshot.png" />
    
# Installation

This project is based on springboot2. To start the server, you can run MockApplication or packet the project to jar. 

DB-Mysql
Before starting the server, make sure the mysql server is initiated and configured in application.yml. You can initiate the database by using initsql.sql.

Proxy function
If you want to use this function which described in the General, HTTP and reverse proxy server will be nessesary. Nginx is prefferd. nginx.conf.example is an example for nginx configuration, you can simply use this conf.

# Starting

Index page is locate in http://localhost:7801/, you can add your own strategy and response here.

# Change log
v2.0:
Rebuild front end, using vue and element-ui instead, more friendly, easier to operation.
Optimize some sql issue.
