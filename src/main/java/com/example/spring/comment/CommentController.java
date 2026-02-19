package com.example.spring.comment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    /**
     * 댓글 등록 요청 처리 (POST, 비동기)
     * - 세션에서 로그인한 사용자 ID를 가져와 댓글 작성자로 설정
     * - 댓글 등록 후 결과를 JSON 형태로 반환
     *
     * @param comment 작성된 댓글 정보
     * @param request HttpServletRequest 객체 (세션 접근용)
     * @return JSON 형식 응답 (result: "ok" 또는 "error")
     */
    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> createPost(CommentDto comment, HttpServletRequest request) {
        // 세션에서 로그인된 사용자 정보 가져오기
        String userId = (String) request.getSession().getAttribute("userId");

        // 사용자 아이디 등록
        comment.setUserId(userId);

        // 댓글 등록 (성공 시 ID 반환)
        int createdId = commentService.create(comment);

        // 응답용 데이터 생성
        Map<String, Object> response = new HashMap<>();
        response.put("result", createdId > 0 ? "ok" : "error");

        // JSON 형식으로 결과 반환
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    /**
     * 댓글 목록
     *
     * @RequestParam authPostId 게시글 ID
     * @return JSON 형식 응답 (result: "ok" 또는 "error")
     */
    @GetMapping("")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> listGet(@RequestParam int authPostId) {
        // 댓글 목록
        List<CommentDto> comments = commentService.list(authPostId);

        // 응답용 데이터 생성
        Map<String, Object> response = new HashMap<>();
        response.put("comments", comments);

        // JSON 형식으로 결과 반환
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    /**
     * 댓글 수정 요청 처리 (POST, 비동기)
     * - 댓글 수정 후 결과를 JSON 형태로 반환
     *
     * @param comment 작성된 댓글 정보
     * @param request HttpServletRequest 객체 (세션 접근용)
     * @return JSON 형식 응답 (result: "ok" 또는 "error")
     */
    @PostMapping("/update")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updatePost(CommentDto comment, HttpServletRequest request) {
        // 응답용 데이터 생성
        Map<String, Object> response = new HashMap<>();

        // 세션에서 로그인된 사용자 정보 가져오기
        String userId = (String) request.getSession().getAttribute("userId");

        // 댓글 정보
        CommentDto existsComment = commentService.read(comment.getId());

        // 사용자 세션 아이디와 댓글 등록한 사용자 아이디 비교
        if (!userId.equals(existsComment.getUserId())) {
            response.put("result", "error");
        } else {
            // 댓글 수정 처리
            boolean updated = commentService.update(comment);

            if (updated) {
                response.put("result", "ok");
            } else {
                response.put("result", "error");
            }
        }

        // JSON 형식으로 결과 반환
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    /**
     * 댓글 삭제 요청 처리 (POST, 비동기)
     * - 댓글 삭제 후 결과를 JSON 형태로 반환
     *
     * @param comment 작성된 댓글 정보
     * @param request HttpServletRequest 객체 (세션 접근용)
     * @return JSON 형식 응답 (result: "ok" 또는 "error")
     */
    @PostMapping("/delete")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deletePost(CommentDto comment, HttpServletRequest request) {
        // 응답용 데이터 생성
        Map<String, Object> response = new HashMap<>();

        // 세션에서 로그인된 사용자 정보 가져오기
        String userId = (String) request.getSession().getAttribute("userId");

        // 댓글 정보
        CommentDto existsComment = commentService.read(comment.getId());

        // 사용자 세션 아이디와 댓글 등록한 사용자 아이디 비교
        if (!userId.equals(existsComment.getUserId())) {
            response.put("result", "error");
        } else {
            // 댓글 삭제 처리
            boolean deleted = commentService.delete(comment.getId());

            if (deleted) {
                response.put("result", "ok");
            } else {
                response.put("result", "error");
            }
        }

        // JSON 형식으로 결과 반환
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }
}
