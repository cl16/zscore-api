package com.zscore_api.zscore_api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("")
public class MainController {

    @Autowired
    private GameRepository gameRepository;

    @GetMapping(path="/game/all")
    public @ResponseBody Iterable<Game> getAllGames() {
        return gameRepository.findAll();
    }
}
