package com.blogapi.blogappapis.services.impl;

import com.blogapi.blogappapis.entities.Category;
import com.blogapi.blogappapis.entities.Post;
import com.blogapi.blogappapis.entities.User;
import com.blogapi.blogappapis.exceptions.ResourceNotFoundException;
import com.blogapi.blogappapis.payloads.PostDto;
import com.blogapi.blogappapis.payloads.PostResponse;
import com.blogapi.blogappapis.repositories.CategoryRepo;
import com.blogapi.blogappapis.repositories.PostRepo;
import com.blogapi.blogappapis.repositories.UserRepo;
import com.blogapi.blogappapis.services.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private CategoryRepo categoryRepo;
    @Autowired
    private PostRepo postRepo;

    @Override
    public PostDto createPost(PostDto postDto,Integer userId,Integer categoryId) {

        User user  = this.userRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("User","User Id",userId));

        Category category  = this.categoryRepo.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category","Category Id",categoryId));

        Post post = this.modelMapper.map(postDto,Post.class);
        post.setImageName("default.png");
        post.setAddedDate(new Date());
        post.setUser(user);
        post.setCategory(category);

        Post newPost = this.postRepo.save(post);

        return this.modelMapper.map(newPost,PostDto.class);
    }

    @Override
    public PostDto updatePost(PostDto postDto, Integer postId) {
        Post post = this.postRepo.findById(postId).orElseThrow(() ->new ResourceNotFoundException("Post","Post Id",postId));

        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setImageName(postDto.getImageName());

        Post updatedPost = this.postRepo.save(post);
        return this.modelMapper.map(updatedPost,PostDto.class);
    }

    @Override
    public void deletePost(Integer postId) {
        Post post = this.postRepo.findById(postId).orElseThrow(() ->new ResourceNotFoundException("Post","Post Id",postId));
        this.postRepo.delete(post);
    }

    @Override
    public PostResponse getAllPosts(Integer pageNumber, Integer pageSize,String sortBy,String sortDir) {
         Pageable p= PageRequest.of(pageNumber,pageSize,
                 (sortDir.equalsIgnoreCase("desc")) ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending()
                                   );
        Page<Post> pagePost = this.postRepo.findAll(p);
        List<Post> content = pagePost.getContent();

        List<PostDto> postDtos =content.stream().map((allPost) -> this.modelMapper.map(allPost,PostDto.class))
                .collect(Collectors.toList());

        PostResponse postResponse = new PostResponse();

        postResponse.setContent(postDtos);
        postResponse.setPageNumber(pagePost.getNumber());
        postResponse.setPageSize(pagePost.getSize());
        postResponse.setTotalPages(pagePost.getTotalPages());
        postResponse.setTotalElements(pagePost.getTotalElements());
        postResponse.setLastPage(pagePost.isLast());

        return postResponse;
    }

    @Override
    public PostDto getPostById(Integer postId) {
        Post post = this.postRepo.findById(postId).orElseThrow(() ->new ResourceNotFoundException("Post","Post Id",postId));

        return this.modelMapper.map(post,PostDto.class);
    }

    @Override
    public List<PostDto> getPostsByCategory(Integer categoryId) {

        Category category = this.categoryRepo.findById(categoryId).orElseThrow(() ->new ResourceNotFoundException("Category","Category Id",categoryId));
        List<Post> posts = this.postRepo.findByCategory(category);

        return posts.stream().map((post) -> this.modelMapper.map(post,PostDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<PostDto> getPostsByUser(Integer userId) {
        User user = this.userRepo.findById(userId).orElseThrow(() ->new ResourceNotFoundException("User","User Id",userId));
        List<Post> posts = this.postRepo.findByUser(user);

        return posts.stream().map((post) -> this.modelMapper.map(post,PostDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<PostDto> searchPosts(String keyword) {
        List<Post> posts = this.postRepo.searchByTitle("%"+keyword+"%");
        return posts.stream().map((post) -> this.modelMapper.map(post,PostDto.class))
                .collect(Collectors.toList());
    }
}
