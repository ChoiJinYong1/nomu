<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<HTML>
	<HEAD>
		<TITLE> Test Page </TITLE>
		<script language="javascript" src="./script/jquery-1.11.0.min.js"></script>
		<script src="http://code.jquery.com/jquery-latest.js"></script>
		<script language="javascript">
			var appUrl = "http://nomu.hyunjang.co.kr:8001/NomuApiService/rest";
			
			/**
			 * HashMap
			 * 
			 * @returns
			 */
			HashMap = function () {
			    this.map = new Object();
			};
	
			HashMap.prototype = {
			    put: function (key, value) {
			        this.map[key] = value;
			    },
			    get: function (key) {
			        return this.map[key];
			    },
			    containsKey: function (key) {
			        return key in this.map;
			    },
			    containsValue: function (value) {
			        for (var prop in this.map) {
			            if (this.map[prop] == value) return true;
			        }
			        return false;
			    },
			    getKey: function (value) {
			        for (var prop in this.map) {
			            if (this.map[prop] == value) return prop;
			        }
			        return null;
			    },
			    isEmpty: function (key) {
			        return (this.size() == 0);
			    },
			    clear: function () {
			        for (var prop in this.map) {
			            delete this.map[prop];
			        }
			    },
			    remove: function (key) {
			        delete this.map[key];
			    },
			    keys: function () {
			        var keys = new Array();
			        for (var prop in this.map) {
			            keys.push(prop);
			        }
			        return keys;
			    },
			    values: function () {
			        var values = new Array();
			        for (var prop in this.map) {
			            values.push(this.map[prop]);
			        }
			        return values;
			    },
			    
			    size: function () {
			        var count = 0;
			        for (var prop in this.map) {
			            count++;
			        }
			        return count;
			    }
			};
	
			rowHashMap = new HashMap();
			
			function fnCall(id) {
				var rowData;
				var self = this;
				
				if(id == "CUSTOM") {
					rowData = new Object();
					rowData.url = $("#URL_User").val();
					rowData.json = $("#DATA_User").val();
				}else {
					rowData = rowHashMap.get(id);
				}

			    $.ajax({
			        type : 'post',
			        url : appUrl + rowData.url,
			        dataType : 'json',
			        data : {"JSON": rowData.json},
			        beforeSend : function(xhr){
			            xhr.setRequestHeader("appId", $("#appId").val()); 
			            xhr.setRequestHeader("deviceId", $("#deviceId").val());
			            xhr.setRequestHeader("authKey", $("#authKey").val());
			        },
			        error: function(xhr, status, data){
			        	$("#url").val(rowData.url);
						$("#request").val(rowData.json);
						$("#response").val(JSON.stringify(data)); 
			        },
			        success : function(data){
			        	$("#url").val(rowData.url);
						$("#request").val(rowData.json);
						$("#response").val(JSON.stringify(data));
						
						var row0 = data.ROWS[0],
							resultData = row0.RESULT_DATA,
							rowInfo = null;
						
						if ("COMMON.AUTHLOGIN_S" == row0.REQUEST_ID){
							if ("200" == row0.RESULT_CODE) {
								rowInfo = resultData[0];
								
								$("#appId").val(rowInfo.APPID);
								$("#deviceId").val(rowInfo.DEVICEID);
								$("#authKey").val(rowInfo.AUTHKEY);
							}
						}
			        },
			    });
			}
			
			function fnOnload() {
				var rowData = new Object();

				// ------------------------------------------------------------------------------------------------------------
				// 공통
				//사용자 인증로그인(SESSION CHECK)
				rowData = new Object();
				rowData.id 		= "COMMON.AUTHLOGIN_S";
				rowData.url 	= "/web/login";
				rowData.json 	= '{"ROWS":[{"REQUEST_ID":"S|COMMON.AUTHLOGIN_S","PARAMETER":{"USERID":"sb0950","USERPW":"sbsb7356","APPID":"API_WEB","DEVICEID":"API_NOMU_M0079","REMARK":"IE 8.0"}}]}';
				rowHashMap.put(rowData.id, rowData);

				// ------------------------------------------------------------------------------------------------------------
				// 노무API
				//노무자출역정보조회(일일)
				rowData = new Object();
				rowData.id 		= "USERAPI.WORKING_SEARCH_S";
				rowData.url 	= "/web/workercommon";
				rowData.json 	= '{"ROWS":[{"REQUEST_ID":"S|USERAPI.WORKING_SEARCH_S","PARAMETER":{"COM_CD":"M0079","SITE_CD":"S300011863","REQDATE":"2018-11-15","RES_NO":""}}]}';
				rowHashMap.put(rowData.id, rowData);
			}
			
			function fnLink(a) {
				window.open(a);
			}
			
		</script>
	</HEAD>
	
	<BODY onload="fnOnload();">
		<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
			<tr height="20px">
				<td valign="top">
					<input type="button" value="사용자정의" 	onclick="fnCall('CUSTOM');">
					<input type="button" value="JSON Parser" 		onclick="fnLink('http://json.parser.online.fr');">
				</td>
			</tr>
			<tr height="30px">
				<td valign="MIDDLE">
					<input type="button" value="사용자인증로그인" 		onclick="fnCall('COMMON.AUTHLOGIN_S');">
				</td>
			</tr>
			<tr height="30px">
				<td valign="MIDDLE">
					<input type="button" value="근로자출역조회(일일)" 		onclick="fnCall('USERAPI.WORKING_SEARCH_S');">
				</td>
			</tr>
			<tr height="100px">
				<td valign="top">
					appId: <input id="appId" type="text" size="50"><br>
					deviceId: <input id="deviceId" type="text" size="50"><br>
					authKey: <input id="authKey" type="text" size="50">
				</td>
			</tr>
			<tr>
				<td valign="top">
					<textarea id="URL_User" rows="1" cols="150">/web/workercommon</textarea>
					<textarea id="DATA_User" rows="5" cols="150"></textarea>
				</td>
			</tr>
			<tr>
			<td valign="top">
				<textarea id="url" rows="1" cols="150"></textarea>
				<textarea id="request" rows="3" cols="150"></textarea>
				<textarea id="response" rows="10" cols="150"></textarea>
			</td>
			</tr>
		</table>
	</BODY>
</HTML>
