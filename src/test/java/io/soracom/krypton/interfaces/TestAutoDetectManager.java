package io.soracom.krypton.interfaces;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class TestAutoDetectManager {

	public static void main(String[] args) {
		new AutoDetectManager(true);
		System.out.println("end");
	}

	public static class MockIUiccInterface implements IUiccInterface {
		String imsi;
		int wait;

		@Override
		public void setDebug(boolean debug) {
		}

		@Override
		public String readImsi() {
			try {
				Thread.sleep(wait * 1000);
			} catch (InterruptedException e) {
			}
			return imsi;
		}

		@Override
		public byte[] authenticate(byte[] rand, byte[] autn) {
			return null;
		}
		@Override
		public boolean disconnect() {
			// TODO Auto-generated method stub
			return false;
		}
	}

	@Test
	public void testAutoDetect() {
		AutoDetectManager manager = new AutoDetectManager(true) {
			@Override
			protected List<InterfaceDetectThread> createThreadList(Callback callback) {
				List<InterfaceDetectThread> list = new ArrayList<>();
				{
					MockIUiccInterface mock = new MockIUiccInterface();
					mock.imsi = "FIRST";
					mock.wait = 5;
					list.add(new InterfaceDetectThread(mock, mock.imsi, callback));
				}
				{
					MockIUiccInterface mock = new MockIUiccInterface();
					mock.imsi = null;
					mock.wait = 1;
					list.add(new InterfaceDetectThread(mock, mock.imsi, callback));
				}
				{
					MockIUiccInterface mock = new MockIUiccInterface();
					mock.imsi = "SECOND";
					mock.wait = 10;
					list.add(new InterfaceDetectThread(mock, mock.imsi, callback));
				}
				return list;
			}
		};
		assertEquals("FIRST", manager.readImsi());
	}
}
