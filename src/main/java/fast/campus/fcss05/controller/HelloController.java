package fast.campus.fcss05.controller;

import org.springframework.scheduling.annotation.Async;
import org.springframework.security.concurrent.DelegatingSecurityContextCallable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
public class HelloController {

    @GetMapping("/api/v1/hello")
    public String hello(Authentication authentication) {
        return "hello, " + authentication.getName();
    }

    @GetMapping("/api/v2/hello")
    @Async
    public void helloV2() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        String username = securityContext.getAuthentication().getName();
        System.out.println("hello, " + username);
    }

    @GetMapping("/api/v3/hello")
    public String helloV3() throws ExecutionException, InterruptedException {
        Callable<String> task = () -> {
            SecurityContext context = SecurityContextHolder.getContext();
            return context.getAuthentication().getName();
        };

        ExecutorService executorService = Executors.newCachedThreadPool();
        try {
            DelegatingSecurityContextCallable<String> callable = new DelegatingSecurityContextCallable<>(task);
            return "hello v3, " + executorService.submit(callable).get();
        } finally {
            executorService.shutdown();
        }
    }
}
