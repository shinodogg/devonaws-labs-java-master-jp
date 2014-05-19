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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.AttributeValueUpdate;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.DeleteTableRequest;
import com.amazonaws.services.dynamodbv2.model.DescribeTableRequest;
import com.amazonaws.services.dynamodbv2.model.DescribeTableResult;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.QueryResult;
import com.amazonaws.services.dynamodbv2.model.TableDescription;
import com.amazonaws.services.dynamodbv2.model.UpdateItemRequest;

/**
 * プロジェクト: Lab2.2
 */
public abstract class SolutionCode implements ILabCode, IOptionalLabCode {
	@Override
	public void createAccountItem(AmazonDynamoDBClient ddbClient, String tableName, Account account) {
		// HashMap<String, AttributeValue>オブジェクトを生成し、追加するアイテムの属性を持たせる
		Map<String, AttributeValue> itemAttributes = new HashMap<String, AttributeValue>();
		// アカウントパラメータより、必要なアイテム(Company と Email)をHashMap属性に追加する
		itemAttributes.put("Company", new AttributeValue().withS(account.getCompany()));
		itemAttributes.put("Email", new AttributeValue().withS(account.getEmail()));

		// アカウントパラメータを確認し、空の文字列("") ではない値のすべてを HashMap属性に追加する
		if (!account.getFirst().equals("")) {
			itemAttributes.put("First", new AttributeValue().withS(account.getFirst()));
		}
		if (!account.getLast().equals("")) {
			itemAttributes.put("Last", new AttributeValue().withS(account.getLast()));
		}
		if (!account.getAge().equals("")) {
			itemAttributes.put("Age", new AttributeValue().withN(account.getAge()));
		}

		// PutItemRequestオブジェクトを作成し、指定したテーブルに属性を加える
		PutItemRequest putItemRequest = new PutItemRequest().withTableName(tableName).withItem(itemAttributes);

		// ddbClientクライアントのputItemメソッドを用いてリクエストを送信
		ddbClient.putItem(putItemRequest);
	}

	@Override
	public QueryResult lookupByHashKey(AmazonDynamoDBClient ddbClient, String tableName, String company) {
		// 指定した会社名(company name)を含むAttributeValueオブジェクトを生成する
		AttributeValue attributeValue = new AttributeValue().withS(company);
		// desired comparison ("EQ")、および、
		// 会社名(company name)属性値をもつConditionオブジェクトを生成
		Condition condition = new Condition().withComparisonOperator("EQ").withAttributeValueList(attributeValue);

		// QueryRequestを生成し、前に生成した条件のテーブルに対して
		// consistent readを実行する
		QueryRequest queryRequest = new QueryRequest().withTableName(tableName).withConsistentRead(true);
		queryRequest.addKeyConditionsEntry("Company", condition);

		// ddbClientオブジェクトのqueryメソッドを呼び出してクエリを実行し、結果を返す
		return ddbClient.query(queryRequest);
	}

	@Override
	public void updateIfMatch(AmazonDynamoDBClient ddbClient, String tableName, String email, String company,
			String firstNameTarget, String firstNameMatch) {
		// 指定したテーブルに対しUpdateItemRequestオブジェクトを生成する
		UpdateItemRequest updateItemRequest = new UpdateItemRequest().withTableName(tableName);

		// company nameとemail addressをもつAttributeValueオブジェクトを含むリクエストにKeyEntryエレメントを追加する
		// 
		updateItemRequest.addKeyEntry("Company", new AttributeValue().withS(company));
		updateItemRequest.addKeyEntry("Email", new AttributeValue().withS(email));

		// firstNameMatchパラメータに値を持つExpectedAttributeValueオブジェクトを含むリクエストに、ExpectedEntryエレメントを追加する
		// 
		updateItemRequest.addExpectedEntry("First",
				new ExpectedAttributeValue().withValue(new AttributeValue().withS(firstNameMatch)));

		// firstNameTargetパラメータに値を持つAttributeValueUpdateオブジェクトを含むリクエストに、AttributeUpdatesEntryエレメントを追加する
		// 
		updateItemRequest.addAttributeUpdatesEntry("First",
				new AttributeValueUpdate().withAction("PUT").withValue(new AttributeValue().withS(firstNameTarget)));

		// ddbClientのupdateItemを使用してリクエストを送信
		ddbClient.updateItem(updateItemRequest);
	}

	@Override
	public void deleteTable(AmazonDynamoDBClient ddbClient, String tableName) {
		String tableStatus = getTableStatus(ddbClient, tableName);
		if (tableStatus.equals("ACTIVE")) {
			System.out.println("Deleting pre-existing table.");
			DeleteTableRequest deleteTableRequest = new DeleteTableRequest().withTableName(tableName);
			ddbClient.deleteTable(deleteTableRequest);
			waitForStatus(ddbClient, tableName, "NOTFOUND");

			System.out.println("Table deletion confirmed.");
		} else if (tableStatus.equals("NOTFOUND")) {
			System.out.println("Skipped deletion operation. Table not found.");
		} else {
			System.out.println("Skipped deletion operation. Table not in correct state.");
		}
	}

	@Override
	public void buildTable(AmazonDynamoDBClient ddbClient, String tableName) {
		System.out.println("Creating table.");
		CreateTableRequest createTableRequest = new CreateTableRequest().withTableName(tableName);
		createTableRequest.setAttributeDefinitions(new ArrayList<AttributeDefinition>());
		// 属性を定義
		createTableRequest.getAttributeDefinitions().add(
				new AttributeDefinition().withAttributeName("Company").withAttributeType("S"));
		createTableRequest.getAttributeDefinitions().add(
				new AttributeDefinition().withAttributeName("Email").withAttributeType("S"));
		// キースキーマを定義
		createTableRequest.setKeySchema(new ArrayList<KeySchemaElement>());
		createTableRequest.getKeySchema().add(new KeySchemaElement().withAttributeName("Company").withKeyType("HASH"));
		createTableRequest.getKeySchema().add(new KeySchemaElement().withAttributeName("Email").withKeyType("RANGE"));
		// プロビジョンドスループットを定義
		createTableRequest.setProvisionedThroughput(new ProvisionedThroughput().withReadCapacityUnits(10L)
				.withWriteCapacityUnits(5L));

		// createリクエストを送信
		ddbClient.createTable(createTableRequest);
		// テーブルがアクティブになるまで停止
		waitForStatus(ddbClient, tableName, "ACTIVE");
		System.out.println("Table created and active.");
	}

	@Override
	public String getTableStatus(AmazonDynamoDBClient ddbClient, String tableName) {
		TableDescription tableDescription = getTableDescription(ddbClient, tableName);
		if (tableDescription == null) {
			return "NOTFOUND";
		}
		return tableDescription.getTableStatus();
	}

	@Override
	public TableDescription getTableDescription(AmazonDynamoDBClient ddbClient, String tableName) {
		try {
			DescribeTableRequest describeTableRequest = new DescribeTableRequest().withTableName(tableName);

			DescribeTableResult describeTableResult = ddbClient.describeTable(describeTableRequest);

			return describeTableResult.getTable();
		} catch (AmazonServiceException ase) {
			// テーブルが見つからなければ問題なし
			// エラーがそれ以外だった場合、例外を再スロー
			if (!ase.getErrorCode().equals("ResourceNotFoundException")) {
				throw ase;
			}
			return null;
		}
	}

	@Override
	public void waitForStatus(AmazonDynamoDBClient ddbClient, String tableName, String status) {
		while (!getTableStatus(ddbClient, tableName).equals(status)) {
			// 1秒間スリープ
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// 例外処理
			}
		}
	}

}
