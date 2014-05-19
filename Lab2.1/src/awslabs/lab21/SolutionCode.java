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
package awslabs.lab21;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.amazonaws.regions.Region;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import com.amazonaws.services.s3.model.DeleteBucketRequest;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.DeleteObjectsRequest.KeyVersion;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;

/**
 * Project: Lab2.1
 */
public abstract class SolutionCode implements ILabCode, IOptionalLabCode {

    @Override
    public void createBucket(AmazonS3 s3Client, String bucketName, Region region) {
    	// 与えられたバケット名を含むCreateBucketRequestオブジェクトを作成
		// リージョンがus-east-1以外の場合、リージョンの制約を指定する必要がある
    	CreateBucketRequest createBucketRequest;
		if (region.getName().equals("us-east-1")) {
			createBucketRequest = new CreateBucketRequest(bucketName);
		}
		else {
			createBucketRequest = new CreateBucketRequest(bucketName, com.amazonaws.services.s3.model.Region.fromValue(region.getName()));
		}

        //  s3ClientオブジェクトのcreateBucketメソッドを用いてリクエストを送信
        s3Client.createBucket(createBucketRequest);
        
    }

    @Override
    public void putObject(AmazonS3 s3Client, String bucketName, String sourceFileName, String objectKey) {
        File sourceFile = new File(sourceFileName);

        // メソッドのパラメータで指定された値を用いて、PutObjectRequestオブジェクトを生成 
    	PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectKey, sourceFile);
    	
        // s3ClientオブジェクトのputObjectメソッドを用いてリクエストを送信し、オブジェクトをアップロード
        s3Client.putObject(putObjectRequest);
    }

    @Override
    public void listObjects(AmazonS3 s3Client, String bucketName) {
        // 指定されたバケット名で、ListObjectsRequestオブジェクトを生成
    	ListObjectsRequest listObjectsRequest = new ListObjectsRequest().withBucketName(bucketName);

        // s3ClientオブジェクトのlistObjectsメソッドを用いてリクエストを送信
        ObjectListing objectListing = s3Client.listObjects(listObjectsRequest);

        // コンソールにオブジェクトキーとサイズを表示 
        for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
        	System.out.println(objectSummary.getKey() + " (size: " + objectSummary.getSize() + ")");
        }
    }

    @Override
    public void makeObjectPublic(AmazonS3 s3Client, String bucketName, String key) {

    	// s3ClientオブジェクトのsetObjectAclメソッド を用いて、指定したオブジェクトのACLを
    	// CannedAccessControlList.PublicReadに設定
        s3Client.setObjectAcl(bucketName, key, CannedAccessControlList.PublicRead);
    }

    @Override
    public String generatePreSignedUrl(AmazonS3 s3Client, String bucketName, String key) {
    	Date nowPlusOneHour = new Date(System.currentTimeMillis() + 3600000L);
    	
    	// 指定したオブジェクトのGeneratePresignedUrlRequestオブジェクトを生成
    	GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, key);
    	// nowPlusOneHourオブジェクトへのリクエストに含める有効期限の値を設定 
    	// (これにより、今から一時間後が指定される). 
    	generatePresignedUrlRequest.setExpiration(nowPlusOneHour);
    	
        // s3ClientのgeneratePresignedUrlを用いて、リクエストを送信
    	URL url = s3Client.generatePresignedUrl(generatePresignedUrlRequest);
    	// 文字列としてURLを返す
        return url.toString();
    }
    
    @Override
    public void deleteBucket(AmazonS3 s3Client, String bucketName) {
    	// まずはじめに、バケットの削除を試みる
    	DeleteBucketRequest deleteBucketRequest = new DeleteBucketRequest(bucketName);
    	
    	try {
    	    s3Client.deleteBucket(deleteBucketRequest);
    	    // ここに到達した場合、エラーが生成されていないためバケットは削除されたとみなしてリターン
    	    return;
    	}
    	catch (AmazonS3Exception ex) {
    		if (!ex.getErrorCode().equals("BucketNotEmpty")) {
    			// 唯一扱う例外はBucketNotEmptyなので、それ以外すべては再スローする
    			throw ex; 
    	    }
    	}
    	
    	// ここに到達した場合、バケットは空ではないため、中身を削除して再実行
    	List<KeyVersion> keys = new ArrayList<KeyVersion>();
    	for (S3ObjectSummary obj : s3Client.listObjects(bucketName).getObjectSummaries()) {
    	    // オブジェクトのリストにキーを追加
    	    keys.add(new KeyVersion(obj.getKey()));
    	}
    	// リクエストを生成してオブジェクトを削除
    	DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(bucketName);
    	deleteObjectsRequest.withKeys(keys);
    	// オブジェクト削除リクエストを送信
    	s3Client.deleteObjects(deleteObjectsRequest);
    	
    	// バケットがここで空となったので、バケットの削除を再試行
    	s3Client.deleteBucket(deleteBucketRequest);
    }
}
