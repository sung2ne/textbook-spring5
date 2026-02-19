package com.example.spring.post;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * 게시글 관련 요청을 처리하는 웹 컨트롤러 클래스
 * 사용자 요청을 받아 서비스 계층과 연결하고 뷰로 데이터를 전달함
 */
@Controller // Spring MVC에서 이 클래스가 컨트롤러임을 명시
public class PostController {

    @Autowired // PostService 객체를 자동으로 주입
    PostService postService;

    /**
     * 게시글 목록 화면 요청 처리 (GET 방식)
     * @param model 뷰에 데이터를 전달하기 위한 객체
     * @return "posts/list" 뷰 이름 (posts/list.jsp)
     */
    @RequestMapping(value = "/posts", method = RequestMethod.GET)
    public String listGet(Model model) {
        // 서비스 계층을 통해 게시글 목록을 가져옴
        List<PostDto> posts = postService.list();

        // "posts"라는 이름으로 게시글 목록 데이터를 모델에 담아 뷰로 전달
        model.addAttribute("posts", posts);

        // posts/list.jsp 의 화면을 렌더링
        return "posts/list";
    }

    /**
     * 게시글 등록 화면 요청 처리 (GET 방식)
     * 사용자가 글을 작성할 수 있는 입력 폼 화면을 보여줌
     * @return "post/create" 뷰 이름 (예: post/create.jsp)
     */
    @RequestMapping(value = "/posts/create", method = RequestMethod.GET)
    public String createGet() {
        // 단순히 글쓰기 화면만 보여주는 기능이므로 별도의 데이터 전달 없음
        return "posts/create";
    }

    /**
     * 게시글 등록 요청 처리 (POST 방식)
     * @param post 사용자가 작성한 게시글 정보(PostDto)
     * @param redirectAttributes 리다이렉트 시 전달할 메시지를 담는 객체
     * @return 등록 성공 시 글 보기로 리다이렉트, 실패 시 글쓰기 화면으로 이동
     */
    @RequestMapping(value = "/posts/create", method = RequestMethod.POST)
    public String createPost(PostDto post, RedirectAttributes redirectAttributes) {
        int createdId = postService.create(post);

        if (createdId > 0) {
            redirectAttributes.addFlashAttribute("successMessage", "게시글이 등록되었습니다.");
            return "redirect:/posts/" + createdId;
        }

        redirectAttributes.addFlashAttribute("errorMessage", "게시글 등록에 실패했습니다.");
        return "redirect:/posts/create";
    }

    /**
     * 게시글 상세보기 요청 처리 (GET 방식)
     * @param id 상세 조회할 게시글 ID
     * @param model 뷰에 전달할 게시글 데이터를 담는 객체
     * @return 상세보기 화면 뷰 이름 ("post/read.jsp")
     */
    @RequestMapping(value = "/posts/{id}", method = RequestMethod.GET)
    public String readGet(@PathVariable("id") int id, Model model) {
        // 서비스 계층을 통해 게시글 ID에 해당하는 게시글 데이터 조회
        PostDto post = postService.read(id);

        // 조회한 게시글 데이터를 모델에 담아 뷰로 전달
        model.addAttribute("post", post);

        // 게시글 상세보기 화면 렌더링
        return "posts/read";
    }

    /**
     * 게시글 수정 화면 요청 처리 (GET 방식)
     * @param id 수정할 게시글의 ID
     * @param model 수정할 게시글 데이터를 뷰로 전달하기 위한 모델 객체
     * @return "post/update" 뷰 이름 (예: post/update.jsp)
     */
    @RequestMapping(value = "/posts/{id}/update", method = RequestMethod.GET)
    public String updateGet(@PathVariable("id") int id, Model model) {
        PostDto post = postService.read(id);
        model.addAttribute("post", post);
        return "posts/update";
    }

    /**
     * 게시글 수정 요청 처리 (POST 방식)
     * @param id 수정할 게시글 ID
     * @param post 수정된 게시글 정보 (비밀번호 포함)
     * @param redirectAttributes 결과 메시지 전달용 객체
     * @return 수정 성공 시 상세 페이지로, 실패 시 수정 페이지로 리다이렉트
     */
    @RequestMapping(value = "/posts/{id}/update", method = RequestMethod.POST)
    public String updatePost(@PathVariable("id") int id, PostDto post, RedirectAttributes redirectAttributes) {
        // URL 경로에서 받은 ID를 post 객체에 설정
        post.setId(id);

        // 게시글 수정 처리
        if (postService.update(post)) {
            redirectAttributes.addFlashAttribute("successMessage", "게시글이 수정되었습니다.");
            return "redirect:/posts/" + id;
        }

        // 실패 시 메시지 전달 후 수정 페이지로 이동
        redirectAttributes.addFlashAttribute("errorMessage", "게시글 수정에 실패했습니다. (비밀번호 확인)");
        return "redirect:/posts/" + id + "/update";
    }

    /**
     * 게시글 삭제 요청 처리 (POST 방식)
     */
    @RequestMapping(value = "/posts/{id}/delete", method = RequestMethod.POST)
    public String deletePost(@PathVariable("id") int id, PostDto post, RedirectAttributes redirectAttributes) {
        post.setId(id);
        boolean deleted = postService.delete(post);

        if (deleted) {
            redirectAttributes.addFlashAttribute("successMessage", "게시글이 삭제되었습니다.");
            return "redirect:/posts";
        }

        redirectAttributes.addFlashAttribute("errorMessage", "게시글 삭제에 실패했습니다. (비밀번호 확인)");
        return "redirect:/posts/" + id;
    }
}
