/**
 * Copyright 2013 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance with
 * the License. A copy of the License is located at
 * 
 * http://aws.amazon.com/apache2.0/
 * 
 * or in the "LICENSE" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */
package awslabs.lab51;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.UUID;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSCredentialsProviderChain;
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.TableDescription;
import com.amazonaws.services.s3.AmazonS3Client;

/**
 * プロジェクト: Lab5.1
 */
public class Lab51 {
	private ILabCode labCode = new StudentCode(this);
	private IOptionalLabCode optionalLabCode = new StudentCode(this);

	private List<String> statusLog = new ArrayList<String>();
	private List<String> imageListRows = new ArrayList<String>();
	private List<String> imageNames = new ArrayList<String>();

	private InstanceIdentity instance = new InstanceIdentity();

	private AWSCredentials credentials;

	// カスタム認証情報プロバイダーチェーンを作成する
	private AWSCredentialsProvider credsProvider = new AWSCredentialsProviderChain(
			new EnvironmentVariableCredentialsProvider(), new InstanceProfileCredentialsProvider());

	public Lab51() {
		// リストにイメージを追加する
		imageNames.add("icons/dynamodb.png");
		imageNames.add("icons/ec2.png");
		imageNames.add("icons/elasticbeanstalk.png");
		imageNames.add("icons/iam.png");
		imageNames.add("icons/s3.png");
		imageNames.add("icons/sqs.png");
		// デコイイメージ
		imageNames.add("decoy/decoy.png");

		
		try {
			credentials = credsProvider.getCredentials();
			
		} catch (Exception ex) {
			logMessageToPage(ex.getMessage());
			return;
		}
		finally {
    		if (credentials==null) {
    			logMessageToPage("No credentials found.");
    			return;
    		}
		}
		// 指定された場合、カスタム設定をインポートする。ソース：ElasticBeanstalkのカスタム設定(Config)
    		
		prepSettings();

		// オブジェクトが生成されたので、値を確認し、追加で必要なデフォルト設定をする
		if (System.getProperty("SESSIONTABLE") == null || System.getProperty("SESSIONTABLE").isEmpty()) {
			logMessageToPage("SESSIONTABLE wasn't defined. Using default value 'imageindex'");
			System.setProperty("SESSIONTABLE", "imageindex");
		}
		

		if (System.getProperty("REGION") == null || System.getProperty("REGION").isEmpty()) {
			logMessageToPage("REGION wasn't defined. Using default value 'us-east-1'");
			System.setProperty("REGION", "us-east-1");
		}

		if (System.getProperty("PARAM3") == null || System.getProperty("PARAM3").isEmpty()) {
			logMessageToPage("PARAM3 wasn't defined. Using default value 'icons'");
			System.setProperty("PARAM3", "icons");
		}

	}
	
	public void syncImages() {
		try {
		String tableName = System.getProperty("SESSIONTABLE");
		
		AmazonDynamoDBClient dynamoDbClient = labCode.createDynamoDbClient(credentials);
		TableDescription tableDescription = optionalLabCode.getTableDescription(dynamoDbClient, tableName);
		if (tableDescription == null) {
			logMessageToPage("No table found. Creating it.");
			optionalLabCode.buildTable(dynamoDbClient, tableName);
			tableDescription = optionalLabCode.getTableDescription(dynamoDbClient, tableName);
		}

		// テーブルができたので、有効かを確認する
		if (!optionalLabCode.validateSchema(tableDescription)) {
			// もし有効でなければ、再構築する
			logMessageToPage("Table schema is incorrect. Dropping table and rebuilding it.");
			optionalLabCode.deleteTable(dynamoDbClient, tableName);
			optionalLabCode.buildTable(dynamoDbClient, tableName);
		}

		// 有効になったら、イメージを確認する。もしDynamoDBでなければDynamoDBおよびS3に追加する
		List<String> missingImages = new ArrayList<String>();

		for (String image : imageNames) {
			if (!optionalLabCode.isImageInDynamo(dynamoDbClient, tableName, image)) {
				// もし存在しなければ、そのイメージをリストに追加する.
				missingImages.add(image);
			}
		}

		// もしイメージリストに何か不足している場合、ページをフォーマットする前に確実に追加する
		if (missingImages.size() > 0) {
			String bucketName = "awslabj" + UUID.randomUUID().toString().substring(0, 8);
			logMessageToPage("Adding images to S3 (%s) and DynamoDB", bucketName);
			// 無いイメージを追加する

			AmazonS3Client s3Client = labCode.createS3Client(credentials);
			// はじめにバケットを作成
			// us-east-1以外のリージョンの場合は、バケット作成時にリージョン制約 
			// を指定
			String region = System.getProperty("REGION");
			if (region.equals("us-east-1")) {
				s3Client.createBucket(bucketName);
			}
			else {
				s3Client.createBucket(bucketName, com.amazonaws.services.s3.model.Region.fromValue(region));
			}
			
			String rootPath = Lab51.class.getResource("/").getFile(); 
			for (String image : missingImages) {
				String filePath = rootPath + image;
				if (new File(filePath).exists()) {
					optionalLabCode.addImage(dynamoDbClient, tableName, s3Client, bucketName, image, filePath);
				}
				else {
					logMessageToPage("File not found on disk: %s", filePath);
				}
			}
		}
		}
		catch (Exception ex) {
			logMessageToPage("syncImages() error: [%s] %s", ex.getClass().getName(), ex.getMessage());
		}
	}

	public String getConfigAsHtml() {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("<tr><td><b>%s</b> <i>%s</i>&nbsp;</td><td>&nbsp;%s</td></tr>", "SESSIONTABLE",
				"(table name)", System.getProperty("SESSIONTABLE")));
		sb.append(String.format("<tr><td><b>%s</b> <i>%s</i>&nbsp;</td><td>&nbsp;%s</td></tr>", "REGION",
				"(target region)", System.getProperty("REGION")));
		sb.append(String.format("<tr><td><b>%s</b> <i>%s</i>&nbsp;</td><td>&nbsp;%s</td></tr>", "PARAM3",
				"(key prefix)", System.getProperty("PARAM3")));
		sb.append(String.format("<tr><td><b>%s</b>&nbsp;</td><td>&nbsp;%s</td></tr>", "runtime.settings",
				System.getProperty("runtime.settings")));

		return sb.toString();
	}

	public String getSysEnvAsHtml() {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("<tr><td><b>%s</b>&nbsp;</td><td>&nbsp;%s</td></tr>", "EC2 Instance ID",
				instance.getInstanceId()));
		sb.append(String.format("<tr><td><b>%s</b>&nbsp;</td><td>&nbsp;%s</td></tr>", "Instance Type",
				instance.getInstanceType()));
		sb.append(String.format("<tr><td><b>%s</b>&nbsp;</td><td>&nbsp;%s</td></tr>", "Host Instance Region",
				instance.getRegion()));
		sb.append(String.format("<tr><td><b>%s</b>&nbsp;</td><td>&nbsp;%s</td></tr>", "Availability Zone",
				instance.getAvailabilityZone()));

		String[] keys = { "PROCESSOR_IDENTIFIER" };
		for (String key : keys) {
			sb.append(String.format("<tr><td><b>%s</b>&nbsp;</td><td>&nbsp;%s</td></tr>", key, System.getenv(key)));
		}

		return sb.toString();
	}

	public String getImageListAsHtml() {
		StringBuilder sb = new StringBuilder();
		buildImageList();
		for (String imageRow : imageListRows) {
			sb.append(imageRow);
		}
		return sb.toString();
	}

	public String getStatusAsHtml() {
		StringBuilder sb = new StringBuilder();
		String newline = System.getProperty("line.separator");
		if (statusLog.size() > 0) {
			sb.append("<ul>");
			for (String status : statusLog) {
				sb.append(String.format("<li>%s</li>%s", status.contains(newline) ? formatForPage(status) : status, newline));
				//sb.append(String.format("<li>%s</li>", status));
				// System.lineSeparator()));
			}
			sb.append("</ul>");
			statusLog.clear();
		}
		return sb.toString();
	}

	/**
	 * ページが表示された際にスクリーンに記録するメッセージを格納
	 * 
	 * @param message 記録するメッセージ
	 */
	public void logMessageToPage(String message) {
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy HH:mm:ss.SS");
		statusLog.add(String.format("[%s] %s", sdf.format(new Date()), message));
	}

	/**
	 * ページが表示された際にスクリーンに記録するメッセージを格納
	 * 
	 * @param format フォーマット文字列
	 * @param args フォーマットの字列パラメータ
	 */
	public void logMessageToPage(String format, Object... args) {
		logMessageToPage(String.format(format, args));
	}

	/**
	 * スタックセルとしてウェブページにイメージを追加。 
	 * ページにURLパラメータで指定されたイメージが表示され、メソッドの残りのパラメータは横に表示される
	 * 
	 * @param url 表示するイメージのURL
	 * @param bucket イメージファイルを格納しているバケット
	 * @param key バケット内のイメージファイルのキー
	 */
	public void addImageToPage(String url, String bucket, String key) {
		StringBuilder sb = new StringBuilder();
		sb.append("<div class=\"imageblock\"><table><tr>");
		sb.append("<td><img src=\"" + url + "\" /></td>");
		sb.append("<td><table>");
		sb.append("<tr><td><b>Bucket:</b></td><td>" + bucket + "</td></tr>");
		sb.append("<tr><td><b>Key:</b></td><td>" + key + "</td></tr>");
		sb.append("</table></td></tr></table></div>");
		imageListRows.add(sb.toString());
	}

	// runtime.configに設定がある場合は、カスタム設定ファイルに記載したステートメント毎に、
	// ElasticBeanstalkによって記された情報を含むファイルを指し示す
	// プロビジョンしたDynamoDBテーブルの名前と
	// アプリケーションが配備され実行するリージョンと追加する
	private void prepSettings() {
		String settingsFileName = System.getProperty("runtime.settings");

		if (settingsFileName != null && !settingsFileName.isEmpty()) {
			File settingsFile = new File(settingsFileName);
			if (settingsFile.exists()) {
				Properties properties = new Properties();
				try {
					// ファイルから各プロパティを取り出し、システムプロパティに追加する
					properties.load(new FileInputStream(settingsFile));
					for (Entry<Object, Object> entry : properties.entrySet()) {
						System.setProperty((String)entry.getKey(), (String)entry.getValue());
					}
					
					logMessageToPage("Properties imported from %s", settingsFileName);
				} catch (Exception ex) {
					logMessageToPage("Error while loading settings from %s:%s%s", settingsFileName,
							System.lineSeparator(), ex.getMessage());
					return;
				}
			}
			else {
				logMessageToPage("Settings file was specified but doesn't exist.");
			}
		}
		else {
			logMessageToPage("No settings file speicified.");
		}
	}

	private String formatForPage(String status) {
		String temp = status.replaceAll("  ", "&nbsp;&nbsp;");
		temp = temp.replaceAll(System.getProperty("line.separator"), "<br/>");
		
		return String.format("<div>%s</div>", temp);
	}

	/**
	 * BuildImageList - ページにイメージを表示するために必要な情報を集める
	 * 
	 */
	private void buildImageList() {
		imageListRows.clear();
		if (credentials != null) {
			AmazonDynamoDBClient dynamoDbClient = labCode.createDynamoDbClient(credentials);
			AmazonS3Client s3Client = labCode.createS3Client(credentials);

			List<Map<String, AttributeValue>> images = labCode.getImageItems(dynamoDbClient);
			if (images != null) {
				labCode.addItemsToPage(s3Client, images);
			} else {
				logMessageToPage("List of images came back from DynamoDB empty.");
			}
		} else {
			logMessageToPage("Skipped building image list because no credentials were found.");
		}
	}

}
