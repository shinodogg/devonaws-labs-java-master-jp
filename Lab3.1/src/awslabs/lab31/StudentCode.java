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
package awslabs.lab31;

import java.util.List;

import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.Message;

/**
 * プロジェクト: Lab3.1
 */
public class StudentCode extends SolutionCode {

	/**
	 * 与えられたキュー名を用いてSQSキューを作成し、新しいキューのURLを返す
	 * ヒント：クライアントオブジェクトのcreateQueue()メソッドを使用する URLは戻り値
	 * 
	 * @param sqsClient SQSクライアントオブジェクト
	 * @param queueName 作成するキュー名
	 * @return 新たに作成しれたキューのURL
	 */
	@Override
	public String createQueue(AmazonSQSClient sqsClient, String queueName) {
		// TODO: スーパークラスの呼び出しを、自分の実装メソッドと差し替える
		return super.createQueue(sqsClient, queueName);
	}

	/**
	 * 指定されたキューのARNに対しSQSサービスをクエリして返す 
	 * ヒント：クライアントオブジェクトのgetQueueAttributes()メソッドを使用する
	 * リクエストする属性はQueueArnと命名
	 * @param sqsClient SQSクライアントオブジェクト
	 * @param queueUrl 検査するキューのURL
	 * @return キューのARNを含む文字列
	 */
	@Override
	public String getQueueArn(AmazonSQSClient sqsClient, String queueUrl) {
		// TODO: スーパークラスの呼び出しを、自分の実装メソッドと差し替える
		return super.getQueueArn(sqsClient, queueUrl);
	}

	/**
	 * SNSトピックを作成し、新たに作成したトピックのARNを返す。ヒント：クライアントオブジェクトのcreateTopic() メソッドを使用する
	 * 戻り値にトピックのARNが含まれる
	 * 
	 * @param snsClient SNSクライアントオブジェクト
	 * @param topicName 作成するトピック名
	 * @return 新たに作成したトピックのARN
	 */
	@Override
	public String createTopic(AmazonSNSClient snsClient, String topicName) {
		// TODO: スーパークラスの呼び出しを、自分の実装メソッドと差し替える
		return super.createTopic(snsClient, topicName);
	}

	/**
	 * SQSキューへ通知を発行するSNSサブスクリプションを作成する。ヒント：クライアントオブジェクトの subscribe()メソッドを使用する
	 * サブスクリプションエンドポイントとはqueueArnパラメータで与えられる
	 * 
	 * @param snsClient SNSクライアントオブジェクト
	 * @param queueArn サブスクリプションエンドポイントで使用するキューのARN
	 * @param topicArn サブスクライブするトピックのARN
	 */
	@Override
	public void createSubscription(AmazonSNSClient snsClient, String queueArn, String topicArn) {
		// TODO: スーパークラスの呼び出しを、自分の実装メソッドと差し替える
		super.createSubscription(snsClient, queueArn, topicArn);
	}

	/**
	 * 指定されたSNSトピックへのメッセージを発行。ヒント：クライアントオブジェクトのpublish() メソッドを使用する
	 * 
	 * @param snsClient SNSクライアントオブジェクト
	 * @param topicArn メッセージを送るトピックのARN
	 * @param subject 発行するメッセージの件名
	 * @param message 発行するメッセージの中身
	 */
	@Override
	public void publishTopicMessage(AmazonSNSClient snsClient, String topicArn, String subject, String message) {
		// TODO: スーパークラスの呼び出しを、自分の実装メソッドと差し替える
		super.publishTopicMessage(snsClient, topicArn, subject, message);
	}

	/**
	 * 指定されたキューにメッセージを送る。ヒント：クライアントオブジェクトのsendMessage() メソッドを使用する
	 * 
	 * @param sqsClient SQSクライアントオブジェクト
	 * @param queueUrl メッセージを置くキューのURL
	 * @param messageText キューに置くメッセージの中身
	 */
	@Override
	public void postToQueue(AmazonSQSClient sqsClient, String queueUrl, String messageText) {
		// TODO: スーパークラスの呼び出しを、自分の実装メソッドと差し替える
		super.postToQueue(sqsClient, queueUrl, messageText);
	}

	/**
	 * 1つのリクエストで指定されたSQSキューから10メッセージくらいまで読み込む。ヒント：クライアントオブジェクトのreceiveMessage()メソッドを使用する
	 * リクエストでは、最大メッセージ数を10に設定
	 * 
	 * @param sqsClient SQSクライアントオブジェクト
	 * @param queueUrl メッセージを含むキューのURL
	 * @return キューからのメッセージのリスト
	 */
	@Override
	public List<Message> readMessages(AmazonSQSClient sqsClient, String queueUrl) {
		// TODO: スーパークラスの呼び出しを、自分の実装メソッドと差し替える
		return super.readMessages(sqsClient, queueUrl);
	}

	/**
	 * 指定されたキューから指定されたメッセージを削除する。ヒント：クライアントオブジェクトのdeleteMessage()メソッドを使用する
	 * 
	 * @param sqsClient SQSクライアントオブジェクト
	 * @param queueUrl メッセージを含むキューURL
	 * @param receiptHandle 削除するメッセージのreceipt handle
	 */
	@Override
	public void removeMessage(AmazonSQSClient sqsClient, String queueUrl, String receiptHandle) {
		// TODO: スーパークラスの呼び出しを、自分の実装メソッドと差し替える
		super.removeMessage(sqsClient, queueUrl, receiptHandle);
	}

	/**
	 * 指定されたSNSトピックのサブスクリプションをすべて削除する。 Hint: クライアントオブジェクトの getSubscriptions() メソッドですべてのサブスクリプションを呼び出し、
	 * クライアントオブジェクトのunsubscribe()メソッドを用いて詳細とともに繰り返す
	 * 
	 * @param snsClient SNSクライアントオブジェクト
	 * @param topicArn サブスクリプションを削除するSNSトピック
	 */
	@Override
	public void deleteSubscriptions(AmazonSNSClient snsClient, String topicArn) {
		// TODO: スーパークラスの呼び出しを、自分の実装メソッドと差し替える
		super.deleteSubscriptions(snsClient, topicArn);
	}

	/**
	 * 指定したSNSトピックを削除する。 Hint: Use the deleteTopic() method of the client object.
	 * 
	 * @param snsClient SNSクライアントオブジェクト
	 * @param topicArn 削除するトピックのARN
	 */
	@Override
	public void deleteTopic(AmazonSNSClient snsClient, String topicArn) {
		// TODO: スーパークラスの呼び出しを、自分の実装メソッドと差し替える
		super.deleteTopic(snsClient, topicArn);
	}

	/**
	 * 指定したSQSキューを削除する。 Hint: クライアントオブジェクトのdeleteQueue() メソッドを使用する
	 * 
	 * @param sqsClient SQSクライアントオブジェクト
	 * @param queueUrl 削除するキューのURL
	 */
	@Override
	public void deleteQueue(AmazonSQSClient sqsClient, String queueUrl) {
		// TODO: スーパークラスの呼び出しを、自分の実装メソッドと差し替える
		super.deleteQueue(sqsClient, queueUrl);
	}

	// オプションタスク
	/**
	 * 与えられたSNSトピックが、自身のキューにメッセージを発行するパーミッションを付与する。これを実現するためには、
	 * 適切に指定されたポリシーステートメントを作成し、キューのPolicy属性にそれを割り当てる必要がある
	 * （正しくこのタスクを実行するためには調査が必要です）
	 * 
	 * @param sqsClient SQSクライアントオブジェクト
	 * @param queueArn キューを定義するARN。ポリシーステートメントにおいて、Resourceとして用いられる
	 * @param queueUrl キューのURL。ポリシー属性を更新する目的でキューを特定するのに使用される
	 *            
	 * @param topicArn キューに発行するトピックのARN。 ポリシーステートメントにおいてARN Conditionのソースとして用いられる
	 *            .
	 */
	@Override
	public void grantNotificationPermission(AmazonSQSClient sqsClient, String queueArn, String queueUrl, String topicArn) {
		// TODO: スーパークラスの呼び出しを、自分の実装メソッドと差し替える
		super.grantNotificationPermission(sqsClient, queueArn, queueUrl, topicArn);
	}
}
