package com.misaka.service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.misaka.service.vo.MatchVO;
import com.misaka.service.vo.Result;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/match")
public class MatchController {

  private final OkHttpClient client = new OkHttpClient.Builder().build();
  private final ObjectMapper objectMapper;

  private final Cache<Long, MatchVO> matchCache = CacheBuilder.newBuilder()
      .initialCapacity(10)
      .maximumSize(100)
      .expireAfterWrite(7, TimeUnit.DAYS)
      .build();

  @Value("${stratz.token}")
  private String token;

  @GetMapping("{id}")
  public Result<MatchVO> getMatch(@PathVariable Long id) throws ExecutionException {
    return Result.success(
        matchCache.get(id, () -> {
          Request request = new Builder()
              .header("authorization",this.token)
              .url("https://api.stratz.com/api/v1/match/" + id).build();
          try (Response response = client.newCall(request).execute()) {
            return objectMapper.readValue(Objects.requireNonNull(response.body()).string(), MatchVO.class);
          }
        })
    );
  }

//  @PostConstruct
//  public void test() throws ExecutionException {
//    getMatch(6466445129L);
//  }

}
