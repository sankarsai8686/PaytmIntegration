package com.paytm.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

//@ConfigurationProperties("paytm.payment.sandbox")
@ConfigurationProperties("paytm.payment.sandbox")
@Component
//@ToString
//@Getter
//@Setter
//@Data
public class Bean {
	
	private String merchantId;

    private String merchantKey;

    private String channelId;

    private String website;

    private String industryTypeId;

    private String paytmUrl;
    
    private String callBackUrl;

    private Map<String, String> details;

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getMerchantKey() {
		return merchantKey;
	}

	public void setMerchantKey(String merchantKey) {
		this.merchantKey = merchantKey;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getIndustryTypeId() {
		return industryTypeId;
	}

	public void setIndustryTypeId(String industryTypeId) {
		this.industryTypeId = industryTypeId;
	}

	public String getPaytmUrl() {
		return paytmUrl;
	}

	public void setPaytmUrl(String paytmUrl) {
		this.paytmUrl = paytmUrl;
	}

	public Map<String, String> getDetails() {
		return details;
	}

	public void setDetails(Map<String, String> details) {
		this.details = details;
	}
	

	public String getCallBackUrl() {
		return callBackUrl;
	}

	public void setCallBackUrl(String callBackUrl) {
		this.callBackUrl = callBackUrl;
	}

	@Override
	public String toString() {
		return "Bean [merchantId=" + merchantId + ", merchantKey=" + merchantKey + ", channelId=" + channelId
				+ ", website=" + website + ", industryTypeId=" + industryTypeId + ", paytmUrl=" + paytmUrl
				+ ", callBackUrl=" + callBackUrl + ", details=" + details + "]";
	}
	
    
    
    
    
//	private final Data data = new Data();
//	public Data getData() {
//		return data;
//	}
	
	
	
	
	
//	@Override
//	public String toString() {
//		return "Bean [merchantId=" + merchantId + ", data=" + data + "]";
//	}




//	public static class Data
//	{
//		String data;
//
//		
//
//		public String getData() {
//			return data;
//		}
//
//
//
//		public void setData(String data) {
//			this.data = data;
//		}
//
//
//
//		@Override
//		public String toString() {
//			return "Data [data=" + data + "]";
//		}
//		
//		
//	}

}
