/***************************************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 **************************************************************************************************/
package org.eclipse.wst.common.internal.emf.utilities;

public class PasswordEncoderDecoder extends EncoderDecoderAdapter {
	public static final String KEY = "password-security-coder"; //$NON-NLS-1$

	public PasswordEncoderDecoder() {
	}

	@Override
	public String decode(String s) {
		return PasswordUtil.passwordDecode(s);
	}

	@Override
	public String encode(String s) {
		return PasswordUtil.passwordEncode(s);
	}

	public Object getKey() {
		return KEY;
	}
}