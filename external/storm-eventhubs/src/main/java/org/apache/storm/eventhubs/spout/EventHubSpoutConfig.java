/*******************************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package org.apache.storm.eventhubs.spout;

import org.apache.storm.eventhubs.core.EventHubConfig;
import org.apache.storm.eventhubs.core.FieldConstants;

/**
 * EventHub configuration. This class remains in
 * org.apache.storm.eventhubs.spout package for backward compatibility reasons
 * with other libraries, and client code.
 *
 */
public class EventHubSpoutConfig extends EventHubConfig {
	private static final long serialVersionUID = 1L;

	private String targetAddress;
	private String outputStreamId;

	/**
	 * Constructor to configure EventHub connection parameters, and partition settings.
	 * @param username username to connect as
	 * @param password password to connect as 
	 * @param namespace Service bus namespace. For soverign clouds
	 * @param entityPath
	 * @param partitionCount
	 */
	public EventHubSpoutConfig(String username, String password, String namespace, String entityPath,
			int partitionCount) {
		super(namespace, entityPath, username, password, partitionCount);
	}

	public EventHubSpoutConfig(String username, String password, String namespace, String entityPath,
			int partitionCount, String zkConnectionString) {
		this(username, password, namespace, entityPath, partitionCount);
		setZkConnectionString(zkConnectionString);
	}

	// Keep this constructor for backward compatibility
	public EventHubSpoutConfig(String username, String password, String namespace, String entityPath,
			int partitionCount, String zkConnectionString, int checkpointIntervalInSeconds, int receiverCredits) {
		this(username, password, namespace, entityPath, partitionCount, zkConnectionString);
		setCheckpointIntervalInSeconds(checkpointIntervalInSeconds);
		setReceiverCredits(receiverCredits);
	}

	public EventHubSpoutConfig(String username, String password, String namespace, String entityPath,
			int partitionCount, String zkConnectionString, int checkpointIntervalInSeconds, int receiverCredits,
			long enqueueTimeFilter) {
		this(username, password, namespace, entityPath, partitionCount, zkConnectionString, checkpointIntervalInSeconds,
				receiverCredits);
		setEnqueueTimeFilter(enqueueTimeFilter);
	}

	// Keep this constructor for backward compatibility
	public EventHubSpoutConfig(String username, String password, String namespace, String entityPath,
			int partitionCount, String zkConnectionString, int checkpointIntervalInSeconds, int receiverCredits,
			int maxPendingMsgsPerPartition, long enqueueTimeFilter) {

		this(username, password, namespace, entityPath, partitionCount, zkConnectionString, checkpointIntervalInSeconds,
				receiverCredits);
		setMaxPendingMsgsPerPartition(maxPendingMsgsPerPartition);
		setEnqueueTimeFilter(enqueueTimeFilter);
	}

	public EventHubSpoutConfig(String username, String password, String namespace, String entityPath,
			int partitionCount, String zkConnectionString, int checkpointIntervalInSeconds, int receiverCredits,
			int maxPendingMsgsPerPartition, long enqueueTimeFilter, int batchSize) {

		this(username, password, namespace, entityPath, partitionCount, zkConnectionString, checkpointIntervalInSeconds,
				receiverCredits);
		setMaxPendingMsgsPerPartition(maxPendingMsgsPerPartition);
		setEnqueueTimeFilter(enqueueTimeFilter);
	}

	public EventHubSpoutConfig(String username, String password, String namespace, String entityPath,
			int partitionCount, int receiveEventsMaxCount) {
		this(username, password, namespace, entityPath, partitionCount);
		setReceiveEventsMaxCount(receiveEventsMaxCount);
	}

	public String getOutputStreamId() {
		return outputStreamId;
	}

	public void setOutputStreamId(String outputStreamId) {
		this.outputStreamId = outputStreamId;
	}

	public String getTargetAddress() {
		return targetAddress;
	}

	public void setTargetAddress(String targetAddress) {
		this.targetAddress = targetAddress;
	}
}
