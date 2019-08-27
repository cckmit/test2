package com.alibaba.robot.transport;

public class Address {
	public Address(String host, int port) {
		if (host == null || host.length() == 0) {
			throw new IllegalArgumentException();
		}
		this.mHost = host;
		this.mPort = port;
	}

	public Address(String address) {
		if (address == null || address.length() == 0) {
			throw new IllegalArgumentException();
		}
		String[] parts = address.split(":");
		if(parts.length == 2) {
			mHost = parts[0];
			mPort = Integer.parseInt(parts[1]);	
		}
	}

	public String getHost() {
		return mHost;
	}

	public int getPort() {
		return mPort;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		Address another = (Address) obj;
		return (another.mPort == mPort && another.mHost.equals(mHost));
	}

	@Override
	public int hashCode() {
		int i = mHost != null ? mHost.hashCode() : 0;
		i += mPort;
		return i;
	}
	
	@Override
	public String toString() {
		if(mHost != null) {
			return mHost + ":" + mPort;
		}
		return ":" + mPort;
	}

	private String mHost = "localhost";
	private int mPort = 9090;
}
