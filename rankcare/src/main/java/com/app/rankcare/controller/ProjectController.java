package com.app.rankcare.controller;

import com.app.rankcare.model.Project;
import com.app.rankcare.model.Site;
import com.app.rankcare.model.User;
import com.app.rankcare.payload.ApiResponse;
import com.app.rankcare.payload.ProjectRequest;
import com.app.rankcare.payload.SiteResponse;
import com.app.rankcare.repository.ProjectRepository;
import com.app.rankcare.repository.UserRepository;
import com.app.rankcare.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/api")
public class ProjectController {
    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/projects")
    @PreAuthorize("hasRole('CLIENT') or hasRole('ADMIN')")
    public Map<String, Object> getProjects(
            @RequestHeader("Authorization") String bearerToken,
            @RequestParam(name = "page", defaultValue = "0") Integer pageNo,
            @RequestParam(name = "count", defaultValue = "10") Integer pageSize
    ) {
        String authToken = bearerToken.replace("Bearer ", "");
        Long userId = tokenProvider.getUserIdFromJWT(authToken);

        Optional<User> userOptional = userRepository.findById(userId);

        Map<String, Object> resMap = new HashMap<>();

        Pageable pagination = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Order.asc("projectName")));
        resMap.put("pageCnt", 0);
        Page<Project> pgLst;

        if (userOptional.isPresent() && userOptional.get().isAdmin()) {
            pgLst = projectRepository.findAll(pagination);
        } else {
            pgLst = projectRepository.findUserCreatedProjects(userId, pagination);
        }

        if (pgLst.hasContent()) {
            resMap.put("pageCnt", pgLst.getTotalPages());
            resMap.put("data", pgLst.getContent());
        }

        return resMap;
    }

    @GetMapping("/projects/search")
    @PreAuthorize("hasRole('CLIENT') or hasRole('ADMIN')")
    public List<Project> getProjects(
            @RequestHeader("Authorization") String bearerToken,
            @RequestParam(name = "query", defaultValue = "") String query
    ) {
        String authToken = bearerToken.replace("Bearer ", "");
        Long userId = tokenProvider.getUserIdFromJWT(authToken);

        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isPresent() && userOptional.get().isAdmin()) {
            return projectRepository.searchProjectsAdmin(query);
        } else {
            return projectRepository.searchProjects(userId, query);
        }
    }

    @PostMapping("/project/add")
    @PreAuthorize("hasRole('CLIENT') or hasRole('ADMIN')")
    public ResponseEntity<Object> createProject(@RequestHeader("Authorization") String bearerToken, @Valid @RequestBody ProjectRequest projectRequest) {
        String authToken = bearerToken.replace("Bearer ", "");
        Long userId = tokenProvider.getUserIdFromJWT(authToken);

        projectRepository.save(new Project(projectRequest.getProjectName(), userId));

        return new ResponseEntity<>(new ApiResponse(true, "Project created successfully"), HttpStatus.OK);
    }

    @PostMapping("/project/update")
    @PreAuthorize("hasRole('CLIENT') or hasRole('ADMIN')")
    public ResponseEntity<Object> updateProject(@RequestHeader("Authorization") String bearerToken, @Valid @RequestBody ProjectRequest projectRequest) {
        String authToken = bearerToken.replace("Bearer ", "");
        Long userId = tokenProvider.getUserIdFromJWT(authToken);

        try {
            Project project = projectRepository.getOne(projectRequest.getId());
            Optional<User> userOptional = userRepository.findById(userId);

            if (!userOptional.isPresent() || (!userOptional.get().isAdmin() && !project.getCreatedByUser().equals(userId))) {
                return new ResponseEntity<>(new ApiResponse(false, "You don't have access to this project"), HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(new ApiResponse(false, "You don't have access to this project"), HttpStatus.UNAUTHORIZED);
        }

        projectRepository.save(new Project(projectRequest.getId(), projectRequest.getProjectName(), userId));

        return new ResponseEntity<>(new ApiResponse(true, "Project updated successfully"), HttpStatus.OK);
    }

    @DeleteMapping("/project/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> deleteById(@RequestHeader("Authorization") String bearerToken, @PathVariable("id") Long id) {
        String authToken = bearerToken.replace("Bearer ", "");
        Long userId = tokenProvider.getUserIdFromJWT(authToken);

        try {
            Project project = projectRepository.getOne(id);
            Optional<User> userOptional = userRepository.findById(userId);

            if (!userOptional.isPresent() || (!userOptional.get().isAdmin() && !project.getCreatedByUser().equals(userId))) {
                return new ResponseEntity<>(new ApiResponse(false, "You don't have access to this project"), HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(new ApiResponse(false, "You don't have access to this project"), HttpStatus.UNAUTHORIZED);
        }

        projectRepository.deleteById(id);

        return new ResponseEntity<>(new ApiResponse(true, "Project deleted successfully!"), HttpStatus.OK);
    }
}
