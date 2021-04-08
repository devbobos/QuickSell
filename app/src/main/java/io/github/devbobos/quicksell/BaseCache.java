package io.github.devbobos.quicksell;

import android.content.Context;

import io.github.devbobos.quicksell.api.models.MarketInfo;
import io.github.devbobos.quicksell.constants.PreferenceKey;
import io.github.devbobos.quicksell.helper.Utils;

public class BaseCache {
    private static BaseCache instance;
    private String accessKey;
    private String accessKeyExpireDate;
    private String secretKey;
    private String jwtToken;
    private MarketInfo selectedMarket;

    private BaseCache() { }

    public static BaseCache getInstance() {
        if(instance == null){
            instance = new BaseCache();
        }
        return instance;
    }

    public String getAccessKey() {
        if(Utils.isEmpty(accessKey)){
            this.accessKey = Utils.get(PreferenceKey.USER_ACCESS_KEY.name(), null);
        }
        return accessKey;
    }

    public String getAccessKeyExpireDate() {
        if(Utils.isEmpty(accessKeyExpireDate)){
            this.accessKeyExpireDate = Utils.get(PreferenceKey.USER_ACCESS_KEY_EXPIRE_DATE.name(), null);
        }
        return accessKeyExpireDate;
    }

    public String getSecretKey() {
        if(Utils.isEmpty(secretKey)){
            this.secretKey = Utils.get(PreferenceKey.USER_SECRET_KEY.name(), null);
        }
        return secretKey;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public MarketInfo getSelectedMarket(Context context) {
        if(Utils.isNull(selectedMarket)){
            final String marketId = Utils.get(PreferenceKey.USER_SELECTED_MARKET_ID.name(), null);
            if(Utils.isEmpty(marketId)){
                return selectedMarket;
            }
            final String marketKoreanName = Utils.get(PreferenceKey.USER_SELECTED_MARKET_KOREAN_NAME.name(), null);
            final String marketEnglishName = Utils.get(PreferenceKey.USER_SELECTED_MARKET_ENGLISH_NAME.name(), null);
            this.selectedMarket = new MarketInfo(marketId, marketEnglishName, marketKoreanName, null);
        }
        return selectedMarket;
    }

    public void setSelectedMarket(MarketInfo selectedMarket) {
        this.selectedMarket = selectedMarket;
    }
}
