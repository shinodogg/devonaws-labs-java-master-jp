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

import java.util.HashMap;
import java.util.List;

import com.amazonaws.auth.policy.Policy;
import com.amazonaws.auth.policy.Principal;
import com.amazonaws.auth.policy.Resource;
import com.amazonaws.auth.policy.Statement;
import com.amazonaws.auth.policy.Statement.Effect;
import com.amazonaws.auth.policy.actions.SQSActions;
import com.amazonaws.auth.policy.conditions.ConditionFactory;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.CreateTopicRequest;
import com.amazonaws.services.sns.model.CreateTopicResult;
import com.amazonaws.services.sns.model.DeleteTopicRequest;
import com.amazonaws.services.sns.model.ListSubscriptionsByTopicRequest;
import com.amazonaws.services.sns.model.ListSubscriptionsByTopicResult;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.SubscribeRequest;
import com.amazonaws.services.sns.model.Subscription;
import com.amazonaws.services.sns.model.UnsubscribeRequest;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.CreateQueueResult;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.DeleteQueueRequest;
import com.amazonaws.services.sqs.model.GetQueueAttributesRequest;
import com.amazonaws.services.sqs.model.GetQueueAttributesResult;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SetQueueAttributesRequest;

/**
 * プロジェクト: Lab3.1
 */
public abstract class SolutionCode implements ILabCode, IOptionalLabCode {

	@Override
	public String createQueue(AmazonSQSClient sqsClient, String queueName) {
		// TODO: 与えられたキュー名を使用して、CreateQueueRequestオブジェクトを生成
		CreateQueueRequest createQueueRequest = new CreateQueueRequest().withQueueName(queueName);

		// TODO: sqsClientオブジェクトのcreateQueueメソッドを使用してリクエストを送信.
		CreateQueueResult createQueueResult = sqsClient.createQueue(createQueueRequest);

		// TODO: リクエストの結果から、キューURLを返す
		return createQueueResult.getQueueUrl();
	}

	@Override
	public String getQueueArn(AmazonSQSClient sqsClient, String queueUrl) {
		// TODO: 指定したキューおよび"QueueArn"という名前の属性のGetQueueAttributesRequestを生成する
		GetQueueAttributesRequest getQueueAttributesRequest = new GetQueueAttributesRequest().withQueueUrl(queueUrl)
				.withAttributeNames("QueueArn");

		// TODO: sqsClientのgetQueueAttributesメソッドを用いてリクエストを送信.
		GetQueueAttributesResult getQueueAttributesResult = sqsClient.getQueueAttributes(getQueueAttributesRequest);

		// TODO: QueueArn属性値を返す
		return getQueueAttributesResult.getAttributes().get("QueueArn");
	}

	@Override
	public String createTopic(AmazonSNSClient snsClient, String topicName) {
		// TODO: 指定されたトピック名でCreateTopicRequestオブジェクトを生成
		CreateTopicRequest createTopicRequest = new CreateTopicRequest().withName(topicName);

		// TODO: snsClientオブジェクトのcreateTopicメソッドを使用してリクエストを送信.
		CreateTopicResult createTopicResult = snsClient.createTopic(createTopicRequest);

		// TODO: リクエストの結果からトピックARNを返す.
		return createTopicResult.getTopicArn();
	}

	@Override
	public void createSubscription(AmazonSNSClient snsClient, String queueArn, String topicArn) {
		// TODO: 指定されたキューARNエンドポイントおよびトピックARNに"sqs"プロトコルを用いてSubscribeRequestオブジェクトを生成
		// 
		SubscribeRequest subscribeRequest = new SubscribeRequest().withEndpoint(queueArn).withProtocol("sqs")
				.withTopicArn(topicArn);

		// TODO: snsClientオブジェクトのsubscribeメソッドを用いてリクエストを送信
		snsClient.subscribe(subscribeRequest);
	}

	@Override
	public void publishTopicMessage(AmazonSNSClient snsClient, String topicArn, String subject, String message) {
		// TODO: 与えられた件名(subject)、メッセージ(message）、およびトピックARN(topic ARN)を用いてPublishRequestConstructオブジェクトを生成
		PublishRequest publishRequest = new PublishRequest().withMessage(message).withSubject(subject)
				.withTopicArn(topicArn);

		// TODO: snsClientオブジェクトのpublishメソッドを用いてリクエストを送信
		snsClient.publish(publishRequest);
	}

	@Override
	public void postToQueue(AmazonSQSClient sqsClient, String queueUrl, String messageText) {
		// TODO: 与えられたURLおよびメッセージを用いてSendMessageRequestオブジェクトを生成
		SendMessageRequest sendMessageRequest = new SendMessageRequest().withMessageBody(messageText).withQueueUrl(
				queueUrl);

		// TODO: sqsClientオブジェクトのsendMessageメソッドを用いてリクエストを送信
		sqsClient.sendMessage(sendMessageRequest);
	}

	@Override
	public List<Message> readMessages(AmazonSQSClient sqsClient, String queueUrl) {
		ReceiveMessageResult receiveMessageResult;

		// TODO: 与えられたキューURLを用いて、ReceiveMessageRequestオブジェクトを生成
		// 最大メッセージ数を10に設定
		ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest().withMaxNumberOfMessages(10)
				.withQueueUrl(queueUrl);

		// TODO: sqsClientオブジェクトのreceiveMessageメソッドを用いてリクエストを送信
		// 既に定義されているreceiveMessageResultオブジェクトに結果を格納
		receiveMessageResult = sqsClient.receiveMessage(receiveMessageRequest);

		return receiveMessageResult.getMessages();
	}

	@Override
	public void removeMessage(AmazonSQSClient sqsClient, String queueUrl, String receiptHandle) {
		// TODO: 与えられたキューURLおよびreceipt handleを用いて、DeleteMessageRequestオブジェクトを生成
		DeleteMessageRequest deleteMessageRequest = new DeleteMessageRequest().withQueueUrl(queueUrl)
				.withReceiptHandle(receiptHandle);

		// TODO: sqsClientオブジェクトのdeleteMessageメソッドを用いてリクエストを送信
		sqsClient.deleteMessage(deleteMessageRequest);
	}

	@Override
	public void deleteSubscriptions(AmazonSNSClient snsClient, String topicArn) {
		// TODO: 与えられたトピックARNを用いて、ListSubscriptionsByTopicRequestオブジェクトを生成
		ListSubscriptionsByTopicRequest listSubscriptionsByTopicRequest = new ListSubscriptionsByTopicRequest()
				.withTopicArn(topicArn);

		// TODO: snsClientオブジェクトのlistSubscriptionsByTopicメソッドを用いてリクエストを送信
		ListSubscriptionsByTopicResult listSubscriptionsByTopicResult = snsClient
				.listSubscriptionsByTopic(listSubscriptionsByTopicRequest);

		// TODO: リクエスト結果のサブスクリプションの繰り返し
		// TODO: 各サブスクリプションに対し、サブスクリプションARNを用いてUnsubscribeRequestオブジェクトを生成
		// TODO: 各サブスクリプションに対し、snsClientオブジェクトのunsubscribeメソッドを用いて送信
		for (Subscription subscription : listSubscriptionsByTopicResult.getSubscriptions()) {
			UnsubscribeRequest unsubscribeRequest = new UnsubscribeRequest().withSubscriptionArn(subscription
					.getSubscriptionArn());
			snsClient.unsubscribe(unsubscribeRequest);
		}
	}

	@Override
	public void deleteTopic(AmazonSNSClient snsClient, String topicArn) {
		// TODO: 与えられたトピックARNを用いてDeleteTopicRequestオブジェクトを生成
		DeleteTopicRequest deleteTopicRequest = new DeleteTopicRequest().withTopicArn(topicArn);

		// TODO: snsClientオブジェクトのdeleteTopicメソッドを用いてリクエストを送信
		snsClient.deleteTopic(deleteTopicRequest);
	}

	@Override
	public void deleteQueue(AmazonSQSClient sqsClient, String queueUrl) {
		// TODO: 与えられたキューURLを用いてDeleteQueueRequestオブジェクトを生成
		DeleteQueueRequest deleteQueueRequest = new DeleteQueueRequest().withQueueUrl(queueUrl);

		// TODO: sqsClientオブジェクトのdeleteQueueメソッドを用いてリクエストを送信
		sqsClient.deleteQueue(deleteQueueRequest);
	}

	@Override
	public void grantNotificationPermission(AmazonSQSClient sqsClient, String queueArn, String queueUrl, String topicArn) {

		Statement statement = new Statement(Effect.Allow).withActions(SQSActions.SendMessage)
				.withPrincipals(new Principal("*")).withConditions(ConditionFactory.newSourceArnCondition(topicArn))
				.withResources(new Resource(queueArn));

		Policy policy = new Policy("SubscriptionPermission").withStatements(statement);

		HashMap<String, String> attributes = new HashMap<String, String>();
		attributes.put("Policy", policy.toJson());

		// ポリシーのキュー属性をセットするためにリクエストを作成
		SetQueueAttributesRequest setQueueAttributesRequest = new SetQueueAttributesRequest().withQueueUrl(queueUrl)
				.withAttributes(attributes);

		// キューポリシーをセット
		sqsClient.setQueueAttributes(setQueueAttributesRequest);
	}
}
