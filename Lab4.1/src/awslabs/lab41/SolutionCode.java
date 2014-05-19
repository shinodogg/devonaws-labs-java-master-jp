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
import com.amazonaws.services.identitymanagement.model.CreateRoleRequest;
import com.amazonaws.services.identitymanagement.model.DeleteRolePolicyRequest;
import com.amazonaws.services.identitymanagement.model.DeleteRoleRequest;
import com.amazonaws.services.identitymanagement.model.GetRoleRequest;
import com.amazonaws.services.identitymanagement.model.GetUserRequest;
import com.amazonaws.services.identitymanagement.model.ListRolePoliciesRequest;
import com.amazonaws.services.identitymanagement.model.ListRolePoliciesResult;
import com.amazonaws.services.identitymanagement.model.ListUsersRequest;
import com.amazonaws.services.identitymanagement.model.NoSuchEntityException;
import com.amazonaws.services.identitymanagement.model.PutRolePolicyRequest;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import com.amazonaws.services.s3.model.DeleteBucketRequest;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceClient;
import com.amazonaws.services.securitytoken.model.AssumeRoleRequest;
import com.amazonaws.services.securitytoken.model.AssumeRoleResult;
import com.amazonaws.services.securitytoken.model.Credentials;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.ListTopicsRequest;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.ListQueuesRequest;

/**
 * プロジェクト: Lab4.1
 */
public abstract class SolutionCode implements ILabCode, IOptionalLabCode {

	@Override
	public String prepMode_GetUserArn(AmazonIdentityManagementClient iamClient, String userName) {
    	String userArn = null;
    	//  与えられたユーザー名を用いて、GetUserRequestオブジェクトを生成する
    	GetUserRequest getUserRequest = new GetUserRequest().withUserName(userName);

    	//  iamClientオブジェクトのgetUserメソッドを用いてリクエストを送信
    	userArn = iamClient.getUser(getUserRequest).getUser().getArn();
    	//  IAMユーザーを表すARNを返す
        return userArn;
	}

	@Override
	public String prepMode_CreateRole(AmazonIdentityManagementClient iamClient, String roleName, String policyText,
			String trustRelationshipText) {
        String roleArn = null;

        //  指定された名前と"assume role"ポリシーを用いて、CreateRoleRequestオブジェクトを生成。このポリシーがtrustRelationshipTextパラメータとなる
        CreateRoleRequest createRoleRequest = new CreateRoleRequest().withAssumeRolePolicyDocument(trustRelationshipText).withRoleName(roleName);
        //  iamClientオブジェクトのcreateRoleメソッドを用いてリクエストを送信
        //  リクエストの戻りのロールARNを取り出し格納する
        roleArn = iamClient.createRole(createRoleRequest).getRole().getArn();

        //  新しいロールの与えられたポリシーを用いてPutRolePolicyRequestオブジェクトを生成する。ポリシー名には任意の名前を指定
        PutRolePolicyRequest putRolePolicyRequest = new PutRolePolicyRequest().withPolicyDocument(policyText).withPolicyName(roleName+"_policy").withRoleName(roleName);
        // iamClientオブジェクトのputRolePolicyメソッドを用いてリクエストを送信
        iamClient.putRolePolicy(putRolePolicyRequest);

        //  新しいロールのARNを返す
        return roleArn;
	}

	@Override
	public Credentials appMode_AssumeRole(AWSSecurityTokenServiceClient stsClient, String roleArn,
			String roleSessionName) {
        Credentials credentials;

        //  与えられたロールARNおよびロールセッション名を使用して、AssumeRoleRequestオブジェクトを生成
        AssumeRoleRequest assumeRoleRequest = new AssumeRoleRequest().withRoleSessionName(roleSessionName).withRoleArn(roleArn);

        //  stsClientオブジェクトのassumeRoleメソッドを用いて、リクエストを送信 
        AssumeRoleResult assumeRoleResult = stsClient.assumeRole(assumeRoleRequest);
        //  リクエストの結果から認証情報を返す
        credentials = assumeRoleResult.getCredentials();
        return credentials;
	}


	@Override
	public AmazonS3Client appMode_CreateS3Client(Credentials credentials, Region region) {
        AmazonS3Client s3Client;
        //  与えられた認証情報を使用してBasicSessionCredentialsオブジェクトを生成する
        BasicSessionCredentials sessionCredentials = new BasicSessionCredentials(
            credentials.getAccessKeyId(), 
            credentials.getSecretAccessKey(), 
            credentials.getSessionToken());

        //  作成した基本セッション認証情報を用いてAmazonS3Clientオブジェクトを生成
        s3Client = new AmazonS3Client(sessionCredentials);
        //  S3クライアントオブジェクトのリージョンを設定
        s3Client.setRegion(region);

        //  S3クライアントオブジェクトを返す
        return s3Client;
	}

	@Override
	public void prepMode_RemoveRoles(AmazonIdentityManagementClient iamClient, String... roles) {
        for (String roleName: roles)
        {
            try
            {
                iamClient.getRole(new GetRoleRequest().withRoleName(roleName));
                System.out.println("Removing old role " + roleName);
                // 既存のポリシーを削除する
                ListRolePoliciesResult listRolePoliciesResult = iamClient.listRolePolicies(new ListRolePoliciesRequest().withRoleName(roleName));
                for (String policyName: listRolePoliciesResult.getPolicyNames())
                {
                	DeleteRolePolicyRequest deleteRolePolicyRequest = new DeleteRolePolicyRequest().withPolicyName(policyName).withRoleName(roleName);
                    iamClient.deleteRolePolicy(deleteRolePolicyRequest);
                }
                iamClient.deleteRole(new DeleteRoleRequest().withRoleName(roleName));
            }
            catch (NoSuchEntityException nse)
            {
                // ロールが存在しないので何もしない
                // 例外を握りつぶしてループを抜ける
                break;
            }
        }

	}

	
	@Override
	public void prepMode_CreateBucket(AmazonS3Client s3Client, String bucketName, Region region) {
    	// 与えられたバケット名を含むCreateBucketRequestオブジェクトを生成する
		// us-east-1以外のリージョンの場合, リージョン制約を指定する必要がある
    	CreateBucketRequest createBucketRequest;
		if (region.getName().equals("us-east-1")) {
			createBucketRequest = new CreateBucketRequest(bucketName);
		}
		else {
			createBucketRequest = new CreateBucketRequest(bucketName, com.amazonaws.services.s3.model.Region.fromValue(region.getName()));
		}
		s3Client.createBucket(createBucketRequest);
	}

	
	@Override
	public Boolean appMode_TestSnsAccess(Region region, BasicSessionCredentials credentials) {
        try
        {
        	AmazonSNSClient snsClient = new AmazonSNSClient(credentials);
        	snsClient.setRegion(region);
            snsClient.listTopics(new ListTopicsRequest());
            return true;
        }
        catch (Exception ex)
        {
            return false;
        }
	}

	
	@Override
	public Boolean appMode_TestSqsAccess(Region region, BasicSessionCredentials credentials) {
        try
        {
        	AmazonSQSClient sqsClient = new  AmazonSQSClient (credentials);
        	sqsClient.setRegion(region);
            sqsClient.listQueues(new ListQueuesRequest());
            return true;
        }
        catch (Exception ex)
        {
            return false;
        }
	}

	
	@Override
	public Boolean appMode_TestIamAccess(Region region, BasicSessionCredentials credentials) {
        try
        {
        	AmazonIdentityManagementClient iamClient = new AmazonIdentityManagementClient(credentials);
        	//iamClient.setRegion(region);
            iamClient.listUsers(new ListUsersRequest());
            return true;
        }
        catch (Exception ex)
        {
            return false;
        }
	}

	
	@Override
	public void removeLabBuckets(AmazonS3Client s3Client, List<String> bucketNames) {
        for (String bucketName: bucketNames)
        {
            try
            {
                ObjectListing objectListing = s3Client.listObjects(new ListObjectsRequest().withBucketName(bucketName));
                for (S3ObjectSummary s3ObjectSummary: objectListing.getObjectSummaries())
                {
                	DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(s3ObjectSummary.getBucketName(), s3ObjectSummary.getKey());
                	
                    s3Client.deleteObject(deleteObjectRequest);
                }

                s3Client.deleteBucket(new DeleteBucketRequest(bucketName));
            }
            catch (AmazonS3Exception s3E)
            {
                if (!s3E.getErrorCode().equals("NoSuchBucket"))
                {
                    // このエラーは期待しないもののため、再スロー
                    throw s3E;
                }
            }
        }
    }
}
