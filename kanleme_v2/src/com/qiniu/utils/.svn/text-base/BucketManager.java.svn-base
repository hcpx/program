package com.qiniu.utils;

public class BucketManager {

	/**
	 * EncodedEntryURI格式
	 *
	 * @param bucket
	 * @param key
	 * @return urlsafe_base64_encode(Bucket:Key)
	 */
	private static String entry(String bucket, String key) {
		return entry(bucket, key, true);
	}

	/**
	 * EncodedEntryURI格式 当 mustHaveKey 为 false， 且 key 为 null 时，返回
	 * urlsafe_base64_encode(Bucket); 其它条件下返回 urlsafe_base64_encode(Bucket:Key)
	 *
	 * @param bucket
	 * @param key
	 * @param mustHaveKey
	 * @return urlsafe_base64_encode(entry)
	 */
	private static String entry(String bucket, String key, boolean mustHaveKey) {
		String en = bucket + ":" + key;
		if (!mustHaveKey && (key == null)) {
			en = bucket;
		}
		return UrlSafeBase64.encodeToString(en);
	}

	/**
	 * 删除指定空间、文件名的文件
	 *
	 * @param bucket
	 * @param key
	 * @throws QiniuException
	 */
	public static BucketManagerParam delete(String managerurl,Auth auth, String bucket, String key) {
		BucketManagerParam param = new BucketManagerParam();
		param.url = managerurl + "/delete/" + entry(bucket, key);
		param.authorization = auth.authorization(param.url, null, "application/x-www-form-urlencoded");
		return param;
	}

	public static class BucketManagerParam {
		public String url;
		public String authorization;
	}
}
