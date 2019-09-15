package com.moqi.community01.controller;

import com.moqi.community01.dto.AccessTokenDTO;
import com.moqi.community01.dto.GitHubUser;
import com.moqi.community01.model.User;
import com.moqi.community01.provider.GitHubProvider;
import com.moqi.community01.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
public class AuthorizeController {
    @Autowired
    private GitHubProvider gitHubprovider;
    @Autowired
    private UserService userService;

    @Value("${github.client.id}")
    private String id;
    @Value("${github.client.secret}")
    private String secret;
    @Value("${github.client.uri}")
    private String uri;
    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state")String state,
                           HttpServletRequest request,
                           HttpServletResponse response) {
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id(id);
        accessTokenDTO.setClient_secret(secret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(uri);
        accessTokenDTO.setState(state);
        String accessToken = gitHubprovider.getAccessToken(accessTokenDTO);
        GitHubUser gitHubUser = gitHubprovider.getUser(accessToken);
        if (gitHubUser != null) {
            User user = new User();
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setName(gitHubUser.getName());
            user.setAccountId(String.valueOf(gitHubUser.getId()));
            user.setAvatarUrl(gitHubUser.getAvatarUrl());
            userService.createOrUpdate(user);
            response.addCookie(new Cookie("token", token));
            request.getSession().setAttribute("user", user);
            return "redirect:/";
        } else {
            return "redirect:/";
        }
    }
    @GetMapping("/logout")
    public String logout(HttpServletRequest request,
                        HttpServletResponse response) {
        request.getSession().removeAttribute("user");
        Cookie cookie = new Cookie("token",null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/";
    }
}
