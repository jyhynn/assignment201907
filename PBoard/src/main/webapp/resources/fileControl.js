/**
 * file control js
 */
//업로드 영역에 변화 감지 시 uploadFile에 있는 정보 가져오기
function detect(cloneObj,jsp,changing){
	console.log("★uploadevent 변화감지★ : " + jsp + " / " + changing);
	$(".uploadevent").show();
	//multipart/form-data 형태의 폼을 한꺼번에 처리하기
	var formData = new FormData();
	
	//file 안에 들어있는 여러개의 첨부된 파일 가져오기
	var inputFile = $("input[name='uploadFile']"); //value에 담길 값.
	var files = inputFile[0].files;	
	var len = $(".uploadResult ul li").length;
	var one = inputFile[0].files.length;
	console.log("한번첨부 갯수 : " + one);
	console.log("총첨부 갯수 : " + len);
	
	//확장자 및 파일크기 확인
	for(var i=0; i<files.length; i++){
		if(!checkExtension(files[i].name, files[i].size)){	
			$(".btn-attach").html(cloneObj.html());
			return false;
		}
		formData.append("uploadFile", files[i]);
	}
		
	if(len+one<6){
		//formData를 서버에 전송
		$.ajax({
			url : "/uploadAjaxServer",
			data : formData,		//전송할 데이터
			processData : false,	//formData를 사용시 데이터를 어떤 방식으로 변환할 것인지 결정
			contentType : false,	//true : application/x-222-form-urlencoded이기 때문에 false로 지정.
			type : "post",
			dataType : "json",		//되돌아오는 데이터 타입. 첨부리스트 돌려받음
			beforeSend : function(xhr){   //데이터 전송 전에 헤더에 csrf값을 설정
				xhr.setRequestHeader($('#csrf').attr('name'), $('#csrf').attr('content'));
	        },
			success : function(result){
				console.log(result);
				showUploadedFile(result,jsp,changing);
				$(".btn-attach").html(cloneObj.html());
			},
			error : function(error){
				console.log("ㅠㅠ" + error.status);
				alert("파일 사이즈 초과 오류.");
				$(".btn-attach").html(cloneObj.html());
				if(!$(".uploadResult").has($(".attahLi"))){
					$(".uploadevent").css("display","none");
				}
				if($("#fileListImg ul li").length==0){
					$("#fileListImg").css("display","none");
				}
				if($("#fileListEct ul li").length==0){
					$("#fileListEct").css("display","none");
				}
				return;
			}
		});//ajax
	}else{
		alert("첨부파일 개수는 5개를 초과할 수 없습니다.");
		$(".btn-attach").html(cloneObj.html());
		return;
	}
}

//서버에서 result를 받은 후 result 보여주기
function showUploadedFile(uploadResultArr, jsp, changing){
	var fileCount = 0;	// 총 파일 개수
	console.log("수정 : " + changing);
	console.log("result : " + jsp);
	//결과 보여줄 영역 가져오기
	var uploadResultImg = $(".uploadResult ul#fileUlImg");
	var uploadResultFile = $(".uploadResult ul#fileUlFile");
	var fileCounts = $("#fileCount");
	var fileSize = 0;	// 개별 파일 사이즈
	
	if(uploadResultArr.length == 0){
		$(".uploadResult").css("display","none");
		$("#fileListImg").css("display","none");
		$("#fileListEct").css("display","none");
	}
	if($("#fileListImg ul li").length==0){
		$("#fileListImg").css("display","none");
	}
	if($("#fileListEct ul li").length==0){
		$("#fileListEct").css("display","none");
	}
	$(uploadResultArr).each(function(i,obj){
		if(obj.fileType){//true : 이미지
			$("#fileListImg").show();
			fileSize += obj.fileSize;
			//썸네일 이미지 경로	//encodeURIComponent : 클라이언트 쪽에서 인코딩. uploadPath에 d/upload/2019/07/16 들어있음. 
			var filePath = encodeURIComponent(obj.uploadPath + "\\thum_" + obj.uuid + obj.fileName);
		
			//원본파일이미지경로
			var oriPath=obj.uploadPath+"\\"+obj.uuid + obj.fileName;
			oriPath = oriPath.replace(new RegExp(/\\/g), "/"); //폴더 구분의 /를 \로 바꾸기
			
			if(jsp=="read" || changing==false){
				uploadResultImg.append("<li class='attahListImg attahLi list-group-item' style='border:none; padding-left:10px;' "
								+ "data-path='" + obj.uploadPath
								+ "' data-uuid='" + obj.uuid
								+ "' data-filename='" + obj.fileName
								+ "' data-type='" + obj.fileType
								+ "' data-time='" + obj.uploadtime
								+ "' data-size='" + obj.fileSize + "'>"
								+ "<div class='sizingBox overflow-hidden' style='width:99px; text-overflow: ellipsis;'>"
								+ "<img id='watch-image'"
								+ " data-ori='" + oriPath
								+ "' src='/display?fileName=" + filePath + "'>"
								+ "<br>"
								+ "<a href=\"javascript:download(\'" + obj.uploadPath.replace(new RegExp(/\\/g), "/") + "\',\'" + obj.uuid + "\',\'" + obj.fileName + "\')\"'>"
								+ "<small>" + obj.fileName + "</small></a>"
								+ "</a><br>"
								+ "<span style='color:gray; margin:0;'><small>" + getSize(obj.fileSize) + "</small></span>"
								+ "</div>"
								+ "<span class='deleteBtn close'"
								+ "style='display:none;'"
								+ "data-file='" + filePath
								+ "' data-type='image'>&times;</span></li>");
			}
			if(changing == true){
				$("#fileUlImg li span.deleteBtn").show();
			}
			if(jsp=="write"){
				uploadResultImg.append("<li class='attahListImg attahLi list-group-item' style='border:none;  padding-left:10px;  margin:0;' "
								+ "data-path='" + obj.uploadPath
								+ "' data-uuid='" + obj.uuid
								+ "' data-filename='" + obj.fileName
								+ "' data-type='" + obj.fileType
								+ "' data-time='" + obj.uploadtime
								+ "' data-size='" + obj.fileSize + "'>"
								+ "<img id='watch-image'"
								+ " data-ori='" + oriPath
								+ "' src='/display?fileName=" + filePath + "'><br>"
								+ "<span><small>" + obj.fileName + "</small></span>"
								+ "</a><br>"
								+ "<p style='color:gray'><small>" + getSize(obj.fileSize) + "</small></p>"
								+"<span class='deleteBtn close'"
								+ "data-file='" + filePath
								+ "' data-type='image'>&times;</span></li>");
			}
		}else{//false:이미지 외
			$("#fileListEct").show();
			var filePath = obj.uploadPath + "\\" + obj.uuid + obj.fileName;
			filePath = filePath.replace(new RegExp(/\\/g), "/"); //폴더 구분의 /를 \로 바꾸기
			fileSize += obj.fileSize;
			if(jsp=="read" || changing == 'false'){
				uploadResultFile.append("<li class='attahListFile attahLi list-group-item' style='padding:10px;border-left:none;border-right:none;border-bottom:none' "
									+ "data-path='" + obj.uploadPath
									+ "' data-uuid='" + obj.uuid
									+ "' data-filename='" + obj.fileName
									+ "' data-type='" + obj.fileType
									+ "' data-time='" + obj.uploadtime
									+ "' data-size='" + obj.fileSize + "'>"
									+ "<a href=\"javascript:download(\'" + obj.uploadPath.replace(new RegExp(/\\/g), "/") + "\',\'" + obj.uuid + "\',\'" + obj.fileName + "\')\">" + obj.fileName + "</a> &nbsp;&nbsp;"
									+ "<p style='color:gray; display:inline;'><small>" + getSize(obj.fileSize) + "</small></p>"
									+ "<span class='deleteBtn close' "
									+ "style='display:none;' "
									+ "data-file='" + filePath
									+ "' data-type='file'>&times;</span></li>");
			}
			if(changing == true){
				$("#fileUlFile li span.deleteBtn").show();
			}
			if(jsp=="write"){
				uploadResultFile.append("<li class='attahListFile attahLi list-group-item' style='padding:10px;border-left:none;border-right:none;border-bottom:none' "
									+ "data-path='" + obj.uploadPath
									+ "' data-uuid='" + obj.uuid
									+ "' data-filename='" + obj.fileName
									+ "' data-type='" + obj.fileType
									+ "' data-time='" + obj.uploadtime
									+ "' data-size='" + obj.fileSize + "'>"
									+ "<span>" + obj.fileName + "</span> &nbsp;&nbsp;"
									+ "<p style='color:gray; display:inline;'><small>" + getSize(obj.fileSize) + "</small></p>"
									+ "<span class='deleteBtn close' "
									+ "data-file='" + filePath
									+ "' data-type='file'>&times;</span></li>");
			}
		}
		fileCount++;
	});
	fileCounts.text(" 총 " + fileCount + "개 (" + getSize(fileSize) + ")");
}//showUploadedFile

// 글 등록, 수정 시 첨부된 파일 리스트 input으로 붙이기
function addAttach(lis){
	var str = "";
	$(lis).each(function(i,obj){
		var job = $(obj);
		str+="<input type='hidden' name='attach[" + i + "].uuid' value='" + job.data("uuid") + "'>";
		str+="<input type='hidden' name='attach[" + i + "].uploadPath' value='" + job.data("path") + "'>";
		str+="<input type='hidden' name='attach[" + i + "].fileName' value='" + job.data("filename") + "'>";
		str+="<input type='hidden' name='attach[" + i + "].fileType' value='" + job.data("type") + "'>";
		str+="<input type='hidden' name='attach[" + i + "].uploadtime' value='" + job.data("time") + "'>";
		str += "<input type='hidden' name='attach[" + i + "].fileSize' value='" + job.data("size") + "'>";
	});
	return str;
}
// 첨부파일의 크기와 확장자 제한
function checkExtension(fileName,fileSize){
	var regex = new RegExp("(.*?)\.(exe|sh|alz|java|jsp|php|egg)$");
	var maxSize = 10485760; //10mb
	console.log("사이즈 및 확장자 체크 완료");
	if(fileSize>maxSize){
		alert("파일 사이즈 초과. 한 파일 당 10MB를 넘을 수 없습니다.");
		return false;
	}
	if(regex.test(fileName)){
		alert("해당 파일의 확장자는 업로드 할 수 없습니다.\n첨부불가 확장자 : exe | sh | alz | java | jsp | php | egg");
		return false;
	}
	return true;
}//확장자검사

//등록,수정 시 x클릭된 파일 삭제
function deleteFile(targetLi, board_no, jsp){
	console.log("파일 삭제 실행");
	//targetLi = $(this).closest("li");
	targetLi.remove();
	if(!$(".uploadResult").has($(".attahLi"))){
		$(".uploadevent").css("display","none");
	}
	if($("#fileListImg ul li").length==0){
		$("#fileListImg").css("display","none");
	}
	if($("#fileListEct ul li").length==0){
		$("#fileListEct").css("display","none");
	}
}

// 파일 사이즈 뷰 설정
function getSize(bytes) {
    var s = "";
    var kb = bytes / 1024;
    var mb = kb / 1024;
    if(bytes < 1024) {
        s = bytes + " Bytes";
    }else if(bytes >= 1024 && bytes < (1024 * 1024)) {
    	s = parseFloat(Math.round(kb * 100) / 100).toFixed(2) + " KB";
    }else if(bytes >= (1024 * 1024) && bytes < (1024 * 1024 * 1024)) {
    	s = parseFloat(Math.round(mb * 100) / 100).toFixed(2) + " MB";
    }
    return s;
}

// Post방식 개별파일 다운로드
function download(filepath, uuid, filename){
	var url = '/download';
	var inputs = '';
	inputs+='<input type="hidden" name="_csrf" value="'+ $('#csrf').attr('content') +'" />'; 
	inputs+='<input type="hidden" name="filePath" value="'+ filepath +'" />'; 
	inputs+='<input type="hidden" name="fileUuid" value="'+ uuid +'" />'; 
	inputs+='<input type="hidden" name="fileName" value="'+ filename +'" />'; 
	$('<form id="dldl" action="'+ url +'" method="post">' + inputs + '</form>').appendTo('body');
	$("#dldl").submit().remove();
}

// Post방식 전체 파일 다운로드
function downloadZip(data){
	var url = '/downloadZip';
	var inputs = '';
	inputs+='<input type="hidden" name="_csrf" value="'+ $('#csrf').attr('content') +'" />'; 
	inputs+='<input type="hidden" name="files" value="'+ data +'" />';
	$('<form id="dldl" action="'+ url +'" method="post">'+inputs+'</form>').appendTo('body');
	$("#dldl").submit().remove();
}

// 전체파일 다운로드 시 첨부 리스트 읽기
function dlFileAllList(obj){
	var job = $(obj);
	var sepPath = new Array();
	var path = job.data("path").replace(new RegExp(/\\/g), "/");
	var uuid = job.data("uuid");
	var filename = job.data("filename");
	sepPath.push(path);
	sepPath.push(uuid);
	sepPath.push(filename);
	
	return sepPath;
}

