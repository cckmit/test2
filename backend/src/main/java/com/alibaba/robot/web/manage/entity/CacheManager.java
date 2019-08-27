package com.alibaba.robot.web.manage.entity;

import java.io.UnsupportedEncodingException;
import java.time.Duration;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class CacheManager {
	
	private static Logger LOGGER = Logger.getLogger(CacheManager.class);
	private static final String REDIS_STATUS_OK = "OK";
	private static CacheManager INSTANCE = new CacheManager();
	private static String DEFAULT_ENCODING = "utf-8";
	private final JedisPoolConfig poolConfig = buildPoolConfig();
	private JedisPool jedisPool = new JedisPool(poolConfig, "localhost");

	private JedisPoolConfig buildPoolConfig() {
		final JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxTotal(128);
		poolConfig.setMaxIdle(32);
		poolConfig.setMinIdle(8);
		poolConfig.setTestOnBorrow(true);
		poolConfig.setTestOnReturn(true);
		poolConfig.setTestWhileIdle(true);
		poolConfig.setMinEvictableIdleTimeMillis(Duration.ofSeconds(60).toMillis());
		poolConfig.setTimeBetweenEvictionRunsMillis(Duration.ofSeconds(30).toMillis());
		poolConfig.setNumTestsPerEvictionRun(3);
		poolConfig.setBlockWhenExhausted(true);
		return poolConfig;
	}

	private CacheManager() {

	}

	public static CacheManager getInstance() {
		return INSTANCE;
	}

	public boolean set(String key, int value) {
		if (key == null || key.length() == 0) {
			return false;
		}
		try {

			try (Jedis jedis = jedisPool.getResource()) {
				String statusCode = jedis.set(key.getBytes(DEFAULT_ENCODING),
						Integer.toString(value).getBytes(DEFAULT_ENCODING));
				return REDIS_STATUS_OK.equals(statusCode);
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			LOGGER.error(e.toString());
			return false;
		}
	}

	 
	/***
	 * Add value into a set
	 * */
	public Long sadd(String key, String value) {
		if (key == null || value == null || key.length() == 0 || value.length() == 0) {
			return -1l;
		}

		try (Jedis jedis = jedisPool.getResource()) {
			return jedis.sadd(key, value);
		}
	}
	
	
	/***
	 * Get all members of a set
	 * */
	public boolean smembers(String key, Set<String> set) {
		if (key == null || set == null || key.length() == 0 ) {
			return false;
		}

		try (Jedis jedis = jedisPool.getResource()) {
			set.addAll(jedis.smembers(key));
		}
		return true;
	}
	
 
	public Long hset(String key, String field, String value) {
		if (key == null || value == null || key.length() == 0 || value.length() == 0) {
			return -1l;
		}

		try (Jedis jedis = jedisPool.getResource()) {
			return jedis.hset(key, field, value);
		}
	}
	
	public boolean hmset(String key, Map<String, String> pair) {
		if (key == null || pair == null || pair.size() == 0 ) {
			return false;
		}

		try (Jedis jedis = jedisPool.getResource()) {
			String status = jedis.hmset(key, pair);
			return REDIS_STATUS_OK.equals(status);
		}
	}
	
	public Long del(String key) {
		if (key == null || key.length() == 0 ) {
			return -1l;
		}
		try (Jedis jedis = jedisPool.getResource()) {
			return jedis.del(key);
		}
	}
	
	/***
	 * Get value of the field in hash
	 * */
	public String hget(String key, String field) {
		if (key == null ||  key.length() == 0) {
			return null;
		}

		try (Jedis jedis = jedisPool.getResource()) {
			return jedis.hget(key, field);
		}
	}
	
	
	/**
	 * Get all keys of a hash
	 * */
	public boolean hkeys(String key, Set<String> keys) {
		if (key == null ||  key.length() == 0 || keys == null) {
			return false;
		}

		try (Jedis jedis = jedisPool.getResource()) {
			keys.addAll(jedis.hkeys(key));
		}
		return true;
	}
	
	
	/**
	 * Get all keys and value
	 * */
	public boolean hgetall(String key, Map<String, String> all) {
		if (key == null ||  key.length() == 0 || all == null) {
			return false;
		}

		try (Jedis jedis = jedisPool.getResource()) {
			Map<String, String> map = jedis.hgetAll(key);
			all.putAll(map);
		}
		return true;
	}
	
	
	
	public boolean set(String key, String value) {
		if (key == null || value == null || key.length() == 0 || value.length() == 0) {
			return false;
		}

		try (Jedis jedis = jedisPool.getResource()) {
			String statusCode = jedis.set(key, value);
			return REDIS_STATUS_OK.equals(statusCode);
		}
	}

	/***
	 * Set key with expiration in second
	 */
	public boolean set(String key, String value, int expirationSecond) {
		if (key == null || value == null || key.length() == 0 || value.length() == 0) {
			return false;
		}
		try (Jedis jedis = jedisPool.getResource()) {

			String statusCode = jedis.setex(key, expirationSecond, value);
			return REDIS_STATUS_OK.equals(statusCode);

		}
	}

	public String get(String key) {
		return get(key, null);
	}

	public String get(String key, String defaultValue) {
		if (key == null || key.length() == 0) {
			return defaultValue;
		}

		try (Jedis jedis = jedisPool.getResource()) {
			String value = jedis.get(key);
			if (value == null) {
				return defaultValue;
			}
			return value;

		}
	}

	public int getInt(String key, int defaultValue) {
		if (key == null || key.length() == 0) {
			return defaultValue;
		}
		try {

			try (Jedis jedis = jedisPool.getResource()) {
				String value = jedis.get(key);
				if (value == null) {
					return defaultValue;
				}
				return Integer.parseInt(value);
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
			LOGGER.error(e.toString());
			return defaultValue;
		}
	}

}
