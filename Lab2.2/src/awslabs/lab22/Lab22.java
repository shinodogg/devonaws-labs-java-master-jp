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
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import awslabs.labutility.LabUtility;

import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.QueryResult;
import com.amazonaws.services.dynamodbv2.model.TableDescription;

/**
 * プロジェクト: Lab2.2
 */
public class Lab22 {

	private static Region region = Region.getRegion(Regions.US_EAST_1);

	// NON-STUDENT_CODE開始
	private static ILabCode labCode = new StudentCode();
	private static IOptionalLabCode optionalLabCode = new StudentCode();
	private static String tableName = "Accounts";

	/**
	 * ラボコード実行の流れを制御
	 */
	public static void main(String[] args) {
		try {
			// DynamoDBクライアントを作成し、リージョンにUS East(Virginia)を指定
			AmazonDynamoDBClient ddbClient = new AmazonDynamoDBClient(new ClasspathPropertiesFileCredentialsProvider());
			ddbClient.setRegion(region);

			List<Account> accounts = new ArrayList<Account>();
			accounts.add(new Account().withCompany("Amazon.com").withEmail("johndoe@amazon.com").withFirst("John")
					.withLast("Doe").withAge("33"));
			accounts.add(new Account().withCompany("Asperatus Tech").withEmail("janedoe@amazon.com").withFirst("Jane")
					.withLast("Doe").withAge("24"));
			accounts.add(new Account().withCompany("Amazon.com").withEmail("jimjohnson@amazon.com").withFirst("Jim")
					.withLast("Johnson"));

			// テーブルスキーマが期待通りのものかを確認し、
			// 必要に応じて問題を修正する
			if (!confirmTableSchema(ddbClient, tableName)) {
				System.out.print("Deleting. ");
				optionalLabCode.deleteTable(ddbClient, tableName);
				System.out.print("Rebuilding. ");
				optionalLabCode.buildTable(ddbClient, tableName);
				System.out.println("Done.");
			}

			System.out.println("Adding items to table.");
			// アカウントの作成
			for (Account account : accounts) {
				labCode.createAccountItem(ddbClient, tableName, account);
				System.out.println("Added item: " + account.getCompany() + "/" + account.getEmail());
			}

			System.out.println("Requesting matches for Company == Amazon.com");
			QueryResult queryResult = labCode.lookupByHashKey(ddbClient, tableName, "Amazon.com");
			if (queryResult != null && queryResult.getCount() > 0) {
				// レコードが見つかる
				for (Map<String, AttributeValue> item : queryResult.getItems()) {
					System.out.println("Item Found-");

					for (Entry<String, AttributeValue> attribute : item.entrySet()) {
						System.out.print("    " + attribute.getKey() + ":");
						if (attribute.getKey().equals("Age")) {
							System.out.println(attribute.getValue().getN());
						} else {
							System.out.println(attribute.getValue().getS());
						}
					}
				}
			} else {
				System.out.println("No matches found.");
			}

			// 条件付きで、レコードを更新
			System.out.print("Attempting update. ");
			labCode.updateIfMatch(ddbClient, tableName, "jimjohnson@amazon.com", "Amazon.com", "James", "Jim");
			System.out.println("Done.");
		} catch (Exception ex) {
			LabUtility.dumpError(ex);
		}
	}

	/**
	 * ラボに期待されるスキーマにテーブルがマッチするかを確認する
	 * 手作業で構築しているため、期待通りではない可能性もあるため
	 * 
	 * @param ddbClient DynomoDBクライアントオブジェクト
	 * @param tableName 確認するテーブルの名前
	 * 
	 * @return テーブルのスキーマが正しければTrue、問題が見つかればFalse
	 */
	private static Boolean confirmTableSchema(AmazonDynamoDBClient ddbClient, String tableName) {
		System.out.println("Confirming table schema.");
		TableDescription tableDescription = optionalLabCode.getTableDescription(ddbClient, tableName);
		if (tableDescription == null) {
			System.out.println("Table does not exist.");
			// テーブルが存在しなければスキーマはマッチしない
			return false;
		}
		if (!tableDescription.getTableStatus().equals("ACTIVE")) {
			System.out.println("Table is not active.");
			return false;
		}

		if (tableDescription.getAttributeDefinitions() == null || tableDescription.getKeySchema() == null) {
			System.out.println("Schema doesn't match.");
			return false;
		}
		for (AttributeDefinition attributeDefinition : tableDescription.getAttributeDefinitions()) {
			String attributeName = attributeDefinition.getAttributeName();
			if (attributeName.equals("Company") || attributeName.equals("Email") || attributeName.equals("First")
					|| attributeName.equals("Last")) {
				if (!attributeDefinition.getAttributeType().equals("S")) {
					// マッチする属性はあるが、タイプが違っている
					System.out.println(attributeDefinition.getAttributeName()
							+ " attribute is wrong type in attribute definition.");
					return false;
				}
			} else if (attributeName.equals("Age")) {
				if (!attributeDefinition.getAttributeType().equals("N")) {
					System.out.println("Age attribute is wrong type in attribute definition.");
					// マッチする属性はあるが、タイプが違っている
					return false;
				}
			}
		}
		// ここに到達した場合、属性は問題ない
		// ここでキースキーマを確認する
		if (tableDescription.getKeySchema().size() != 2) {
			System.out.println("Wrong number of elements in the key schema.");
			return false;
		}
		for (KeySchemaElement keySchemaElement : tableDescription.getKeySchema()) {
			String attributeName = keySchemaElement.getAttributeName();
			if (attributeName.equals("Company")) {
				if (!keySchemaElement.getKeyType().equals("HASH")) {
					// マッチする属性はあるが、タイプが違っている
					System.out.println("Company attribute is wrong type in key schema.");
					return false;
				}
			} else if (attributeName.equals("Email")) {
				if (!keySchemaElement.getKeyType().equals("RANGE")) {
					// マッチする属性はあるが、タイプが違っている
					System.out.println("Email attribute is wrong type in key schema.");
					return false;
				}
			} else {
				System.out.println("Unexpected attribute (" + keySchemaElement.getAttributeName()
						+ ") in the key schema.");
			}
		}
		System.out.println("Table schema is as expected.");
		// チェック完了
		return true;

	}
	// NON-STUDENT CODE終了
}
