package io.github.devbobos.quicksell;

import android.content.Context;

import io.github.devbobos.quicksell.api.models.MarketInfo;
import io.github.devbobos.quicksell.api.models.MarketOrderInfo;
import io.github.devbobos.quicksell.constants.PreferenceKey;
import io.github.devbobos.quicksell.helper.security.AES256;
import io.github.devbobos.quicksell.helper.utils.Utils;

public class ApplicationCache {
    private static ApplicationCache instance;
    private String accessKey;
    private String accessKeyExpireDate;
    private String secretKey;
    private String jwtToken;
    private MarketInfo selectedMarket;
    private MarketOrderInfo selectedMarketOrderInfo;

    private ApplicationCache() { }

    public static ApplicationCache getInstance() {
        if(instance == null){
            instance = new ApplicationCache();
        }
        return instance;
    }

    public String getAccessKey() {
        if(Utils.isEmpty(accessKey)){
            AES256 decypter = new AES256();
            final String encryptedData = Utils.get(PreferenceKey.STRING_USER_ACCESS_KEY.name(), null);
            if(Utils.hasText(encryptedData)){
                this.accessKey = decypter.decrypt(encryptedData);
            }
        }
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getAccessKeyExpireDate() {
        if(Utils.isEmpty(accessKeyExpireDate)){
            this.accessKeyExpireDate = Utils.get(PreferenceKey.STRING_USER_ACCESS_KEY_EXPIRE_DATE.name(), null);
        }
        return accessKeyExpireDate;
    }
    public void setAccessKeyExpireDate(String accessKeyExpireDate) {
        this.accessKeyExpireDate = accessKeyExpireDate;
    }

    public String getSecretKey() {
        if(Utils.isEmpty(secretKey)){
            AES256 decypter = new AES256();
            final String encryptedData = Utils.get(PreferenceKey.STRING_USER_SECRET_KEY.name(), null);
            if(Utils.hasText(encryptedData)){
                this.secretKey = decypter.decrypt(encryptedData);
            }
        }
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public MarketInfo getSelectedMarket(Context context) {
        if(Utils.isNull(selectedMarket)){
            final String marketId = Utils.get(PreferenceKey.STRING_USER_SELECTED_MARKET_ID.name(), null);
            if(Utils.isEmpty(marketId)){
                return selectedMarket;
            }
            final String marketKoreanName = Utils.get(PreferenceKey.STRING_USER_SELECTED_MARKET_KOREAN_NAME.name(), null);
            final String marketEnglishName = Utils.get(PreferenceKey.STRING_USER_SELECTED_MARKET_ENGLISH_NAME.name(), null);
            this.selectedMarket = new MarketInfo(marketId, marketEnglishName, marketKoreanName, null);
        }
        return selectedMarket;
    }

    public void setSelectedMarket(MarketInfo selectedMarket) {
        this.selectedMarket = selectedMarket;
    }

    public MarketInfo getSelectedMarket() {
        return selectedMarket;
    }

    public MarketOrderInfo getSelectedMarketOrderInfo() {
        return selectedMarketOrderInfo;
    }

    public void setSelectedMarketOrderInfo(MarketOrderInfo selectedMarketOrderInfo) {
        this.selectedMarketOrderInfo = selectedMarketOrderInfo;
    }
}
