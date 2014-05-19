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
import java.util.Date;

import com.amazonaws.regions.Region;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;

/**
 * Project: Lab2.1
 * 
 * このラボの主な目的は、S3をプログラムで操作する経験を得ることです
 */
@SuppressWarnings("unused")
public class StudentCode extends SolutionCode {
    /**
     * S3クライアントオブジェクトを使用して、指定したバケットを作成する 
     * ヒント：クライアントオブジェクトの createBucket()メソッドを使用する
     * 		 リージョンがus-east-1以外の場合、
     * 		 リクエストにリージョンを明示的に指定する必要があります
     * 
     * @param s3Client	 S3クライアントオブジェクト	
     * @param bucketName 作成するバケットの名前
     */
    @Override
    public void createBucket(AmazonS3 s3Client, String bucketName, Region region) {
	//TODO: スーパークラスの呼び出しを、自分の実装メソッドと差し替える
	super.createBucket(s3Client, bucketName, region);
    }

    /**
     * 指定したバケットに与えられたアイテムをアップロードする
     * ヒント: クライアントオブジェクトのputObject() メソッドを使用する
     * 
     * @param s3Client	 S3クライアントオブジェクト	
     * @param bucketName ターゲットバケットの名前
     * @param sourceFile アップロードするファイルの名前
     * @param objectKey  新しいS3オブジェクトに割り当てるキー
     */
    @Override
    public void putObject(AmazonS3 s3Client, String bucketName, String sourceFile, String objectKey) {
	//TODO: スーパークラスの呼び出しを、自分の実装メソッドと差し替える
	super.putObject(s3Client, bucketName, sourceFile, objectKey);
    }

    /**
     * コンソールにオブジェクトキーとアイテムサイズを表示して、指定したバケットの中身をリストアップ
     * ヒント: クライアントオブジェクトのlistObjects()メソッドを使用する
     * 
     * @param s3Client	 S3クライアントオブジェクト	
     * @param bucketName リストアップする中身を格納するバケットの名前
     */
    @Override
    public void listObjects(AmazonS3 s3Client, String bucketName) {
	//TODO: スーパークラスの呼び出しを、自分の実装メソッドと差し替える
	super.listObjects(s3Client, bucketName);
    }

    /**
     * Change the ACL for the specified object to make it publicly readable.
     * Hint: Call the setObjectAcl() method of the client object. Use the CannedAccessControlList 
     * enumeration to set the ACL for the object to PublicRead.
     * 
     * @param s3Client	 S3クライアントオブジェクト	
     * @param bucketName オブジェクトを格納するバケットの名前
     * @param key        The key used to identify the object.
     */
    @Override
    public void makeObjectPublic(AmazonS3 s3Client, String bucketName, String key) {
	//TODO: スーパークラスの呼び出しを、自分の実装メソッドと差し替える
	super.makeObjectPublic(s3Client, bucketName, key);
    }

    /**
     * 指定したアイテムの事前署名URLを作成して返す。URLの有効期限を生成から一時間と設定する 
     * URLの有効期限をURLを生成してから1時間に設定する 
     * ヒント: クライアントオブジェクトのgeneratePresignedUrl()を使用する
     * 
     * @param s3Client	 S3クライアントオブジェクト	
     * @param bucketName オブジェクトを格納するバケットの名前
     * @param key	 オブジェクトを特定するキー
     * @return 		 オブジェクトの事前署名URL
     */
    @Override
    public String generatePreSignedUrl(AmazonS3 s3Client, String bucketName, String key) {
	//TODO: スーパークラスの呼び出しを、自分の実装メソッドと差し替える
	return super.generatePreSignedUrl(s3Client, bucketName, key);
    }

    /**
     * 指定したバケットを削除する。 クライアントオブジェクトのdeleteBucket()メソッドを使用してバケットを削除するが、 
     * まずはじめにバケットの中身を削除する必要がある。、 
     * 中身を削除するには、オブジェクトを列挙し、個々に削除するか(DeleteObject() メソッド) 、 
     * バッチで削除する(DeleteObjects() メソッド)
     * 
     * このタスクの目的は、使用していないAWSリソースの自動化をするようなアプリケーションの記述を試すことです
     * 
     * 
     * @param s3Client	 S3クライアントオブジェクト	
     * @param bucketName 削除するバケットの名前
     */
    @Override
    public void deleteBucket(AmazonS3 s3Client, String bucketName) {
	//TODO: スーパークラスの呼び出しを、自分の実装メソッドと差し替える.
	super.deleteBucket(s3Client, bucketName);
    }
}
