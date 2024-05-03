/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.fraunhofer.sit.sse.valbench.modellingtests;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.Arrays;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import de.fraunhofer.sit.sse.valbench.metadata.ValueComputationTestCase;

public class ApacheHex {

	@ValueComputationTestCase
	public static String testApacheHexDecodeS() throws UnsupportedEncodingException, DecoderException {

		return Arrays.toString(Hex.decodeHex("466f6f".toCharArray()));
	}

	@ValueComputationTestCase
	public static String testApacheHex() throws UnsupportedEncodingException {
		return Hex.encodeHexString("Foo".getBytes());
	}

	@ValueComputationTestCase
	public static String testApacheHexDecode() throws UnsupportedEncodingException, DecoderException {
		return Arrays.toString(Hex.decodeHex(Hex.encodeHexString("Foo".getBytes())));
	}

	@ValueComputationTestCase
	public static String testApacheHexDecode2() throws UnsupportedEncodingException, DecoderException {
		return Arrays.toString(Hex.decodeHex(Hex.encodeHexString(ByteBuffer.wrap(new byte[] { 1, 2, 3 }))));
	}

}
