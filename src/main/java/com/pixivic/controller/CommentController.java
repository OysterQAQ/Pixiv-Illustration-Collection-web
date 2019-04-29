package com.pixivic.controller;

import com.pixivic.model.Comment;
import com.pixivic.model.Result;
import com.pixivic.service.CommentService;
import com.pixivic.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.mail.MessagingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CommentController {
    private final CommentService commentService;
    private final EmailService emailService;

    @PostMapping
    public Mono<ResponseEntity<Result<Comment>>> pushComment(@RequestBody Comment comment) {
        return commentService.findById(comment.getPid()).flatMap(c -> {
            if (c != null) {
                try {
                    commentService.init(comment);
                    String url = "https://pixivic.com/comments?id=" + c.getId() + "&user=" + URLEncoder.encode(c.getUser(), StandardCharsets.UTF_8) + "&email=" + URLEncoder.encode(c.getEmail(), StandardCharsets.UTF_8);
                    return commentService.push(comment).zipWith(emailService.sendEmail(c.getEmail(), c.getUser(), comment.getUser(), comment.getContent(), url)).map(c1 -> ResponseEntity.ok(new Result<>("评论提交成功")));
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
            return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Result<>("回复用户不存在")));
        });
    }

    @GetMapping
    public Mono<ResponseEntity<Result<List<Comment>>>> pullComments() throws MessagingException {
        return commentService.pull().map(comments -> ResponseEntity.ok(new Result<>("拉取评论成功", comments)));
    }

}
