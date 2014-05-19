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
package awslabs.lab22;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.QueryResult;
import com.amazonaws.services.dynamodbv2.model.TableDescription;

/**
 * プロジェクト: Lab2.2
 */

public class StudentCode extends SolutionCode {

	/**
	 * アカウントパラメータに指定した値からDynamoDBアイテムを生成する
	 * アイテムの属性名は対応するAccountオブジェクトのプロパティ名にマッチする必要がある
	 * Accountオブジェクトの空のフィールドに属性は追加しないこと
	 * 
	 * CompanyおよびEmail属性はテーブルキーの一部なため、これらはこのメソッドが呼ばれる際、常にAccountオブジェクトに渡される
	 * 
	 * 
	 * 重要: Account.Ageプロパティが文字列として返されても、数字としてアイテムに追加すること
	 * 
	 * 
	 * @param ddbClient DynamoDBクライアントオブジェクト
	 * @param tableName アイテムを追加するテーブル名
	 * @param account 追加するデータを含むAccountオブジェクト
	 */
	@Override
	public void createAccountItem(AmazonDynamoDBClient ddbClient, String tableName, Account account) {
		// TODO: スーパークラスの呼び出しを、自分の実装メソッドと差し替える
		super.createAccountItem(ddbClient, tableName, account);
	}

	/**
	 * Construct a query using the criteria specified and return the result object. Hint: Use the query() method of the
	 * client object.
	 * 
	 * @param ddbClient TDynamoDBクライアントオブジェクト
	 * @param tableName クエリするテーブル名
	 * @param company 検索するcompany name
	 * @return リクエストの結果を含むQueryResultオブジェクト
	 */
	@Override
	public QueryResult lookupByHashKey(AmazonDynamoDBClient ddbClient, String tableName, String company) {
		// TODO: スーパークラスの呼び出しを、自分の実装メソッドと差し替える
		return super.lookupByHashKey(ddbClient, tableName, company);
	}

	/**
	 * companyおよびemailのパラメータ値がマッチするアイテムをテーブル内に探す。 属性値がfirstNameMatch パラメータにマッチする場合に限り、First属性のfirstNameTargetパラメータに値をセットする
	 * 
	 * ヒント:クライアントオブジェクトのupdateItem() メソッドを使用したシングルリクエストで達成可能
	 * 
	 * @param ddbClient DynamoDBクライアントオブジェクト
	 * @param tableName アイテムを含むテーブル名
	 * @param email Email属性にマッチする値
	 * @param company Company属性にマッチする値
	 * @param firstNameTarget マッチが見つかった場合のFirst属性の新しい値
	 * @param firstNameMatch First属性にマッチする値
	 */
	@Override
	public void updateIfMatch(AmazonDynamoDBClient ddbClient, String tableName, String email, String company,
			String firstNameTarget, String firstNameMatch) {
		// TODO: スーパークラスの呼び出しを、自分の実装メソッドと差し替える
		super.updateIfMatch(ddbClient, tableName, email, company, firstNameTarget, firstNameMatch);
	}

	// オプション課題開始
	/**
	 * 指定したテーブルのテーブル説明をリクエストし、呼び出し元に返す。 
	 * ヒント：クライアントオブジェクトのdescribeTable()メソッドを使用する
	 * 
	 * @param ddbClient DynamoDBクライアントオブジェクト
	 * @param tableName テーブル名
	 * @return The TableDescription object for the table. Null if the table wasn't found.
	 */
	@Override
	public TableDescription getTableDescription(AmazonDynamoDBClient ddbClient, String tableName) {
		// TODO: スーパークラスの呼び出しを、自分の実装メソッドと差し替える
		return super.getTableDescription(ddbClient, tableName);
	}

	/**
	 * 指定したテーブルに関連付けられたテーブルステータスの文字列を返す。テーブルステータスは、TableDescriptionオブジェクトのプロパティ
	 * 
	 * Hint: getTableDescription()を呼び出してTableDescriptionを作る。 メソッドがヌルを返した場合、このメソッドから
	 *  "NOTFOUND" を返す
	 * 
	 * @param ddbClient DynamoDBクライアントオブジェクト
	 * @param tableName テーブル名
	 * @return テーブルステータス文字列。テーブルが存在しない場合または見つからない場合は "NOTFOUND"
	 */
	@Override
	public String getTableStatus(AmazonDynamoDBClient ddbClient, String tableName) {
		// TODO: スーパークラスの呼び出しを、自分の実装メソッドと差し替える
		return super.getTableStatus(ddbClient, tableName);
	}

	/**
	 * テーブルステータスが与えられたステータスストリングとマッチするまで、このスレッドの実行を一時停止する 
	 * ヒント：ステータスを繰り返しリクエストし、リクエスト間はスレッドをスリープにする必要がある。
	 * このラボでは停止する時間は任意で指定可能だが、1秒より長くすること
	 * 
	 * @param ddbClient DynamoDBクライアントオブジェクト
	 * @param tableName 検査するテーブル名
	 * @param status 望むステータス
	 */
	@Override
	public void waitForStatus(AmazonDynamoDBClient ddbClient, String tableName, String status) {
		// TODO: スーパークラスの呼び出しを、自分の実装メソッドと差し替える
		super.waitForStatus(ddbClient, tableName, status);
	}

	/**
	 * このラボで使用するテーブルを作成する。テーブルステータスが"ACTIVE"になるまで、このメソッドからは返らない
	 * ヒント：上記で実行した待つためのwaitForStatus()メソッドを呼び出す
	 * パラメータにマッチするテーブルを構築する
	 * -- Attributes - "Company" 文字列, および "Email" 同じく文字列 -- Hash Key Attribute - "Company" --Range Key Attribute - "Email" -- Provisioned Capacity - 5 reads/5 writes
	 * 
	 * このメソッドは、テーブルの再構築が必要と判断された場合に、ラボコントローラーコードから呼び出されます
	 * 例：スキーマが期待とマッチしない
	 * 
	 * このタスクを完了するためには、DynamoDBでテーブルを作成する方法を調べる必要がある（このコース資料ではカバーしていません）
	 * 
	 * 
	 * @param ddbClient DynamoDBクライアントオブジェクト
	 * @param tableName 作成するテーブル名
	 */
	@Override
	public void buildTable(AmazonDynamoDBClient ddbClient, String tableName) {
		// TODO: スーパークラスの呼び出しを、自分の実装メソッドと差し替える
		super.buildTable(ddbClient, tableName);
	}

	/**
	 * 指定したテーブルを削除する。このメソッドは、存在しているテーブルがこのラボに不適当と判断された際に、ラボコントローラーコードから呼び出される
	 * 
	 * 
	 * @param ddbClient DynamoDBクライアントオブジェクト
	 * @param tableName テーブル名
	 */
	@Override
	public void deleteTable(AmazonDynamoDBClient ddbClient, String tableName) {
		// TODO: スーパークラスの呼び出しを、自分の実装メソッドと差し替える
		super.deleteTable(ddbClient, tableName);
	}

	// オプション課題終了
}
