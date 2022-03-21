package com.misaka.service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.util.concurrent.RateLimiter;
import com.misaka.service.vo.MatchVO;
import com.misaka.service.vo.Result;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
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
@SuppressWarnings("UnstableApiUsage")
public class MatchController {

  private final OkHttpClient client = new OkHttpClient.Builder()
      .proxy(new Proxy(Type.SOCKS, new InetSocketAddress(20170)))
      .build();
  private final ObjectMapper objectMapper;

  private final Cache<Long, MatchVO> matchCache = CacheBuilder.newBuilder()
      .initialCapacity(100)
      .maximumSize(70000)
      .expireAfterWrite(7, TimeUnit.DAYS)
      .build();

  private final RateLimiter secondLimiter = RateLimiter.create(20);
  private final RateLimiter minuteLimiter = RateLimiter.create(250 / 60.0);
  private final RateLimiter hourLimiter = RateLimiter.create(2000 / (60.0 * 60));
  private final RateLimiter dayLimiter = RateLimiter.create(10000 / (24.0 * 60 * 60));

  @Value("${stratz.token}")
  private String token;

  @GetMapping("{id}")
  public Result<MatchVO> getMatch(@PathVariable Long id) throws ExecutionException {
    return Result.success(
        matchCache.get(id, () -> {
          if (secondLimiter.tryAcquire() && minuteLimiter.tryAcquire() && hourLimiter.tryAcquire()
              && dayLimiter.tryAcquire()) {
            Request request = new Builder()
                .header("authorization", this.token)
                .url("https://api.stratz.com/api/v1/match/" + id).build();
            try (Response response = client.newCall(request).execute()) {
              return objectMapper.readValue(Objects.requireNonNull(response.body()).string(), MatchVO.class);
            }
          }
          throw new Exception("请求超过API限制");
        })
    );
  }

}
