package com.paytm.api.util;

import java.util.Random;

import org.springframework.stereotype.Component;

@Component
public class Util {
	
	
	public int genRandonnum( )
	{
		Random random = new Random();
		return 100000 + random.nextInt(900000);
	}

	/*
	 * public static void main(String[] args) { System.out.println(new
	 * Util().genRandonnum()); }
	 */
	
}
