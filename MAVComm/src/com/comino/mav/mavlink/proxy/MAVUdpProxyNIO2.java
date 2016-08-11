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


package com.comino.mav.mavlink.proxy;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;

import org.mavlink.messages.MAVLinkMessage;

import com.comino.mav.mavlink.MAVLinkStream;
import com.comino.msp.main.control.listener.IMAVLinkListener;


public class MAVUdpProxyNIO2 implements IMAVLinkListener  {

	private SocketAddress 			bindPort = null;
	private SocketAddress 			peerPort;
	private DatagramChannel 		channel = null;

	private ByteBuffer 				buffer = null;
	private MAVLinkStream			in	   = null;


	private boolean 			isConnected = false;
	private Selector selector;


	public MAVUdpProxyNIO2(String peerAddress, int pPort, String bindAddress, int bPort) {
		buffer = ByteBuffer.allocate(8192);
		peerPort = new InetSocketAddress(peerAddress, pPort);
		bindPort = new InetSocketAddress(bindAddress, bPort);

		System.out.println("Proxy: BindPort="+bPort+" PeerPort="+pPort);
	}

	public boolean open() {
		if(channel!=null && channel.isConnected()) {
			isConnected = true;
			return true;
		}

		try {
			isConnected = true;
			selector = Selector.open();
			channel = DatagramChannel.open();
			channel.socket().bind(bindPort);
			channel.configureBlocking(false);
			channel.connect(peerPort);
			SelectionKey key = channel.register(selector, SelectionKey.OP_WRITE);

			while(true) {

				int readyChannels = selector.select();
				if(readyChannels == 0) continue;

				Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();

				while(keyIterator.hasNext()) {

					SelectionKey k = keyIterator.next();

					if(k.isAcceptable()) {
						System.out.println("MAVProxy connected to "+peerPort.toString()+" Blocking="+channel.isBlocking());

					} else if (k.isConnectable()) {
						System.out.println("MAVProxy connected to "+peerPort.toString()+" Blocking="+channel.isBlocking());

					} else if (k.isReadable()) {
						// a channel is ready for reading

					} else if (k.isWritable()) {
						// a channel is ready for writing
					}
					keyIterator.remove();
				}
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
			close();
			isConnected = false;
		}

		return true;

		//		while(!isConnected) {
		//			try {
		//				isConnected = true;
		//				//			System.out.println("Connect to UDP channel");
		//				try {
		//					channel = DatagramChannel.open();
		//					channel.socket().bind(bindPort);
		//					channel.configureBlocking(true);
		//				} catch (IOException e) {
		//					e.printStackTrace();
		//				}
		//				channel.connect(peerPort);
		//				in = new MAVLinkStream(channel);
		//				System.out.println("MAVProxy connected to "+peerPort.toString()+" Blocking="+channel.isBlocking());
		//				return true;
		//			} catch(Exception e) {
		//				System.out.println(e.getMessage());
		//				close();
		//				isConnected = false;
		//
		//			}
		//		}
		//		return false;
	}

	public boolean isConnected() {
		return isConnected;
	}

	public void close() {
		isConnected = false;
		try {
			if (channel != null) {
				channel.close();
			}
		} catch(IOException e) {

		}
	}

	public MAVLinkStream getInputStream() {
		return in;
	}

	public void write(MAVLinkMessage msg) {
		try {
			if(isConnected) {

				if(!channel.isConnected())
					throw new IOException("Channel not bound");

				buffer.put(msg.encode());
				buffer.flip();
				channel.write(buffer);
				buffer.compact();
			}


		} catch (IOException e) {
			try { Thread.sleep(150); } catch(Exception k) { }
			buffer.clear();
			close();
			isConnected = false;
		}
	}

	@Override
	public void received(Object o) {
		write((MAVLinkMessage) o);
	}
}
