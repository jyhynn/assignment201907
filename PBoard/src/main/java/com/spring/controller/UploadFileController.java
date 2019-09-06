package com.spring.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpServletRequest;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.spring.domain.BoardAttachVO;

import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnailator;

@Slf4j
@Controller
public class UploadFileController {

	private String fixedPath = "d:\\upload\\";
	private String downloadPath = "d:\\download\\";
	// private String web_path = "/upload/"; //상대경로

	@PostMapping(value = "/uploadAjaxServer", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<List<BoardAttachVO>> uploadAjaxPost(MultipartFile[] uploadFile, HttpServletRequest request) {
		log.info("ajax 파일 업로드 요청");
		// String realPath =
		// request.getSession().getServletContext().getRealPath(web_path);
		// 년/월/일 폴더 형태로 가져오기
		String uploadFolderPath = getFolder();
		File uploadPath = new File(fixedPath, uploadFolderPath);

		// 폴더가 없으면 새로 생성하기
		if (!uploadPath.exists()) {
			uploadPath.mkdirs();
		}

		List<BoardAttachVO> attList = new ArrayList<BoardAttachVO>();
		String uploadFileName = "";

		for (MultipartFile f : uploadFile) {
			log.info("--------------------");
			log.info("File Name : " + f.getOriginalFilename());
			log.info("File Size : " + f.getSize());
			log.info("--------------------");

			if(f.getSize()>10485760) {
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			}
			
			// uuid랑 시간 붙여서 저장
			UUID uuid = UUID.randomUUID();
			Date now = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("HH-mm-ss-SS");
			String time = sdf.format(now);

			uploadFileName = uuid.toString() + "_uuid_" + f.getOriginalFilename();
			File saveFile = new File(uploadPath, uploadFileName);

			// 현재 파일의 저장경로와 파일명, uuid 값을 담는 객체 생성
			BoardAttachVO attach = new BoardAttachVO();
			attach.setUuid(uuid.toString() + "_uuid_");
			attach.setUploadPath(uploadFolderPath);
			attach.setFileName(f.getOriginalFilename());
			attach.setUploadtime(time);
			attach.setFileSize(f.getSize());

			if (checkImageType(saveFile)) {
				attach.setFileType(true);
				// 썸네일작업
				try { // thum_를 붙여서 한번 더 저장해 썸네일이미지를 표시
					FileOutputStream thumbnail = new FileOutputStream(new File(uploadPath, "thum_" + uploadFileName));
					Thumbnailator.createThumbnail(f.getInputStream(), thumbnail, 100, 100);
					thumbnail.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			// 파일저장
			try {
				f.transferTo(saveFile);
				FileCopyUtils.copyToByteArray(saveFile);
				attList.add(attach);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return new ResponseEntity<>(attList, HttpStatus.OK);
	}

	// 파일이 이미지인지 확인하는 메소드
	private boolean checkImageType(File file) {
		// String contentType = Files.probeContentType(file.toPath()); java 버전에 따른 버그 발생 가능성有

		// MimetypesFileTypeMap : JDK 6 부터 JAF(JavaBeans Activation Framework) API에 포함되어 있는 객체.
		// 확장자 판단 불가일 경우 application/octet-stream으로 반환
		// 이미지의 경우 png, gif도 application/octet-stream으로 반환
		MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
		String mimeType = mimeTypesMap.getContentType(file);
		String mimeType2 = URLConnection.guessContentTypeFromName(file.getName()); // jdk버전이 더 낮을 경우
		log.info("mimeType : " + mimeType);
		log.info("mimeType2 : " + mimeType2);
		if (mimeType2 != null && (mimeType.startsWith("image") || mimeType2.startsWith("image"))) {
			return true;
		} else if (mimeType2 == null && (mimeType.startsWith("image"))) {
			return false;
		} else {
			return false;
		}
	}

	// 폴더 생성
	private String getFolder() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		String str = sdf.format(date);
		return str.replace("-", File.separator);
	}

	// 썸네일 이미지를 보여주는 작업
	@GetMapping("/display")
	@ResponseBody
	public ResponseEntity<byte[]> getFile(String fileName, HttpServletRequest request) {
		log.info("-------------------------------- 썸네일이미지 가져오기 " + fileName);
		File file = new File(fixedPath + fileName);
		ResponseEntity<byte[]> result = null;
		HttpHeaders header = new HttpHeaders();
		try {
			header.add("Content-Type", Files.probeContentType(file.toPath()));
			// probeContentType : MIME(text/html,image/jpeg 이런거) 타입 알아내는 역할
			result = new ResponseEntity<byte[]>(FileCopyUtils.copyToByteArray(file), header, HttpStatus.OK);
			// 서버에 있는 걸 바이트 단위로 복사해서 배열에 담음
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	// produces = MediaType.APPLICATION_OCTET_STREAM_VALUE는 모든 데이터를 이 형태로만 받겠다고 선언한 것.
	@PostMapping(value = "/download")
	@ResponseBody
	public ResponseEntity<Resource> download(String filePath, 
											 String fileUuid, 
											 String fileName,
											 @RequestHeader("User-Agent") String userAgent, 
											 HttpServletRequest request) 
	{
		log.info("-------------------------------- 다운로드 요청 : " + filePath + "/" + fileUuid + fileName);
		Resource resource = new FileSystemResource(fixedPath + filePath + "/" + fileUuid + fileName);
		HttpHeaders headers = new HttpHeaders();

		log.info("user" + userAgent);
		try {
			String downloadName = null;
			// 접속브라우저 판단 
			if (userAgent.contains("Trident") || userAgent.contains("Edge")) { // explorer11(Trident), Edge
				// URLEncoder.encode()을 할 때 공백이 있으면 +로 변환시키기 때문에 그걸 찾아서 그냥 공백으로 바꾸기. 
				// 공백 유니코드 : %2B <이걸로도 가능
				downloadName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
			} else {	//chrome, firefox
				downloadName = new String(fileName.getBytes("utf-8"), "ISO-8859-1");
			}
			headers.add("Content-Disposition", "attachment;fileName=" + downloadName);

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(resource, headers, HttpStatus.OK);
	}

	@PostMapping(value = "/downloadZip")
	@ResponseBody
	public ResponseEntity<Resource> zipFileDown(HttpServletRequest request,
												@RequestHeader("User-Agent") String userAgent, 
												String[] files)
	{
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmssSS");
		String time = sdf.format(now);

		String zipFileName = time + ".zip";

		ZipEntry ze = null;
		byte[] buffer = new byte[1024];
		File dlPath = new File(downloadPath);

		if (!dlPath.exists()) {
			dlPath.mkdirs();
		}
		
		// 배열로 넘어온 파일정보 분리
		int loof = 0;
		String[][] ff = new String[files.length/3][3];
		for(int i=0; i<ff.length; i++) {
			for(int j=0; j<ff[i].length; j++) {
				if(loof<files.length) {
					ff[i][j] = files[loof];
				}
				loof++;
			}
		}
		
		try {
			// zip파일 경로 생성
			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(downloadPath + zipFileName));
			// 폴더가 없으면 새로 생성하기
			System.out.println("zip파일 생성 성공, 경로+파일 = " + downloadPath + zipFileName);

			for(int i=0; i<ff.length; i++) {
				String path = "";
				String fileName = "";
				for(int j=0; j<ff[i].length; j++) {
					if(j<2) {
						path += ff[i][j];
						if(j<1) {
							path+="/";
						}
					}else {
						fileName = ff[i][j];
					}
				}
				log.info("전체 경로 : " + path + fileName);
				FileInputStream fis = new FileInputStream(fixedPath + path + fileName);

				ze = new ZipEntry(fileName);
				try {
					out.putNextEntry(ze);
					int len;
					while ((len = fis.read(buffer)) > 0) {
						out.write(buffer, 0, len);
					}
					out.closeEntry();
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

		Resource resource = new FileSystemResource(downloadPath + zipFileName);
		String resourceName = resource.getFilename();
		resourceName = resourceName.substring(resourceName.lastIndexOf("\\") + 1);
		HttpHeaders headers = new HttpHeaders();

		try {
			String downloadName = null;
			if (userAgent.contains("Trident") || userAgent.contains("Edge")) {
				downloadName = URLEncoder.encode(resourceName, "UTF-8").replaceAll("\\+", " ");
			} else {
				downloadName = new String(resourceName.getBytes("utf-8"), "ISO-8859-1");
			}
			headers.add("Content-Disposition", "attachment;fileName=" + downloadName);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return new ResponseEntity<>(resource, headers, HttpStatus.OK);
	}

}
