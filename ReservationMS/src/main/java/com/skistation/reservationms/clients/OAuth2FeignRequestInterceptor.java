package com.skistation.reservationms.clients;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;

public class OAuth2FeignRequestInterceptor implements RequestInterceptor {
        
    private static final Logger logger = LoggerFactory.getLogger(OAuth2FeignRequestInterceptor.class);
            
    private final OAuth2AuthorizedClientManager manager;
    private static final String ANONYMOUS_PRINCIPAL = "anonymous";
            
    public OAuth2FeignRequestInterceptor(OAuth2AuthorizedClientManager manager) {
        this.manager = manager;
    }
            
    @Override
    public void apply(RequestTemplate template) {
        try {
            // Create an anonymous authentication for client credentials flow
            AnonymousAuthenticationToken anonymousToken = new AnonymousAuthenticationToken(
                    "key", 
                    ANONYMOUS_PRINCIPAL, 
                    AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS")
            );
            
            logger.debug("Intentando obtener token OAuth2 para client registration: ms-reservation");
            var client = manager.authorize(
                    OAuth2AuthorizeRequest.withClientRegistrationId("ms-reservation")
                            .principal(anonymousToken)
                            .build()
            );
            
            if (client != null && client.getAccessToken() != null) {
                String tokenValue = client.getAccessToken().getTokenValue();
                logger.info("Token OAuth2 obtenido exitosamente. Primeros caracteres: {}", 
                        tokenValue.substring(0, Math.min(20, tokenValue.length())) + "..."); 
                template.header("Authorization", "Bearer " + tokenValue);
                logger.debug("Header Authorization añadido a la petición Feign");
            } else {
                logger.error("No se pudo obtener el token OAuth2. Client es null o no tiene access token");
            }
        } catch (Exception e) {
            logger.error("Error al obtener token OAuth2 para Feign: {}", e.getMessage(), e);
        }
    }
}
