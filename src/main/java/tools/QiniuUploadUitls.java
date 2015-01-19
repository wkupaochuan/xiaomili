package tools;

import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;
import com.qiniu.api.auth.AuthException;
import com.qiniu.api.auth.digest.Mac;
import com.qiniu.api.rs.PutPolicy;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;


public class QiniuUploadUitls {

	/** 在网站上查看 */
	private static final String ACCESS_KEY = "1ChkZST1lYEAPRKPLQcEXHnIYeKjUWhx6nZbC1nD";
	/** 在网站上查看 */
	private static final String SECRET_KEY = "Vi3g-lK-pw49PzsXchOkoJ-_-Ll6iBP8uE8boKA_";
    /** 你所创建的空间的名称*/
	private static final String bucketName = "test";
	
	public interface QiniuUploadUitlsListener{
		public void onSucess(String fileUrl);
		public void onError(int errorCode, String msg);
		public void onProgress(int progress);
	}

    // 单例
    private static QiniuUploadUitls qiniuUploadUitls = null;

    private UploadManager uploadManager;

    /**
     * 构造方法
     */
	public QiniuUploadUitls() {
        this.uploadManager = new UploadManager();
	}

    /**
     * 获取单例
     * @return
     */
	public static QiniuUploadUitls getInstance() {
		if (qiniuUploadUitls == null) {
			qiniuUploadUitls = new QiniuUploadUitls();
		}
		return qiniuUploadUitls;
	}

    /**
     * 上传文件
     * @param filePath
     * @param listener
     */
	public void uploadFile(String filePath,final QiniuUploadUitlsListener listener) {
		final String fileUrlUUID = getFileUrlUUID();
		String token = getToken();
		if (token == null) {
			if(listener != null){
				listener.onError(-1, "token is null");
			}
			return;
		}
		uploadManager.put(filePath, fileUrlUUID, token,
				new UpCompletionHandler() {
					@Override
					public void complete(String key, ResponseInfo info,
							JSONObject response) {
						System.out.println("debug:info = " + info
								+ ",response = " + response);
						if (info != null && info.statusCode == 200) {// 上传成功
							String fileRealUrl = getRealUrl(fileUrlUUID);
							System.out.println("debug:fileRealUrl = "+fileRealUrl);
							if(listener != null){
								listener.onSucess(fileRealUrl);
							}
						} else {
							if(listener != null){
								listener.onError(info.statusCode, info.error);
							}
						}
					}
				}, new UploadOptions(null, null, false,
						new UpProgressHandler() {
							public void progress(String key, double percent) {
								if(listener != null){
									listener.onProgress((int)(percent*100));
								}
							}
						}, null));

	}
	
	/**
	 * 生成远程文件路径（全局唯一）
	 * 
	 * @return
	 */
	private String getFileUrlUUID() {
		String filePath = android.os.Build.MODEL + "__"
				+ System.currentTimeMillis() + "__"
				+ (new Random().nextInt(500000)) + "_"
				+ (new Random().nextInt(10000));
		return filePath.replace(".", "0");
	}
	
	private String getRealUrl(String fileUrlUUID){
		String filePath = "http://"+bucketName+".qiniudn.com/"+fileUrlUUID;
		return filePath;
	}

	/**
	 * 获取token 本地生成
	 * 
	 * @return
	 */
	private String getToken() {
		Mac mac = new Mac(ACCESS_KEY, SECRET_KEY);
		PutPolicy putPolicy = new PutPolicy(bucketName);
		putPolicy.returnBody = "{\"name\": $(fname),\"size\": \"$(fsize)\",\"w\": \"$(imageInfo.width)\",\"h\": \"$(imageInfo.height)\",\"key\":$(etag)}";
		try {
			String uptoken = putPolicy.token(mac);
			System.out.println("debug:uptoken = " + uptoken);
			return uptoken;
		} catch (AuthException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

}
