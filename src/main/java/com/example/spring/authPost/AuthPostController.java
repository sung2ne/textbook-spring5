package com.example.spring.authPost;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * 게시글 관련 요청을 처리하는 웹 컨트롤러 클래스
 * 사용자 요청을 받아 서비스 계층과 연결하고 뷰로 데이터를 전달함
 */
@Controller // Spring MVC에서 이 클래스가 컨트롤러임을 명시
@RequestMapping("/auth-posts")
public class AuthPostController {

    @Autowired
    AuthPostService authPostService;

    // 로깅을 위한 변수
    private static final Logger logger = LoggerFactory.getLogger(AuthPostController.class);

    /**
     * 운영체제에 따라 파일 업로드 경로를 반환하는 메서드
     * - 로컬 환경마다 업로드 경로가 다를 수 있으므로 OS 구분 처리
     * - 본인의 환경에 맞게 수정 필요
     *
     * @return 업로드 경로 (운영체제별 절대경로)
     */
    public String uploadPathByOS() {
        String uploadPath = "";
        String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("win")) {
            uploadPath = "C:/upload/auth-post";
        } else if (os.contains("mac")) {
            uploadPath = "/Users/user/upload/auth-post";
        } else if (os.contains("nux") || os.contains("nix")) {
            uploadPath = "/home/user/upload/auth-post";
        } else {
            throw new IllegalStateException("Unsupported operating system: " + os);
        }

        return uploadPath;
    }

    /**
     * 게시글 목록 화면 요청 처리 (GET 방식)
     */
    @GetMapping("")
    public String listGet(
        @RequestParam(required = false) String searchType,
        @RequestParam(required = false) String searchKeyword,
        @RequestParam(value = "page", defaultValue = "1") int currentPage,
        Model model
    ) {
        int listCountPerPage = 10;
        int pageCountPerPage = 5;

        Map<String, Object> result = authPostService.list(
            currentPage, listCountPerPage, pageCountPerPage, searchType, searchKeyword
        );

        model.addAttribute("posts", result.get("posts"));
        model.addAttribute("pagination", result.get("pagination"));
        model.addAttribute("searchType", result.get("searchType"));
        model.addAttribute("searchKeyword", result.get("searchKeyword"));

        return "authPost/list";
    }

    /**
     * 게시글 등록 화면 요청 처리 (GET 방식)
     */
    @GetMapping("/create")
    public String createGet() {
        return "authPost/create";
    }

    /**
     * 게시글 등록 요청 처리 (POST 방식)
     */
    @PostMapping("/create")
    public String createPost(AuthPostDto post, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String uploadPath = uploadPathByOS();

        try {
            MultipartFile uploadFile = post.getUploadFile();

            if (uploadFile != null && !uploadFile.isEmpty()) {
                String originalFileName = uploadFile.getOriginalFilename();
                String fileName = UUID.randomUUID().toString() + "_" + originalFileName;

                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }

                File fileToUpload = new File(uploadPath + File.separator + fileName);
                uploadFile.transferTo(fileToUpload);

                post.setFileName(fileName);
                post.setOriginalFileName(originalFileName);
            }

            // 세션에서 로그인된 사용자 아이디 가져오기
            String userId = (String) request.getSession().getAttribute("userId");
            post.setUserId(userId);

            int createdId = authPostService.create(post);

            if (createdId > 0) {
                redirectAttributes.addFlashAttribute("successMessage", "게시글이 등록되었습니다.");
                return "redirect:/auth-posts/" + createdId;
            }

            redirectAttributes.addFlashAttribute("errorMessage", "게시글 등록에 실패했습니다.");
            return "redirect:/auth-posts/create";

        } catch (IOException | IllegalStateException e) {
            logger.error("파일 업로드 오류: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute("errorMessage", "파일 업로드 중 오류가 발생했습니다.");
            return "redirect:/auth-posts/create";
        }
    }

    /**
     * 게시글 상세보기 요청 처리 (GET 방식)
     * - 비밀글인 경우 작성자 또는 관리자만 접근 가능
     * @param id 상세 조회할 게시글 ID
     * @param model 뷰에 전달할 게시글 데이터를 담는 객체
     * @param request 세션에서 사용자 정보를 가져오기 위한 객체
     * @return 상세보기 화면 뷰 이름 ("authPost/read.jsp")
     */
    @GetMapping("/{id}")
    public String readGet(@PathVariable("id") int id, Model model, HttpServletRequest request) {
        AuthPostDto post = authPostService.read(id);

        // 세션에서 로그인된 사용자 정보 가져오기
        String userId = (String) request.getSession().getAttribute("userId");
        String role = (String) request.getSession().getAttribute("role");

        // 비밀글일 경우, 작성자 또는 관리자만 접근 가능
        if ("Y".equals(post.getSecret()) && !userId.equals(post.getUserId()) && !"ADMIN".equals(role)) {
            return "redirect:/auth-posts";
        }

        model.addAttribute("post", post);
        return "authPost/read";
    }

    /**
     * 게시글 수정 화면 요청 처리 (GET 방식)
     */
    @GetMapping("/{id}/update")
    public String updateGet(@PathVariable("id") int id, Model model, HttpServletRequest request) {
        AuthPostDto post = authPostService.read(id);

        String userId = (String) request.getSession().getAttribute("userId");

        if (!userId.equals(post.getUserId())) {
            return "redirect:/auth/logout";
        }

        model.addAttribute("post", post);
        return "authPost/update";
    }

    /**
     * 게시글 수정 요청 처리 (POST 방식)
     */
    @PostMapping("/{id}/update")
    public String updatePost(
            @PathVariable("id") int id,
            AuthPostDto post,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes) {

        String uploadPath = uploadPathByOS();
        post.setId(id);

        try {
            AuthPostDto originalPost = authPostService.read(id);

            String userId = (String) request.getSession().getAttribute("userId");

            if (!userId.equals(originalPost.getUserId())) {
                return "redirect:/auth/logout";
            }

            MultipartFile uploadFile = post.getUploadFile();

            if ((uploadFile != null && !uploadFile.isEmpty()) || post.isDeleteFile()) {
                if (originalPost.getFileName() != null) {
                    Path filePath = Paths.get(uploadPath).resolve(originalPost.getFileName());
                    if (Files.exists(filePath)) {
                        Files.delete(filePath);
                    }
                }

                if (post.isDeleteFile()) {
                    post.setFileName(null);
                    post.setOriginalFileName(null);
                }
            }

            if (uploadFile != null && !uploadFile.isEmpty()) {
                String originalFileName = uploadFile.getOriginalFilename();
                String fileName = UUID.randomUUID().toString() + "_" + originalFileName;

                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }

                File fileToUpload = new File(uploadPath + File.separator + fileName);
                uploadFile.transferTo(fileToUpload);

                post.setFileName(fileName);
                post.setOriginalFileName(originalFileName);
            }

            boolean updated = authPostService.update(post);

            if (updated) {
                redirectAttributes.addFlashAttribute("successMessage", "게시글이 수정되었습니다.");
                return "redirect:/auth-posts/" + id;
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "게시글 수정에 실패했습니다.");
                return "redirect:/auth-posts/" + id + "/update";
            }

        } catch (IOException | IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "파일 업로드에 실패했습니다.");
            return "redirect:/auth-posts/" + id + "/update";
        }
    }

    /**
     * 게시글 삭제 요청 처리 (POST 방식)
     */
    @PostMapping("/{id}/delete")
    public String deletePost(
        @PathVariable("id") int id,
        AuthPostDto post,
        HttpServletRequest request,
        RedirectAttributes redirectAttributes
    ) {
        String uploadPath = uploadPathByOS();
        post.setId(id);

        try {
            AuthPostDto originalPost = authPostService.read(id);

            String userId = (String) request.getSession().getAttribute("userId");

            if (!userId.equals(originalPost.getUserId())) {
                return "redirect:/auth/logout";
            }

            if (originalPost != null && originalPost.getFileName() != null) {
                Path filePath = Paths.get(uploadPath).resolve(originalPost.getFileName());
                if (Files.exists(filePath)) {
                    Files.delete(filePath);
                }
            }

            boolean deleted = authPostService.delete(post);

            if (deleted) {
                redirectAttributes.addFlashAttribute("successMessage", "게시글이 삭제되었습니다.");
                return "redirect:/auth-posts";
            }

            redirectAttributes.addFlashAttribute("errorMessage", "게시글 삭제에 실패했습니다.");
            return "redirect:/auth-posts/" + id;

        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "업로드 파일 삭제에 실패했습니다.");
            return "redirect:/auth-posts/" + id;
        }
    }

    /**
     * 게시글 첨부파일 다운로드 요청 처리
     * - 비밀글인 경우 작성자 또는 관리자만 다운로드 가능
     *
     * @param id 다운로드할 게시글 ID
     * @param request 세션에서 사용자 정보를 가져오기 위한 객체
     * @return ResponseEntity<Resource> 형태의 HTTP 응답 (첨부파일 포함)
     */
    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> download(@PathVariable("id") int id, HttpServletRequest request) {
        String uploadPath = uploadPathByOS();

        try {
            AuthPostDto post = authPostService.read(id);

            // 세션에서 로그인된 사용자 정보 가져오기
            String userId = (String) request.getSession().getAttribute("userId");
            String role = (String) request.getSession().getAttribute("role");

            // 비밀글일 경우, 작성자 또는 관리자만 다운로드 가능
            if ("Y".equals(post.getSecret()) && !userId.equals(post.getUserId()) && !"ADMIN".equals(role)) {
                return ResponseEntity.status(403).build();
            }

            if (post == null || post.getFileName() == null) {
                return ResponseEntity.notFound().build();
            }

            Path filePath = Paths.get(uploadPath).resolve(post.getFileName());
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                return ResponseEntity.notFound().build();
            }

            String fileName = post.getOriginalFileName();
            String encodedDownloadName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedDownloadName + "\"")
                    .body(resource);

        } catch (UnsupportedEncodingException | MalformedURLException e) {
            logger.error("파일 다운로드 오류: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
