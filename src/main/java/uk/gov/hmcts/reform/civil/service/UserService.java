package uk.gov.hmcts.reform.civil.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.civil.utils.MaskHelper;
import uk.gov.hmcts.reform.idam.client.IdamClient;
import uk.gov.hmcts.reform.idam.client.models.UserDetails;
import uk.gov.hmcts.reform.idam.client.models.UserInfo;

@Service
public class UserService {

    private final IdamClient idamClient;

    @Autowired
    public UserService(IdamClient idamClient) {
        this.idamClient = idamClient;
    }

    @Cacheable(value = "userInfoCache")
    public UserInfo getUserInfo(String bearerToken) {
        return idamClient.getUserInfo(bearerToken);
    }

    @Cacheable(value = "accessTokenCache")
    public String getAccessToken(String username, String password) {
        return idamClient.getAccessToken(username, password);
    }

    public UserDetails getUserDetails(String authorisation) {
        try {
            return idamClient.getUserDetails(authorisation);
        } catch (Exception e) {
            String maskedError = MaskHelper.maskEmailsInErrorMessages(e.getMessage());
            throw new IllegalArgumentException(maskedError);
        }
    }
}
