package com.packtpub.e4.hello.ui;

public class Utility {
	public static boolean breakpoint(Object...vars) {
		Object args[] = new Object[]{vars[3].toString(), vars[4].toString()};
		System.out.println(vars[1]+": "+String.format((String) vars[2], args));
		return (boolean) vars[0];
	}
}
