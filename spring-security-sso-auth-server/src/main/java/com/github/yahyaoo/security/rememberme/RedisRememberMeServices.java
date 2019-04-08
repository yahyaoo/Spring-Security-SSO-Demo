/*
 *  Copyright 2019 Yahyaoo.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.github.yahyaoo.security.rememberme;

import com.github.yahyaoo.entity.po.DemoUser;
import com.github.yahyaoo.security.token.RedisRememberMeToken;
import com.github.yahyaoo.service.DemoUserDetailsService;
import lombok.Setter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices;
import org.springframework.security.web.authentication.rememberme.CookieTheftException;
import org.springframework.security.web.authentication.rememberme.InvalidCookieException;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationException;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;

/**
 * {@link RememberMeServices} implementation based on Barry Jaspan's <a
 * href="http://jaspan.com/improved_persistent_login_cookie_best_practice">Improved
 * Persistent Login Cookie Best Practice</a>.
 * <p>
 * There is a slight modification to the described approach, in that the username is not
 * stored as part of the cookie but obtained from the persistent store via an
 * implementation of {@link RedisTokenRepository}. The latter should place a unique
 * constraint on the series identifier, so that it is impossible for the same identifier
 * to be allocated to two different users.
 *
 * <p>
 * User management such as changing passwords, removing users and setting user status
 * should be combined with maintenance of the user's persistent tokens.
 * </p>
 *
 * <p>
 * Note that while this class will use the date a token was created to check whether a
 * presented cookie is older than the configured <tt>tokenValiditySeconds</tt> property
 * and deny authentication in this case, it will not delete these tokens from storage. A
 * suitable batch process should be run periodically to remove expired tokens from the
 * database.
 * </p>
 *
 * @author yahyaoo
 * @date 2019/4/2 10:55
 */
@SuppressWarnings({"WeakerAccess", "UnusedAssignment"})
public class RedisRememberMeServices extends AbstractRememberMeServices {

    // ~ Static fields/initializers
    // =====================================================================================

    public static final int DEFAULT_TOKEN_LENGTH = 16;
    public static final int DEFAULT_SERIES_LENGTH = 16;

    // ~ Instance fields
    // =======================================================================================

    private RedisTokenRepository tokenRepository = new NullRedisTokenRepositoryImpl();
    private SecureRandom random;

    @Setter
    private int seriesLength = DEFAULT_SERIES_LENGTH;
    @Setter
    private int tokenLength = DEFAULT_TOKEN_LENGTH;

    // ~ Constructors
    // ===================================================================================================

    public RedisRememberMeServices(String key, UserDetailsService userDetailsService,
                                   RedisTokenRepository tokenRepository) {
        super(key, userDetailsService);
        this.random = new SecureRandom();
        this.tokenRepository = tokenRepository;
    }

    // ~ Methods
    // =====================================================================================================

    /**
     * Called from autoLogin to process the submitted persistent login cookie. Subclasses
     * should validate the cookie and perform any additional management required.
     *
     * @param cookieTokens the decoded and tokenized cookie value
     * @param request      the request
     * @param response     the response, to allow the cookie to be modified if required.
     * @return the UserDetails for the corresponding user account if the cookie was
     * validated successfully.
     * @throws RememberMeAuthenticationException if the cookie is invalid or the login is
     *                                           invalid for some other reason.
     * @throws UsernameNotFoundException         if the user account corresponding to the login
     *                                           cookie couldn't be found (for example if the user has been removed from the
     *                                           system).
     */
    @Override
    protected UserDetails processAutoLoginCookie(String[] cookieTokens, HttpServletRequest request, HttpServletResponse response) throws RememberMeAuthenticationException, UsernameNotFoundException {

        final int cookieTokensLength = 2;
        if (cookieTokens.length != cookieTokensLength) {
            throw new InvalidCookieException("Cookie token did not contain " + 2
                    + " tokens, but contained '" + Arrays.asList(cookieTokens) + "'");
        }

        final String presentedSeries = cookieTokens[0];
        final String presentedToken = cookieTokens[1];

        RedisRememberMeToken token = tokenRepository.getTokenForSeries(presentedSeries);

        if (token == null) {
            // No series match, so we can't authenticate using this cookie
            throw new RememberMeAuthenticationException(
                    "No persistent token found for series id: " + presentedSeries);
        }

        // We have a match for this user/series combination
        if (!presentedToken.equals(token.getTokenValue())) {
            // Token doesn't match series value. Delete all logins for this user and throw
            // an exception to warn them.
            tokenRepository.removeUserTokens(new RedisRememberMeToken(null, token.getMail(), null, null, null));

            throw new CookieTheftException(
                    messages.getMessage(
                            "PersistentTokenBasedRememberMeServices.cookieStolen",
                            "Invalid remember-me token (Series/token) mismatch. Implies previous cookie theft attack."));
        }

        final long seconds = 1000L;
        if (token.getDate().getTime() + getTokenValiditySeconds() * seconds < System
                .currentTimeMillis()) {
            throw new RememberMeAuthenticationException("Remember-me login has expired");
        }

        // Token also matches, so login is valid. Update the token value, keeping the
        // *same* series number.
        if (logger.isDebugEnabled()) {
            logger.debug("Refreshing persistent login token for user '"
                    + token.getMail() + "', series '" + token.getSeries() + "'");
        }

        RedisRememberMeToken newToken = new RedisRememberMeToken(token.getUserId(), token.getMail(), token.getSeries(),
                generateTokenData(), new Date());

        try {
            tokenRepository.updateToken(token, newToken);
            addCookie(newToken, request, response);
        } catch (Exception e) {
            logger.error("Failed to update token: ", e);
            throw new RememberMeAuthenticationException(
                    "Autologin failed due to data access problem");
        }

        UserDetailsService userDetailsService = getUserDetailsService();
        DemoUserDetailsService demoUserDetailsService = userDetailsService instanceof DemoUserDetailsService ?
                (DemoUserDetailsService) userDetailsService : null;

        UserDetails userDetails = null;
        if (demoUserDetailsService != null) {
            userDetails = demoUserDetailsService.loadUserByMail(token.getMail());
        }

        return userDetails;

    }

    /**
     * Creates a new persistent login token with a new series number, stores the data in
     * the persistent token repository and adds the corresponding cookie to the response.
     */
    @Override
    protected void onLoginSuccess(HttpServletRequest request, HttpServletResponse response, Authentication successfulAuthentication) {
        DemoUser principal = (DemoUser) successfulAuthentication.getPrincipal();

        if (logger.isDebugEnabled()) {
            logger.debug("Creating new persistent login for user principal " + principal.toString());
        }

        RedisRememberMeToken redisRememberMeToken = new RedisRememberMeToken(principal.getUserId(),
                principal.getUserMail(), generateSeriesData(), generateTokenData(), new Date());

        try {
            tokenRepository.createNewToken(redisRememberMeToken);
            addCookie(redisRememberMeToken, request, response);
        } catch (Exception e) {
            logger.error("Failed to save persistent token ", e);
        }
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        super.logout(request, response, authentication);

        String rememberMeCookie = extractRememberMeCookie(request);
        if (rememberMeCookie == null) {
            return;
        }
        if (rememberMeCookie.length() == 0) {
            logger.debug("Cookie was empty");
            cancelCookie(request, response);
            return;
        }
        String[] cookieTokens = decodeCookie(rememberMeCookie);

        if (authentication != null && authentication.getPrincipal() instanceof DemoUser) {
            DemoUser principal = (DemoUser) authentication.getPrincipal();
            tokenRepository.removeUserTokens(new RedisRememberMeToken(null, principal.getUserMail(),
                    cookieTokens[0], null, null));
        }

    }

    protected String generateSeriesData() {
        byte[] newSeries = new byte[seriesLength];
        random.nextBytes(newSeries);
        return new String(Base64.getEncoder().encode(newSeries));
    }

    protected String generateTokenData() {
        byte[] newToken = new byte[tokenLength];
        random.nextBytes(newToken);
        return new String(Base64.getEncoder().encode(newToken));
    }

    private void addCookie(RedisRememberMeToken token, HttpServletRequest request,
                           HttpServletResponse response) {
        setCookie(new String[]{token.getSeries(), token.getTokenValue()},
                getTokenValiditySeconds(), request, response);
    }

    @Override
    public void setTokenValiditySeconds(int tokenValiditySeconds) {
        Assert.isTrue(tokenValiditySeconds > 0, "tokenValiditySeconds must be positive for this implementation");
        super.setTokenValiditySeconds(tokenValiditySeconds);
    }

}
