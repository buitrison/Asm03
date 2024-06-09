package com.example.demo.restcontrollers;

import com.example.demo.dtos.response.DataResponse;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.models.Post;
import com.example.demo.models.Specialization;
import com.example.demo.service.PostService;
import com.example.demo.service.SpecializationService;
import org.hibernate.transform.ResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@RestController
@RequestMapping("${api.prefix}/posts")
public class PostController {
    @Autowired
    private PostService postService;

    @Autowired
    private SpecializationService specializationService;

    // lấy tất cả
    @GetMapping("")
    public ResponseEntity<?> findAll() {
        List<Post> posts = postService.findAll();
        return ResponseEntity.ok(
                new DataResponse(
                        HttpStatus.OK.value(),
                        "all post",
                        new ArrayList<>() {{
                            for (Post post : posts) {
                                add(
                                        new LinkedHashMap<>() {{
                                            put("id", post.getId());
                                            put("title", post.getTitle());
//                                            put("content", post.getContentMarkdown());
                                            put("category", post.getCategory());
                                            put("specialization", post.getSpecialization());
                                            put("clinic", post.getClinic());
                                            put("fee", post.getFee());
                                        }}
                                );
                            }
                        }}

                )
        );
    }

    // tìm kiếm chung và tìm kiếm theo chuyên khoa của bác sĩ
    @PostMapping("/find")
    public ResponseEntity<?> findPostBySpecialization(@RequestParam("keyword") String keyword,
                                                      @RequestParam(value = "specialization", required = false) Long specializationId,
                                                      @RequestParam(value = "region", required = false) String region,
                                                      @RequestParam(value = "category", required = false) String category,
                                                      @RequestParam(value = "minPrice", required = false) Long minPrice,
                                                      @RequestParam(value = "maxPrice", required = false) Long maxPrice,
                                                      @RequestParam(value = "clinic", required = false) String clinic){
        if(region != null && region.trim().isEmpty()){
            region = null;
        }
        if(category != null && category.trim().isEmpty()){
            category = null;
        }
        if(clinic != null && clinic.trim().isEmpty()){
            clinic = null;
        }

        List<Post> posts;

        // tìm kiếm theo chuyên khoa nếu specialization id không null
        // tìm kiếm chung nếu null
        if(specializationId != null) {
            Specialization specialization = specializationService.findById(specializationId);
            posts = postService.findBySpecialization(specialization.getName(), keyword);
        } else {
            posts = postService.findByRegionCategoryPriceClinic(region, category, minPrice, maxPrice, clinic, keyword);
        }
        if(posts.isEmpty()){
            throw new NotFoundException("result is empty");
        }
        return ResponseEntity.ok(
                new ArrayList<>(){{
                    for(Post post: posts){
                        add(
                                new LinkedHashMap<>(){{
                                    put("id", post.getId());
                                    put("title", post.getTitle());
                                    put("category", post.getCategory() == null? null : post.getCategory().getName());
                                    put("specialization", post.getSpecialization() == null? null : post.getSpecialization().getName());
                                    put("clinic", post.getClinic()== null? null : post.getClinic().getName());
                                    put("address", post.getAddress());
                                    put("fee", post.getFee());
                                }}
                        );
                    }
                }}
        );
    }
}
