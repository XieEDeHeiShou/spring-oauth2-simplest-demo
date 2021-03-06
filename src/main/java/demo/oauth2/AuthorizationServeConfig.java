package demo.oauth2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServeConfig extends AuthorizationServerConfigurerAdapter {
    // Unable to perform authorization if this is not configured
    // Throw Oauth2Exception when calling '/oauth2/token' if this is not configured
    // {
    //     "error": "unauthorized",
    //     "error_description": "Full authentication is required to access this resource"
    // }
    @NonNull
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthorizationServeConfig(@NonNull PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        super.configure(security);
        // Throw IllegalArgumentException when calling '/oauth/token' if this is not configured
        // IllegalArgumentException: "There is no PasswordEncoder mapped for the id "null"
        security.passwordEncoder(passwordEncoder);
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        super.configure(clients);
        clients.inMemory()
                .withClient("client")
                .secret(passwordEncoder.encode("secret"))
                // Throw InvalidScopeException when calling '/oauth/token' if 'scope' is not configured
                // InvalidScopeException: Empty scope (either the client or the user is not allowed the requested scopes)
                // {
                //     "error": "invalid_scope",
                //     "error_description": "Empty scope (either the client or the user is not allowed the requested scopes)"
                // }
                .scopes("all");
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        super.configure(endpoints);
    }
}