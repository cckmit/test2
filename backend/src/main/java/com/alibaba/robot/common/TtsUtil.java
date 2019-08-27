package com.alibaba.robot.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.apache.log4j.Logger;

/**
 * 天猫精灵调用HTTP
 */
public class TtsUtil {

	private static final int CONNECT_TIMEOUT = 8000;
	private static final int READ_TIMEOUT = 8000;
	private static Logger LOGGER = Logger.getLogger(TtsUtil.class);

	/**
	 * 停止播放
	 * 
	 * @param uuid
	 * @param command
	 */
	public static boolean pushCommand(String uuid, String command) {
		boolean flag = false;
		try {
			String urlStr = "https://g-aicloud.alibaba.com/toolkits/api/pushCommand?uuid="
					+ URLEncoder.encode(uuid, "utf-8") + "&command=" + URLEncoder.encode(command, "utf-8");

			int responseCode = httpGet(urlStr);
			//System.out.println("==>" + responseCode);
			flag = responseCode == HttpURLConnection.HTTP_OK;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return flag;
	}

	/**
	 * 播放声音
	 * 
	 * @param uuid
	 * @param text
	 */
	public static boolean ttsBroadcast(String uuid, String text) {
		boolean flag = false;
		try {
			String urlStr = "https://g-aicloud.alibaba.com/toolkits/api/ttsBroadcast?uuid="
					+ URLEncoder.encode(uuid, "utf-8") + "&text=" + URLEncoder.encode(text, "utf-8");
			int responseCode = httpGet(urlStr);
			flag = responseCode == HttpURLConnection.HTTP_OK;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return flag;
	}

	public static int httpGet(String urlStr) throws IOException {
		HttpURLConnection connection = null;
		int responseCode = 0;
		InputStreamReader inputStreamReader = null;
		BufferedReader bufferedReader = null;
		try {
			URL url = new URL(urlStr);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Charset", "UTF-8");
			connection.setConnectTimeout(CONNECT_TIMEOUT);
			connection.setReadTimeout(READ_TIMEOUT);
			connection.connect();
			responseCode = connection.getResponseCode();

			inputStreamReader = new InputStreamReader(connection.getInputStream(), "utf-8");
			bufferedReader = new BufferedReader(inputStreamReader);
			String readLine = bufferedReader.readLine();
			LOGGER.info(readLine);
		} catch (IOException e) {
			responseCode = connection.getResponseCode();
			inputStreamReader = new InputStreamReader(connection.getInputStream(), "utf-8");
			bufferedReader = new BufferedReader(inputStreamReader);
			String readLine = bufferedReader.readLine();
			LOGGER.info(readLine);

		} finally {
			if (connection != null) {
				connection.disconnect();
			}
			if (inputStreamReader != null) {
				inputStreamReader.close();
			}
			if (bufferedReader != null) {
				bufferedReader.close();
			}
		}
		return responseCode;
	}

}
