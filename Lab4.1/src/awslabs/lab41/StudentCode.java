/** 
 * Copyright 2013 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"). You may not 
 * use this file except in compliance with the License. A copy of the License 
 * is located at
 * 
 * 	http://aws.amazon.com/apache2.0/
 * 
 * or in the "LICENSE" file accompanying this file. This file is distributed 
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either 
 * express or implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */
package awslabs.lab41;

import java.util.List;

import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.services.identitymanagement.AmazonIdentityManagementClient;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceClient;
import com.amazonaws.services.securitytoken.model.Credentials;

/**
 * プロジェクト: Lab4.1
 */
public class StudentCode extends SolutionCode {
	/**
	 * 指定されたユーザのARNを検索して返す
	 * Hint: クライアントオブジェクトのgetUser()メソッドを使用する。ユーザーのARNが戻り値
	 * 
	 * @param iamClient	IAMクライアントオブジェクト
	 * @param userName	検索するユーザーの名前
	 * @return 指定されたユーザのARN
	 */
	@Override
	public String prepMode_GetUserArn(AmazonIdentityManagementClient iamClient, String userName) {
		
		//TODO: スーパークラスの呼び出しを、自分の実装メソッドと差し替える
		return super.prepMode_GetUserArn(iamClient, userName);
	}

	/**
	 * 指定されたポリシーと信頼関係テキストを使用し、指定されたロールを作成。ロールARNを返す
	 * 
	 * @param iamClient				IAMクライアントオブジェクト
	 * @param roleName				作成するロールの名前
	 * @param policyText			ロールに付加するポリシー
	 * @param trustRelationshipText	だれがロールを引き継ぐことができるかを定義するポリシー
	 * @return 新規に作成したロールのARN
	 */
	@Override
	public String prepMode_CreateRole(AmazonIdentityManagementClient iamClient, String roleName, String policyText,
			String trustRelationshipText) {

		//TODO: スーパークラスの呼び出しを、自分の実装メソッドと差し替える
		return super.prepMode_CreateRole(iamClient, roleName, policyText, trustRelationshipText);
	}

	/**
	 * 指定したロールを引き継ぐ
	 * Hint: クライアントオブジェクトのassumeRole()メソッドを使用する
	 * オプション: ここで、結果整合性の問題をみる可能性があります。. AssumeRoleパーミッションは、システム全体に浸透していない可能性があり、これによりロールを引き継ぐことが阻害される可能性があります
	 *　"AmazonServiceException"のエラーコード"AccessDenied" を確認し、少し待機した後にロール操作の引き継ぎを再試行する(再試行でexponential back-offを使用）
	 * 再試行をやめると判断した場合は、ヌルを返す
	 * 	
	 * @param stsClient			STSクライアントオブジェクト
	 * @param roleArn			引き継ぐロールのARN
	 * @param roleSessionName	ロールセッション名として使用する名前
	 * @return ロール認証情報、または問題がある場合はヌル
	 */
	@Override
	public Credentials appMode_AssumeRole(AWSSecurityTokenServiceClient stsClient, String roleArn,
			String roleSessionName) {
		
		//TODO: スーパークラスの呼び出しを、自分の実装メソッドと差し替える
		return super.appMode_AssumeRole(stsClient, roleArn, roleSessionName);
	}

	/**
	 * 与えられた認証情報（前にassumeRole()メソッドの呼び出しで返されたもの）を使用して、セッション/一時認証情報を作成する
	 * そして、セッション認証情報を使用してS3クライアントオブジェクトを作成する
	 * 
	 * @param credentials	セッション認証情報を作成するために使用する認証情報
	 * @param region		クライアントに使用するリージョンのエンドポイント
	 * @return S3クライアントオブジェクト
	 */
	@Override
	public AmazonS3Client appMode_CreateS3Client(Credentials credentials, Region region) {
		
		//TODO: スーパークラスの呼び出しを、自分の実装メソッドと差し替える
		return super.appMode_CreateS3Client(credentials, region);
	}

	/**
	 * 作成しようとしているロール名とマッチするロールを削除。これはラボ制御コードから呼び出され、ラボ実行に支障をきたす可能性があるリソースをクリーンアップするために呼び出される 
	 * 
	 * @param iamClient	IAMクライアントオブジェクト
	 * @param roles		ロール名のリスト
	 */
	@Override
	public void prepMode_RemoveRoles(AmazonIdentityManagementClient iamClient, String... roles) {
		
		//TODO: スーパークラスの呼び出しを、自分の実装メソッドと差し替える
		super.prepMode_RemoveRoles(iamClient, roles);
	}

	/**
	 * このラボで後で使用されるバケットを作成する。ラボ演習の環境を準備するためのコード 
	 * 
	 * @param s3Client		S3クライアントオブジェクト
	 * @param bucketName	作成するバケット
	 */
	@Override
	public void prepMode_CreateBucket(AmazonS3Client s3Client, String bucketName, Region region) {

		//TODO: スーパークラスの呼び出しを、自分の実装メソッドと差し替える
		super.prepMode_CreateBucket(s3Client, bucketName, region);
	}

	/**
	 * SNSトピックのリストをリクエストすることで、与えられた認証情報を使ってSNSサービスにアクセスできるかをテストする
	 * テストの仕方は問いません。なんらかのリクエストを送信して、実行を確認してください
	 * 
	 * @param region		クライアント接続に使用するリージョンエンドポイント
	 * @param credentials	使用する認証情報
	 * @return サービスがアクセス可能な場合はTrue。認証情報が拒否された場合はFalse 
	 */
	@Override
	public Boolean appMode_TestSnsAccess(Region region, BasicSessionCredentials credentials) {

		//TODO: スーパークラスの呼び出しを、自分の実装メソッドと差し替える
		return super.appMode_TestSnsAccess(region, credentials);
	}

	/**
	 * SQSキューのリストをリクエストすることで、与えられた認証情報を使ってSQSサービスにアクセスできるかをテストする
	 * テストの仕方は問いません。なんらかのリクエストを送信して、実行を確認してください
	 * 
	 * @param region		クライアント接続に使用するリージョンエンドポイント
	 * @param credentials	使用する認証情報
	 * @return サービスがアクセス可能な場合はTrue。認証情報が拒否された場合はFalse 
	 */
	@Override
	public Boolean appMode_TestSqsAccess(Region region, BasicSessionCredentials credentials) {
		
		//TODO: スーパークラスの呼び出しを、自分の実装メソッドと差し替える
		return super.appMode_TestSqsAccess(region, credentials);
	}

	/**
	 * IAMユーザーのリストをリクエストすることで、与えられた認証情報を使ってIAMサービスにアクセスできるかをテストする
	 * テストの仕方は問いません。なんらかのリクエストを送信して、実行を確認してください
	 * 
	 * @param region		クライアント接続に使用するリージョンエンドポイント
	 * @param credentials	使用する認証情報
	 * @return サービスがアクセス可能な場合はTrue。認証情報が拒否された場合はFalse 
	 */
	@Override
	public Boolean appMode_TestIamAccess(Region region, BasicSessionCredentials credentials) {
		
		//TODO: スーパークラスの呼び出しを、自分の実装メソッドと差し替える
		return super.appMode_TestIamAccess(region, credentials);
	}

	/**
	 * このラボで作成されたバケットのクリーンアップと削除
	 * 
	 * @param s3Client		S3クライアントオブジェクト
	 * @param bucketNames	削除するバケット
	 */
	@Override
	public void removeLabBuckets(AmazonS3Client s3Client, List<String> bucketNames) {
		
		//TODO: スーパークラスの呼び出しを、自分の実装メソッドと差し替える
		super.removeLabBuckets(s3Client, bucketNames);
	}
}
