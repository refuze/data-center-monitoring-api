package com.example.datacentermonitoringapi.configuration.websocket;

import com.example.datacentermonitoringapi.configuration.exception.HttpRuntimeException;
import com.example.datacentermonitoringapi.domain.security.JwtService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class JwtAuthenticationInterceptor implements ChannelInterceptor {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    public Message<?> preSend(@NotNull Message<?> message,
                              @NotNull MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        if (SimpMessageType.CONNECT.equals(accessor.getMessageType())) {
            String authHeader = accessor.getFirstNativeHeader(HttpHeaders.AUTHORIZATION);
            final String jwt;
            final String username;

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                throw new HttpRuntimeException("Invalid token or token not found.", HttpStatus.BAD_REQUEST);
            }

            jwt = authHeader.substring(7);
            username = jwtService.extractUsername(jwt);

            if (username == null || SecurityContextHolder.getContext().getAuthentication() != null) {
                throw new HttpRuntimeException("Invalid token or user already authenticated.", HttpStatus.BAD_REQUEST);
            }

            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            if (jwtService.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            } else {
                throw new HttpRuntimeException("Invalid token.", HttpStatus.BAD_REQUEST);
            }
        } else if (SimpMessageType.MESSAGE.equals(accessor.getMessageType())) {
            throw new HttpRuntimeException("MESSAGE is prohibited by server.", HttpStatus.FORBIDDEN);
        }
        return message;
    }
}