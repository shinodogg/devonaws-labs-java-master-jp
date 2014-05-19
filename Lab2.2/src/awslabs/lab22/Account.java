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
package awslabs.lab22;

/**
 * プロジェクト: Lab2.2
 * 
 * Accountクラスは、DynamoDBのアイテムの中身を表す情報を保持する
 * 値を持たないプロパティは、空の文字列("")で示す
 */
public class Account {  
    private String eMail = "";
    private String first = "";
    private String last = "";
    private String age = "";
    private String company = "";
	
    public String getEmail() {
	return eMail;
    }
    
    public String getFirst() {
	return first;
    }
    
    public String getLast() {
	return last;
    }
    
    public String getAge() {
	return age;
    }
    	
    public String getCompany() {
	return company;
    }
    	
    public void setEmail(String eMail) {
	this.eMail = eMail;
    }
    	
    public void setFirst(String first) {
	this.first = first;
    }
    
    public void setLast(String last) {
	this.last = last;
    }
    
    public void setAge(String age) {
	this.age = age;
    }
    	
    public void setCompany(String company) {
	this.company = company;
    }
    
    public Account withEmail(String eMail) {
	this.eMail = eMail;
	return this;
    }
    	
    public Account withFirst(String first) {
	this.first = first;
	return this;
    }
    
    public Account withLast(String last) {
	this.last = last;
	return this;
    }
    
    public Account withAge(String age) {
	this.age = age;
	return this;
    }
    	
    public Account withCompany(String company) {
	this.company = company;
	return this;
    }
}