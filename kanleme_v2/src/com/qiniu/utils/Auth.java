package com.qiniu.utils;


import java.net.URI;
import java.security.GeneralSecurityException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.alibaba.fastjson.JSON;

public final class Auth {

    /**
     * 上传策略，参数规格详见
     * <p/>
     * http://developer.qiniu.com/docs/v6/api/reference/security/put-policy.html
     */
    private static final String[] policyFields = new String[]{
            "callbackUrl",
            "callbackBody",
            "callbackHost",
            "callbackBodyType",
            "callbackFetchKey",

            "returnUrl",
            "returnBody",

            "endUser",
            "saveKey",
            "insertOnly",

            "detectMime",
            "mimeLimit",
            "fsizeLimit",
            "fsizeMin",

            "persistentOps",
            "persistentNotifyUrl",
            "persistentPipeline",
    };
    private static final String[] deprecatedPolicyFields = new String[]{
            "asyncOps",
    };
    private final String accessKey;
    private final SecretKeySpec secretKey;

    private Auth(String accessKey, SecretKeySpec secretKeySpec) {
        this.accessKey = accessKey;
        this.secretKey = secretKeySpec;
    }

    public static Auth create(String accessKey, String secretKey) {
        if (StringUtils.isNullOrEmpty(accessKey) || StringUtils.isNullOrEmpty(secretKey)) {
            throw new IllegalArgumentException("empty key");
        }
        byte[] sk = StringUtils.utf8Bytes(secretKey);
        SecretKeySpec secretKeySpec = new SecretKeySpec(sk, "HmacSHA1");
        return new Auth(accessKey, secretKeySpec);
    }

    private static void copyPolicy(final StringMap policy, StringMap originPolicy, final boolean strict) {
        if (originPolicy == null) {
            return;
        }
        originPolicy.forEach(new StringMap.Consumer() {
            @Override
            public void accept(String key, Object value) {
                if (StringUtils.inStringArray(key, deprecatedPolicyFields)) {
                    throw new IllegalArgumentException(key + " is deprecated!");
                }
                if (!strict || StringUtils.inStringArray(key, policyFields)) {
                    policy.put(key, value);
                }
            }
        });
    }

    private Mac createMac() {
        Mac mac;
        try {
            mac = javax.crypto.Mac.getInstance("HmacSHA1");
            mac.init(secretKey);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e);
        }
        return mac;
    }

    private String sign(byte[] data) {
        Mac mac = createMac();
        String encodedSign = UrlSafeBase64.encodeToString(mac.doFinal(data));
        return this.accessKey + ":" + encodedSign;
    }

    private String signWithData(byte[] data) {
        String s = UrlSafeBase64.encodeToString(data);
        return sign(StringUtils.utf8Bytes(s)) + ":" + s;
    }

    /**
     * scope = bucket
     * 一般情况下可通过此方法获取token
     *
     * @param bucket 空间名
     * @return 生成的上传token
     */
    public String uploadToken(String bucket) {
        return uploadToken(bucket, null, 3600, null, true);
    }

    /**
     * scope = bucket:key
     * 同名文件覆盖操作、只能上传指定key的文件可以可通过此方法获取token
     *
     * @param bucket 空间名
     * @param key    key，可为 null
     * @return 生成的上传token
     */
    public String uploadToken(String bucket, String key) {
        return uploadToken(bucket, key, 3600, null, true);
    }

    /**
     * 生成上传token
     *
     * @param bucket  空间名
     * @param key     key，可为 null
     * @param expires 有效时长，单位秒
     * @param policy  上传策略的其它参数，如 new StringMap().put("endUser", "uid").putNotEmpty("returnBody", "")。
     *                scope通过 bucket、key间接设置，deadline 通过 expires 间接设置
     * @return 生成的上传token
     */
    public String uploadToken(String bucket, String key, long expires, StringMap policy) {
        return uploadToken(bucket, key, expires, policy, true);
    }

    /**
     * 生成上传token
     *
     * @param bucket  空间名
     * @param key     key，可为 null
     * @param expires 有效时长，单位秒。默认3600s
     * @param policy  上传策略的其它参数，如 new StringMap().put("endUser", "uid").putNotEmpty("returnBody", "")。
     *                scope通过 bucket、key间接设置，deadline 通过 expires 间接设置
     * @param strict  是否去除非限定的策略字段，默认true
     * @return 生成的上传token
     */
    public String uploadToken(String bucket, String key, long expires, StringMap policy, boolean strict) {
        long deadline = System.currentTimeMillis() / 1000 + expires;
        return uploadTokenWithDeadline(bucket, key, deadline, policy, strict);
    }

    private String uploadTokenWithDeadline(String bucket, String key, long deadline, StringMap policy, boolean strict) {
        String scope = bucket;
        if (key != null) {
            scope = bucket + ":" + key;
        }
        StringMap x = new StringMap();
        copyPolicy(x, policy, strict);
        x.put("scope", scope);
        x.put("deadline", deadline);

        String s = JSON.toJSONString(x.map());
        return signWithData(StringUtils.utf8Bytes(s));
    }
    /**
     * 生成HTTP请求签名字符串
     *
     * @param urlString
     * @param body
     * @param contentType
     * @return
     */
    public String signRequest(String urlString, byte[] body, String contentType) {
        URI uri = URI.create(urlString);
        String path = uri.getRawPath();
        String query = uri.getRawQuery();

        Mac mac = createMac();

        mac.update(StringUtils.utf8Bytes(path));

        if (query != null && query.length() != 0) {
            mac.update((byte) ('?'));
            mac.update(StringUtils.utf8Bytes(query));
        }
        mac.update((byte) '\n');
        if (body != null && body.length > 0 && !StringUtils.isNullOrEmpty(contentType)) {
            final String JsonMime = "application/json";
            final String FormMime = "application/x-www-form-urlencoded";
            if (contentType.equals(FormMime)
                    || contentType.equals(JsonMime)) {
                mac.update(body);
            }
        }

        String digest = UrlSafeBase64.encodeToString(mac.doFinal());

        return this.accessKey + ":" + digest;
    }
    public String authorization(String url, byte[] body, String contentType) {
        return "QBox " + signRequest(url, body, contentType);
    }
}
