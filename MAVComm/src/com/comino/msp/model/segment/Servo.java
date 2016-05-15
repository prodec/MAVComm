/****************************************************************************
 *
 *   Copyright (c) 2016 Eike Mansfeld ecm@gmx.de. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 * 3. Neither the name of the copyright holder nor the names of its
 *    contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
 * COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS
 * OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED
 * AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN
 * ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 ****************************************************************************/


package com.comino.msp.model.segment;

import com.comino.msp.model.segment.generic.Segment;

public class Servo extends Segment {

	private static final long serialVersionUID = 6155451845240484694L;

	public short 	s0 = 0;
	public short 	s1 = 0;
	public short 	s2 = 0;
	public short 	s3 = 0;
	public short 	s4 = 0;
	public short 	s5 = 0;
	public short 	s6 = 0;
	public short 	s7 = 0;

	public void set(Servo a) {
		s0		= a.s0;
		s1		= a.s1;
		s2		= a.s2;
		s3		= a.s3;
		s4		= a.s4;
		s5		= a.s5;
		s6		= a.s6;
		s7		= a.s7;

	}

	public Servo clone() {
		Servo at = new Servo();
		at.s0 		= s0;
		at.s1 		= s1;
		at.s2 		= s2;
		at.s3 		= s3;
		at.s4 		= s4;
		at.s5 		= s5;
		at.s6 		= s6;
		at.s7 		= s7;

		return at;
	}

	//--------------------------------------------------------------------------------------------------------


	public void clear() {
		s0 = 0;
		s1 = 0;
		s2 = 0;
		s3 = 0;
		s4 = 0;
		s5 = 0;
		s6 = 0;
		s7 = 0;
	}

}
