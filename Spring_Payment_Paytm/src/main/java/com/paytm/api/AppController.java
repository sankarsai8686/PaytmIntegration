package com.paytm.api;

import java.io.IOException;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.paytm.api.util.Util;
import com.paytm.pg.merchant.CheckSumServiceHelper;

import lombok.Getter;

@Controller
public class AppController {

	@Autowired
	private Bean bean;
	
	@Autowired
	private Util util;
	
	@Autowired
	private Environment env;
	
	@GetMapping("/")
	//@ResponseBody
	public String msg()
	{
		//System.out.println("bean : "+bean);
		//System.out.println("bean : "+bean.toString());
		return "home";
	}
	
	
	@PostMapping("/pgredirect")
	public ModelAndView pgredirect(HttpServletRequest request,@RequestParam String ORDER_ID,@RequestParam String CUST_ID,@RequestParam String TXN_AMOUNT ) throws Exception
	{
		System.out.println("pgredirect : "+ORDER_ID+ " : "+TXN_AMOUNT+" : "+CUST_ID);
		
		/* initialize a TreeMap object */
		TreeMap<String, String> paytmParams = new TreeMap<String, String>();
		

		/* Enter your unique order id */
		paytmParams.put("ORDER_ID", ORDER_ID);
		
		/* unique id that belongs to your customer */
		paytmParams.put("CUST_ID", CUST_ID);
		
		/**
		* Amount in INR that is payble by customer
		* this should be numeric with optionally having two decimal points
		*/
		paytmParams.put("TXN_AMOUNT", TXN_AMOUNT);
		
		/* Find your MID in your Paytm Dashboard at https://dashboard.paytm.com/next/apikeys */
		paytmParams.put("MID", bean.getMerchantId());
		
		/* Find your WEBSITE in your Paytm Dashboard at https://dashboard.paytm.com/next/apikeys */
		paytmParams.put("WEBSITE", bean.getWebsite());
		
		/* Find your INDUSTRY_TYPE_ID in your Paytm Dashboard at https://dashboard.paytm.com/next/apikeys */
		paytmParams.put("INDUSTRY_TYPE_ID", bean.getIndustryTypeId());
		
		/* WEB for website and WAP for Mobile-websites or App */
		paytmParams.put("CHANNEL_ID", bean.getChannelId());
		
		/* customer's mobile number */
		paytmParams.put("MOBILE_NO", env.getProperty("paytm.mobileNo"));

		/* customer's email */
		paytmParams.put("EMAIL", env.getProperty("paytm.email"));

		/* on completion of transaction, we will send you the response on this URL */
		paytmParams.put("CALLBACK_URL", bean.getCallBackUrl());
		
		/**
		* Generate checksum for parameters we have
		* You can get Checksum JAR from https://developer.paytm.com/docs/checksum/
		* Find your Merchant Key in your Paytm Dashboard at https://dashboard.paytm.com/next/apikeys 
		*/
		String checksum = CheckSumServiceHelper.getCheckSumServiceHelper().genrateCheckSum(bean.getMerchantKey(), paytmParams);

		
		
		paytmParams.put("CHECKSUMHASH", checksum);
		
		ModelAndView modelAndView = new ModelAndView("redirect:"+bean.getPaytmUrl());
		modelAndView.addAllObjects(paytmParams);
		
		request.setAttribute("params", paytmParams);
		request.setAttribute("paytmURL", bean.getPaytmUrl());
		
		return modelAndView;
		//return "redirect";
	}
	
	@PostMapping("/pgresponse")
	public ModelAndView pgresponse(HttpServletRequest request) throws Exception
	{
		System.out.println("pgresponse : TIME : "+Calendar.getInstance().getTime());
		System.out.println("pgresponse********************************************************* : ");
		Enumeration<String> enumeration =  request.getParameterNames();
		
		String respcode = "";
		String status = "";
		
		while(enumeration.hasMoreElements())
		{
			String key = enumeration.nextElement();
			System.out.println("key : "+key+"  : value : "+request.getParameter(key));
			if(request.getParameter("RESPCODE") != null)
				respcode = request.getParameter("RESPCODE");
			if(request.getParameter("STATUS") != null)
				status = request.getParameter("STATUS") ;
				
			request.setAttribute("RESPMSG", request.getParameter("RESPMSG"));
			request.setAttribute("TXNAMOUNT", request.getParameter("TXNAMOUNT"));
		}
		request.setAttribute("respcode", respcode);
		String paytmChecksum = "";
		/* Create a TreeMap from the parameters received in POST */
		TreeMap<String, String> paytmParams = new TreeMap<String, String>();
		for (Entry<String, String[]> requestParamsEntry : request.getParameterMap().entrySet()) {
		    if ("CHECKSUMHASH".equalsIgnoreCase(requestParamsEntry.getKey())){
		        paytmChecksum = requestParamsEntry.getValue()[0];
		    } else {
		        paytmParams.put(requestParamsEntry.getKey(), requestParamsEntry.getValue()[0]);
		    }
		}

		/**
		* Verify checksum
		* Find your Merchant Key in your Paytm Dashboard at https://dashboard.paytm.com/next/apikeys 
		*/
		boolean isValidChecksum = CheckSumServiceHelper.getCheckSumServiceHelper().verifycheckSum(bean.getMerchantKey(), paytmParams, paytmChecksum);
		if (isValidChecksum) {
			System.out.append("Checksum Matched\n");
		} else {
			System.out.append("Checksum Mismatched");
		}
		
		if(respcode != null && !"".equals(respcode.trim()) && "01".equals(respcode) )
		{
			System.out.println("respcode is : "+respcode);
			if(status != null && !"".equals(status.trim()) && "Approved".equalsIgnoreCase(status) || "TXN_SUCCESS".equalsIgnoreCase(status) )
			{
				System.out.println("Based on respcode and status Transaction is success");
				request.setAttribute("result_check", true);
			}
			else
			{
				System.out.println("Based on  status Transaction is success");
				request.setAttribute("result_check", false);
			}
		}
		else
		{
			System.out.println("Based on respcode  Transaction is failure");
			request.setAttribute("result_check", false);
		}
		
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("result");
		modelAndView.addObject("result", paytmParams);
		return modelAndView;
		
	}
	
	@GetMapping("/getRandomNumber")
	public void getRandomNumber(HttpServletRequest request,HttpServletResponse response) throws IOException
	{
		String ran = String.valueOf(util.genRandonnum());
		response.getWriter().append(ran);
		
	}
	
	
//	https:// givewp.com/documentation/add-ons/paytm-gateway/
//	https:// dashboard.paytm.com/next/apikeys
//	https:// developer.paytm.com/docs/v1/payment-gateway/
	
}
