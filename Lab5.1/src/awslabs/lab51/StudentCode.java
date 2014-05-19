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

import java.util.List;
import java.util.Map;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.TableDescription;
import com.amazonaws.services.s3.AmazonS3Client;

/**
 * プロジェクト: Lab5.1
 */
public class StudentCode extends SolutionCode {
	public StudentCode(Lab51 lab) {
		super(lab);
	}

	/**
	 * getUrlForItem - 用意されたS3クライアントを用いて、指定されたバケットとキーに対して事前署名付きURLを生成する
	 * URLは1分で期限が切れるようにする
	 * 
	 * @param s3Client	S3クライアントオブジェクト
	 * @param key		リンクを用意するオブジェクトのキー
	 * @param bucket	オブジェクトを格納するバケット
	 * @return 			オブジェクトの事前署名付きURL
	 */
	@Override
	public String getUrlForItem(AmazonS3Client s3Client, String key, String bucket) {
		//TODO: スーパークラスの呼び出しを、自分の実装メソッドと差し替える
		return super.getUrlForItem(s3Client, key, bucket);
	}

	/**
	 * getImageItems - DynamoDBから、ページに表示するイメージの詳細を含むアイテムコレクションを返す
	 * アイテムを含むテーブルの名前は、SeSSIONTABLEの値から特定する 
	 * PARAM3に定義されたキープリフィックスを基に結果をフィルターする。スキャン操作を使用してアイテムを特定するようにする 
	 * アイテムコレクションは結果のオブジェクトに入る
	 * 
	 * @param dynamoDbClient	DynamoDBクライアントオブジェクト
	 * @return	マッチングアイテムコレクション
	 */
	@Override
	public List<Map<String, AttributeValue>> getImageItems(AmazonDynamoDBClient dynamoDbClient) {
		//TODO: スーパークラスの呼び出しを、自分の実装メソッドと差し替える
		return super.getImageItems(dynamoDbClient);
	}

	/**
	 * createS3Client - REGION設定にリージョン制約を適用するS3クライアントオブジェクトを生成して返す
	 * 
	 * @param credentials	クライアントオブジェクトに使用する認証情報
	 * @return	クライアントオブジェクト
	 */
	@Override
	public AmazonS3Client createS3Client(AWSCredentials credentials) {
		//TODO: スーパークラスの呼び出しを、自分の実装メソッドと差し替える
		return super.createS3Client(credentials);
	}

	/**
	 * createDynamoDbClient - REGION設定にリージョン制約を適用するDynamoDBクライアントオブジェクトを生成して返す
	 * 
	 * @param credentials 	クライアントオブジェクトに使用する認証情報
	 * @return クライアントオブジェクト
	 */
	@Override
	public AmazonDynamoDBClient createDynamoDbClient(AWSCredentials credentials) {
		//TODO: スーパークラスの呼び出しを、自分の実装メソッドと差し替える
		return super.createDynamoDbClient(credentials);
	}

	/**
	 * addItemsToPage - このメソッドは、DynamoDBのアイテムコレクションを、ウェブページに表示できる要素に変換するために使用する
	 * このタスクを完了するには、以下を行う
	 * (1) コレクション中のアイテムをループし、 "Key" および "Bucket"属性値を抽出
	 * (2) "Key" および "Bucket"値を使用して、各オブジェクトの事前署名付きURLを生成。URLを生成するには、GetUrlForItem()メソッドの実装を呼び出し、戻り値を掴む
	 * (3) 各アイテムについて、_Default.AddImageToPage()を呼び出し、メソッドパラメータとしてキー、バケット、およびURLの値を渡す
	 * 
	 * @param s3Client	S3クライアントオブジェクト
	 * @param items		ページに追加するアイテムコレクション
	 */
	@Override
	public void addItemsToPage(AmazonS3Client s3Client, List<Map<String, AttributeValue>> items) {
		//TODO: スーパークラスの呼び出しを、自分の実装メソッドと差し替える
		super.addItemsToPage(s3Client, items);
	}

	/**
	 * isImageInDynamo - DynamoDBテーブルを検査し、指定したハッシュキーにマッチするアイテムを含むかどうかを特定する
	 * 
	 * @param dynamoDbClient	DynamoDBクライアントオブジェクト
	 * @param tableName			検索するテーブル名
	 * @param key				特定するアイテムのキー
	 * @return アイテムが存在していれば真、なければ偽 
	 */
	@Override
	public Boolean isImageInDynamo(AmazonDynamoDBClient dynamoDbClient, String tableName, String key) {
		//TODO: スーパークラスの呼び出しを、自分の実装メソッドと差し替える
		return super.isImageInDynamo(dynamoDbClient, tableName, key);
	}

	/**
	 * getTableDescription - 指定したテーブルのテーブル説明をリクエストし、それを呼び出し元に返す
	 * 
	 * @param dynamoDbClient	DynamoDBクライアントオブジェクト
	 * @param tableName			テーブル名
	 * @return テーブル説明オブジェクト。テーブルがなければヌル
	 */
	@Override
	public TableDescription getTableDescription(AmazonDynamoDBClient ddbClient, String tableName) {
		//TODO: スーパークラスの呼び出しを、自分の実装メソッドと差し替える
		return super.getTableDescription(ddbClient, tableName);
	}

	/**
	 * validateSchema - tableDescriptionパラメータで定義されるスキーマを有効化する
	 * テーブルは以下の特徴を持つことを求められる
	 *   スキーマ - "Key" および "Bucket"の最低でも2つの属性（双方ともに文字列型）
	 *   ハッシュキー -  "Key"という名前の文字列型の1つの属性
	 *   レンジキー - "Bucket"という名前の文字列型の1つの属性
	 * 
	 * @param tableDescription	テーブル定義
	 * @return スキーマが期待と一致するときは真、スキーマが無効または例外が投げられたときは偽
	 */
	@Override
	public Boolean validateSchema(TableDescription tableDescription) {
		//TODO: スーパークラスの呼び出しを、自分の実装メソッドと差し替える
		return super.validateSchema(tableDescription);
	}
	
	/**
	 * getTableStatus - 指定したテーブルに関連するテーブルステータスの文字列を返す 
	 * テーブルステータスはTableDescriptionオブジェクトのプロパティ
	 * 
	 * @param dynamoDbClient	DynamoDBクライアントオブジェクト
	 * @param tableName			検査するテーブル名
	 * @return テーブルステータス文字列。テーブルが存在しない、または特定できない場合は "NOTFOUND" 
	 */
	@Override
	public String getTableStatus(AmazonDynamoDBClient ddbClient, String tableName) {
		//TODO: スーパークラスの呼び出しを、自分の実装メソッドと差し替える
		return super.getTableStatus(ddbClient, tableName);
	}

	/**
	 * waitForStatus - テーブルステータスが、与えられたステータスの文字列とマッチするまで、このスレッドの実行を停止する
	 * 
	 * @param dynamoDbClient	DynamoDBクライアントオブジェクト
	 * @param tableName			テーブル名
	 * @param status			望むテーブルステータス
	 */
	@Override
	public void waitForStatus(AmazonDynamoDBClient ddbClient, String tableName, String status) {
		//TODO: スーパークラスの呼び出しを、自分の実装メソッドと差し替える
		super.waitForStatus(ddbClient, tableName, status);
	}

	/**
	 * deleteTable - 指定したテーブルを削除。このメソッドは、ラボコントローラーコードが既存のテーブルがこのラボで向こうと判断した場合に呼び出される 
	 * 
	 * @param dynamoDbClient	DynamoDBクライアントオブジェクト
	 * @param tableName			削除するテーブル名
	 */
	@Override
	public void deleteTable(AmazonDynamoDBClient ddbClient, String tableName) {
		//TODO: スーパークラスの呼び出しを、自分の実装メソッドと差し替える
		super.deleteTable(ddbClient, tableName);
	}

	/**
	 * addImage - 指定されたイメージをS3にアップロードし、DynamoDBのイメージへの参照を加える 
	 * イメージを表すDynamoDBのアイテムは、以下の2つの属性を持っているべきである
	 *   Key - S3のオブジェクトへのキー
	 *   Bucket - そのオブジェクトの存在するバケット
	 *   
	 * S3のオブジェクトには何もパーミッションの設定は何も行わず、制限されたデフォルトを保つ
	 * このメソッドは、ラボ制御コードがラボで使われるイメージがない、またはDynamoDBで正しく参照されないと判断された場合に呼び出される
	 * 最低1回は実行される
	 * 
	 * @param dynamoDbClient	DynamoDBクライアントオブジェクト
	 * @param tableName			アイテムを入れるテーブル名
	 * @param s3Client			S3クライアントオブジェクト
	 * @param bucketName		S3オブジェクトにつかうバケットの名前 
	 * @param imageKey			S3オブジェクトに使用するキー 
	 * @param filePath			アップロードするイメージへのパス
	 */
	@Override
	public void addImage(AmazonDynamoDBClient dynamoDbClient, String tableName, AmazonS3Client s3Client,
			String bucketName, String imageKey, String filePath) {
		//TODO: スーパークラスの呼び出しを、自分の実装メソッドと差し替える
		super.addImage(dynamoDbClient, tableName, s3Client, bucketName, imageKey, filePath);
	}

	/**
	 * buildTable - ラボで使われるテーブルを作成。テーブルステータスが"ACTIVE"となるまでこのメソッドから戻らない
	 * 
	 * 下記パラメータにマッチするテーブルを構築する
	 *   属性 - "Key" 文字列、"Bucket" 文字列
	 *   ハッシュキー属性 - "Key"
	 *   レンジキー属性 - "Bucket"
	 *   プロビジョンドキャパシティ - 5 Reads/5 Writes
	 *   
	 * このメソッドは、ラボを準備するために最低1回はラボ制御コードから呼び出される。
	 * また、ラボ制御コードが、テーブルを再構築する必要があると判断した場合にも呼び出される (例：スキーマが期待とマッチしない）
	 * 
	 * @param dynamoDbClient	DynamoDBクライアントオブジェクト
	 * @param tableName			作成するテーブル名
	 */
	@Override
	public void buildTable(AmazonDynamoDBClient ddbClient, String tableName) {
		//TODO:スーパークラスの呼び出しを、自分の実装メソッドと差し替える
		super.buildTable(ddbClient, tableName);
	}

}
